import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class lab6 {
  // global constants
  static int BYTESPERLINE = 4;

  // helper functions
  public static int getIndex(int addr, int indexWidth, int blocks) {
    // right shift by 2 for byte offset, plus any block offset
    // then bitmask
    return (addr >>> (int) (2 + Math.log(blocks) / Math.log(2))) & (int) (Math.pow(2, indexWidth) - 1);
  }

  public static int getTag(int addr, int tagWidth) {
    return addr >>> (32 - tagWidth);
  }

  public static int getAddr(String line) {
    String[] parsedline = line.split("\\s+");
    return Integer.parseUnsignedInt(parsedline[1], 16);
  }

  public static void main(String[] args) throws IOException {
    // Cache #1
    // Cache size: 2048B Associativity: 1 Block size: 1
    // Hits: 4028929 Hit Rate: 80.58%
    // ---------------------------
    int hits = 0;
    int total = 0;

    ArrayList<Integer> cache = new ArrayList<>(); // cache
    int indexWidth = 9;
    int tagWidth = 21;
    int cacheLines = 512;
    ArrayList<Integer> valid = new ArrayList<>(); // store valid bit
    for (int i = 0; i < cacheLines; i++) { // initialize to all 0's
      valid.add(0);
      cache.add(0);
    }

    try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
      String line;
      while ((line = br.readLine()) != null) {
        int addr = getAddr(line);
        int idx = getIndex(addr, indexWidth, 1);
        int tag = getTag(addr, tagWidth);
        if (valid.get(idx) == 1 && cache.get(idx) == tag) {
          hits++;
          total++;
        } else {
          total++;
          cache.set(idx, tag);
          valid.set(idx, 1);
        }
      }
    }
    System.out.println("Cache #1");
    System.out.println("Cache size: " + cacheLines * BYTESPERLINE + "B\tAssociativity: 1" + "\tBlock size: 1");
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits, ((double) hits / total) * 100);

    // Cache #2
    // Cache size: 2048B Associativity: 1 Block size: 2
    // Hits: 4025132 Hit Rate: 80.50%
    // ---------------------------

    indexWidth = 8;
    tagWidth = 21;
    cacheLines = 256;
    hits = 0;
    total = 0;
    int blocks = 2;

    cache.clear();
    valid.clear();
    for (int i = 0; i < cacheLines; i++) { // initialize to all 0's
      valid.add(0);
      cache.add(0);
    }
    try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
      String line;
      while ((line = br.readLine()) != null) {
        int addr = getAddr(line);
        int idx = getIndex(addr, indexWidth, blocks);
        int tag = getTag(addr, tagWidth);
        if (valid.get(idx) == 1 && cache.get(idx) == tag) {
          hits++;
          total++;
        } else {
          total++;
          cache.set(idx, tag);
          valid.set(idx, 1);
        }
      }
    }

    System.out.println("Cache #2");
    System.out.println(
        "Cache size: " + blocks * cacheLines * BYTESPERLINE + "B\tAssociativity: 1" + "\tBlock size: " + blocks);
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits, ((double) hits / total) * 100);

    // Cache #3
    // Cache size: 2048B Associativity: 1 Block size: 4
    // Hits: 4129379 Hit Rate: 82.59%
    // ---------------------------

    indexWidth = 7;
    tagWidth = 21;
    cacheLines = 128;
    hits = 0;
    total = 0;
    blocks = 4;

    cache.clear();
    valid.clear();
    for (int i = 0; i < cacheLines; i++) { // initialize to all 0's
      valid.add(0);
      cache.add(0);
    }
    try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
      String line;
      while ((line = br.readLine()) != null) {
        int addr = getAddr(line);
        int idx = getIndex(addr, indexWidth, blocks);
        int tag = getTag(addr, tagWidth);
        if (valid.get(idx) == 1 && cache.get(idx) == tag) {
          hits++;
          total++;
        } else {
          total++;
          cache.set(idx, tag);
          valid.set(idx, 1);
        }
      }
    }
    System.out.println("Cache #3");
    System.out.println(
        "Cache size: " + blocks * cacheLines * BYTESPERLINE + "B\tAssociativity: 1" + "\tBlock size: " + blocks);
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits, ((double) hits / total) * 100);

    // Cache #4
    // Cache size: 2048B Associativity: 2 Block size: 1
    // Hits: 4302993 Hit Rate: 86.06%
    // ---------------------------
    int associativity = 2;
    ArrayList<ArrayList<Integer>> assocCache = new ArrayList<>();
    indexWidth = 8;
    tagWidth = 22;
    cacheLines = 256;
    hits = 0;
    total = 0;
    blocks = 1;

    cache.clear();
    valid.clear();
    for (int i = 0; i < cacheLines; i++) {
      valid.add(0);
      assocCache.add(new ArrayList<Integer>());
    }
    try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
      String line;
      while ((line = br.readLine()) != null) {
        int addr = getAddr(line);
        int idx = getIndex(addr, indexWidth, blocks);
        int tag = getTag(addr, tagWidth);
        ArrayList<Integer> set = assocCache.get(idx);
        if (valid.get(idx) == 0) {
          valid.set(idx, 1);
          total++;
          set.add(tag);
          continue;
        }
        boolean found = false;
        for (int i = 0; i < set.size(); i++) {
          if (set.get(i) == tag) {
            set.remove(i);
            set.add(tag);
            hits++;
            found = true;
            break;
          }
        }
        if (found == false) {
          set.add(tag);
        }
        total++;
        if (set.size() > associativity) {
          set.remove(0);
        }

      }
      System.out.println("Cache #4");
      System.out.println(
          "Cache size: " + blocks * associativity * cacheLines * BYTESPERLINE + "B\tAssociativity: " + associativity
              + "\tBlock size: "
              + blocks);
      System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits, ((double) hits / total) * 100);
    }
    // Cache #5
    // Cache size: 2048B Associativity: 4 Block size: 1
    // Hits: 4365120 Hit Rate: 87.30%
    // ---------------------------
    associativity = 4;
    indexWidth = 7;
    tagWidth = 23;
    cacheLines = 128;
    hits = 0;
    total = 0;
    blocks = 1;

    cache.clear();
    valid.clear();
    assocCache.clear();
    for (int i = 0; i < cacheLines; i++) {
      valid.add(0);
      assocCache.add(new ArrayList<Integer>());
    }
    try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
      String line;
      while ((line = br.readLine()) != null) {
        int addr = getAddr(line);
        int idx = getIndex(addr, indexWidth, blocks);
        int tag = getTag(addr, tagWidth);
        ArrayList<Integer> set = assocCache.get(idx);
        if (valid.get(idx) == 0) {
          valid.set(idx, 1);
          total++;
          set.add(tag);
          continue;
        }
        boolean found = false;
        for (int i = 0; i < set.size(); i++) {
          if (set.get(i) == tag) {
            set.remove(i);
            set.add(tag);
            hits++;
            found = true;
            break;
          }
        }
        if (found == false) {
          set.add(tag);
        }
        total++;
        if (set.size() > associativity) {
          set.remove(0);
        }

      }
      System.out.println("Cache #5");
      System.out.println(
          "Cache size: " + blocks * associativity * cacheLines * BYTESPERLINE + "B\tAssociativity: " + associativity
              + "\tBlock size: "
              + blocks);
      System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits, ((double) hits / total) * 100);
    }

    // Cache #6
    // Cache size: 2048B Associativity: 4 Block size: 4
    // Hits: 4454531 Hit Rate: 89.09%
    // ---------------------------
    associativity = 4;
    indexWidth = 5;
    tagWidth = 25;
    cacheLines = 32;
    hits = 0;
    total = 0;
    blocks = 4;

    cache.clear();
    valid.clear();
    assocCache.clear();
    for (int i = 0; i < cacheLines; i++) {
      valid.add(0);
      assocCache.add(new ArrayList<Integer>());
    }
    try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
      String line;
      while ((line = br.readLine()) != null) {
        int addr = getAddr(line);
        int idx = getIndex(addr, indexWidth, blocks);
        int tag = getTag(addr, tagWidth);
        ArrayList<Integer> set = assocCache.get(idx);
        if (valid.get(idx) == 0) {
          valid.set(idx, 1);
          total++;
          set.add(tag);
          continue;
        }
        boolean found = false;
        for (int i = 0; i < set.size(); i++) {
          if (set.get(i) == tag) {
            set.remove(i);
            set.add(tag);
            hits++;
            found = true;
            break;
          }
        }
        if (found == false) {
          set.add(tag);
        }
        total++;
        if (set.size() > associativity) {
          set.remove(0);
        }

      }
      System.out.println("Cache #6");
      System.out.println(
          "Cache size: " + blocks * associativity * cacheLines * BYTESPERLINE + "B\tAssociativity: " + associativity
              + "\tBlock size: "
              + blocks);
      System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits, ((double) hits / total) * 100);
    }

    // Cache #7
    // Cache size: 4096B Associativity: 1 Block size: 1
    // Hits: 4278036 Hit Rate: 85.56%
    // ---------------------------
    indexWidth = 10;
    tagWidth = 20;
    cacheLines = 1024;
    hits = 0;
    total = 0;
    blocks = 1;

    cache.clear();
    valid.clear();
    for (int i = 0; i < cacheLines; i++) { // initialize to all 0's
      valid.add(0);
      cache.add(0);
    }
    try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
      String line;
      while ((line = br.readLine()) != null) {
        int addr = getAddr(line);
        int idx = getIndex(addr, indexWidth, blocks);
        int tag = getTag(addr, tagWidth);
        if (valid.get(idx) == 1 && cache.get(idx) == tag) {
          hits++;
          total++;
        } else {
          total++;
          cache.set(idx, tag);
          valid.set(idx, 1);
        }
      }
    }
    System.out.println("Cache #7");
    System.out.println(
        "Cache size: " + blocks * cacheLines * BYTESPERLINE + "B\tAssociativity: 1" + "\tBlock size: " + blocks);
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits, ((double) hits / total) * 100);
  }
}
