package assignment_4.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

public class IOUtil {
    private static IOUtil IO_UTIL;

    private IOUtil() {
    }

    public static IOUtil getInstance() {
        if (IO_UTIL == null) {
            IO_UTIL = new IOUtil();
        }
        return IO_UTIL;
    }

    public void matrixToFile(int[] binary, int number) {
        try {
            PrintWriter pr = new PrintWriter(number + "_" + UUID.randomUUID() + ".txt");
            for (int i = 0; i < binary.length; i++) {
                pr.print(binary[i]);
            }
            pr.flush();
            pr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] fileToMatrix(String path) {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String str = br.readLine();
            for (char c : str.toCharArray()){
                list.add(Integer.parseInt(c+""));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * Convert Integer list to int array.
         */
        return list.stream().mapToInt(Integer::intValue).toArray();
    }
}
