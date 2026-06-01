import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class lab6 {

  static int BYTESPERLINE = 4;

  public static int getIndex(int addr, int indexWidth, int blocks) {
    return ((addr >>> (int) (2 + Math.log(blocks) / Math.log(2))) &
        (int) (Math.pow(2, indexWidth) - 1));
  }

  public static int getTag(int addr, int tagWidth) {
    return addr >>> (32 - tagWidth);
  }

  public static int getAddr(String line) {
    String[] parsedline = line.split("\\s+");
    return Integer.parseUnsignedInt(parsedline[1], 16);
  }

  public static void main(String[] args) throws IOException {

    // Cache #1: size=2048B, assoc=1, block=1
    int indexWidth1 = 9, tagWidth1 = 21, cacheLines1 = 512, blocks1 = 1, assoc1 = 1;
    int hits1 = 0, total1 = 0;
    ArrayList<Integer> cache1 = new ArrayList<>(), valid1 = new ArrayList<>();
    for (int i = 0; i < cacheLines1; i++) {
      cache1.add(0);
      valid1.add(0);
    }

    // Cache #2: size=2048B, assoc=1, block=2
    int indexWidth2 = 8, tagWidth2 = 21, cacheLines2 = 256, blocks2 = 2, assoc2 = 1;
    int hits2 = 0, total2 = 0;
    ArrayList<Integer> cache2 = new ArrayList<>(), valid2 = new ArrayList<>();
    for (int i = 0; i < cacheLines2; i++) {
      cache2.add(0);
      valid2.add(0);
    }

    // Cache #3: size=2048B, assoc=1, block=4
    int indexWidth3 = 7, tagWidth3 = 21, cacheLines3 = 128, blocks3 = 4, assoc3 = 1;
    int hits3 = 0, total3 = 0;
    ArrayList<Integer> cache3 = new ArrayList<>(), valid3 = new ArrayList<>();
    for (int i = 0; i < cacheLines3; i++) {
      cache3.add(0);
      valid3.add(0);
    }

    // Cache #4: size=2048B, assoc=2, block=1
    int indexWidth4 = 8, tagWidth4 = 22, cacheLines4 = 256, blocks4 = 1, assoc4 = 2;
    int hits4 = 0, total4 = 0;
    ArrayList<Integer> valid4 = new ArrayList<>();
    ArrayList<ArrayList<Integer>> assocCache4 = new ArrayList<>();
    for (int i = 0; i < cacheLines4; i++) {
      valid4.add(0);
      assocCache4.add(new ArrayList<>());
    }

    // Cache #5: size=2048B, assoc=4, block=1
    int indexWidth5 = 7, tagWidth5 = 23, cacheLines5 = 128, blocks5 = 1, assoc5 = 4;
    int hits5 = 0, total5 = 0;
    ArrayList<Integer> valid5 = new ArrayList<>();
    ArrayList<ArrayList<Integer>> assocCache5 = new ArrayList<>();
    for (int i = 0; i < cacheLines5; i++) {
      valid5.add(0);
      assocCache5.add(new ArrayList<>());
    }

    // Cache #6: size=2048B, assoc=4, block=4
    int indexWidth6 = 5, tagWidth6 = 25, cacheLines6 = 32, blocks6 = 4, assoc6 = 4;
    int hits6 = 0, total6 = 0;
    ArrayList<Integer> valid6 = new ArrayList<>();
    ArrayList<ArrayList<Integer>> assocCache6 = new ArrayList<>();
    for (int i = 0; i < cacheLines6; i++) {
      valid6.add(0);
      assocCache6.add(new ArrayList<>());
    }

    // Cache #7: size=4096B, assoc=1, block=1
    int indexWidth7 = 10, tagWidth7 = 20, cacheLines7 = 1024, blocks7 = 1, assoc7 = 1;
    int hits7 = 0, total7 = 0;
    ArrayList<Integer> cache7 = new ArrayList<>(), valid7 = new ArrayList<>();
    for (int i = 0; i < cacheLines7; i++) {
      cache7.add(0);
      valid7.add(0);
    }

    // Single pass through file
    try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
      String line;
      while ((line = br.readLine()) != null) {
        int addr = getAddr(line);

        // Cache #1
        {
          int idx = getIndex(addr, indexWidth1, blocks1), tag = getTag(addr, tagWidth1);
          total1++;
          if (valid1.get(idx) == 1 && cache1.get(idx) == tag)
            hits1++;
          else {
            cache1.set(idx, tag);
            valid1.set(idx, 1);
          }
        }

        // Cache #2
        {
          int idx = getIndex(addr, indexWidth2, blocks2), tag = getTag(addr, tagWidth2);
          total2++;
          if (valid2.get(idx) == 1 && cache2.get(idx) == tag)
            hits2++;
          else {
            cache2.set(idx, tag);
            valid2.set(idx, 1);
          }
        }

        // Cache #3
        {
          int idx = getIndex(addr, indexWidth3, blocks3), tag = getTag(addr, tagWidth3);
          total3++;
          if (valid3.get(idx) == 1 && cache3.get(idx) == tag)
            hits3++;
          else {
            cache3.set(idx, tag);
            valid3.set(idx, 1);
          }
        }

        // Cache #4
        {
          int idx = getIndex(addr, indexWidth4, blocks4), tag = getTag(addr, tagWidth4);
          ArrayList<Integer> set = assocCache4.get(idx);
          total4++;
          if (valid4.get(idx) == 0) {
            valid4.set(idx, 1);
            set.add(tag);
          } else {
            boolean found = false;
            for (int i = 0; i < set.size(); i++) {
              if (set.get(i) == tag) {
                set.remove(i);
                set.add(tag);
                hits4++;
                found = true;
                break;
              }
            }
            if (!found)
              set.add(tag);
            if (set.size() > assoc4)
              set.remove(0);
          }
        }

        // Cache #5
        {
          int idx = getIndex(addr, indexWidth5, blocks5), tag = getTag(addr, tagWidth5);
          ArrayList<Integer> set = assocCache5.get(idx);
          total5++;
          if (valid5.get(idx) == 0) {
            valid5.set(idx, 1);
            set.add(tag);
          } else {
            boolean found = false;
            for (int i = 0; i < set.size(); i++) {
              if (set.get(i) == tag) {
                set.remove(i);
                set.add(tag);
                hits5++;
                found = true;
                break;
              }
            }
            if (!found)
              set.add(tag);
            if (set.size() > assoc5)
              set.remove(0);
          }
        }

        // Cache #6
        {
          int idx = getIndex(addr, indexWidth6, blocks6), tag = getTag(addr, tagWidth6);
          ArrayList<Integer> set = assocCache6.get(idx);
          total6++;
          if (valid6.get(idx) == 0) {
            valid6.set(idx, 1);
            set.add(tag);
          } else {
            boolean found = false;
            for (int i = 0; i < set.size(); i++) {
              if (set.get(i) == tag) {
                set.remove(i);
                set.add(tag);
                hits6++;
                found = true;
                break;
              }
            }
            if (!found)
              set.add(tag);
            if (set.size() > assoc6)
              set.remove(0);
          }
        }

        // Cache #7
        {
          int idx = getIndex(addr, indexWidth7, blocks7), tag = getTag(addr, tagWidth7);
          total7++;
          if (valid7.get(idx) == 1 && cache7.get(idx) == tag)
            hits7++;
          else {
            cache7.set(idx, tag);
            valid7.set(idx, 1);
          }
        }
      }
    }

    System.out.println("Cache #1");
    System.out.println(
        "Cache size: " + cacheLines1 * BYTESPERLINE + "B\tAssociativity: " + assoc1 + "\tBlock size: " + blocks1);
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits1, ((double) hits1 / total1) * 100);

    System.out.println("Cache #2");
    System.out.println("Cache size: " + blocks2 * cacheLines2 * BYTESPERLINE + "B\tAssociativity: " + assoc2
        + "\tBlock size: " + blocks2);
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits2, ((double) hits2 / total2) * 100);

    System.out.println("Cache #3");
    System.out.println("Cache size: " + blocks3 * cacheLines3 * BYTESPERLINE + "B\tAssociativity: " + assoc3
        + "\tBlock size: " + blocks3);
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits3, ((double) hits3 / total3) * 100);

    System.out.println("Cache #4");
    System.out.println("Cache size: " + blocks4 * assoc4 * cacheLines4 * BYTESPERLINE + "B\tAssociativity: " + assoc4
        + "\tBlock size: " + blocks4);
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits4, ((double) hits4 / total4) * 100);

    System.out.println("Cache #5");
    System.out.println("Cache size: " + blocks5 * assoc5 * cacheLines5 * BYTESPERLINE + "B\tAssociativity: " + assoc5
        + "\tBlock size: " + blocks5);
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits5, ((double) hits5 / total5) * 100);

    System.out.println("Cache #6");
    System.out.println("Cache size: " + blocks6 * assoc6 * cacheLines6 * BYTESPERLINE + "B\tAssociativity: " + assoc6
        + "\tBlock size: " + blocks6);
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits6, ((double) hits6 / total6) * 100);

    System.out.println("Cache #7");
    System.out.println("Cache size: " + blocks7 * cacheLines7 * BYTESPERLINE + "B\tAssociativity: " + assoc7
        + "\tBlock size: " + blocks7);
    System.out.printf("Hits: %d\tHit Rate: %.2f%%%n", hits7, ((double) hits7 / total7) * 100);
  }
}
