import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class lab6 {

    public static int getIndex(int addr, int indexWidth) {
        // right shift by 2 for byte offset
        // then bitmask
        return (addr >>> 2) & (int) (Math.pow(2, indexWidth) - 1);
    }

    public static int getTag(int addr, int tagWidth) {
        return addr >>> (32 - tagWidth);
    }

    public static int getAddr(String line) {
        String[] parsedline = line.split("\\s+");
        return Integer.parseUnsignedInt(parsedline[1], 16);
    }

    public static void main(String[] args) throws IOException {

        int hits = 0;
        int misses = 0;
        int total = 0;

        ArrayList<Integer> cache = new ArrayList<>();               // cache
        int indexWidth = 9;
        int tagWidth = 21;
        int cacheLines = 512;
        ArrayList<Integer> valid = new ArrayList<>();               // store valid bit
        for (int i = 0; i < cacheLines; i++) {                      // initialize to all 0's
            valid.add(0);
            cache.add(0);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = br.readLine())!=null) {
                int addr = getAddr(line);
                int idx = getIndex(addr, indexWidth);
                int tag = getTag(addr, tagWidth);
                if (valid.get(idx) ==1 && cache.get(idx) == tag) {
                    hits++;
                    total++;
                } else {
                    misses++;
                    total++;
                    cache.set(idx, tag);
                    valid.set(idx, 1);
                }
            }
        }




    }
}
