package assignment_4.Util;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IOUtil {
    private static IOUtil IO_UTIL;
    public static String PATH;

    private IOUtil() {
    }

    public static IOUtil getInstance(String path) {
        if (IO_UTIL == null) {
            IO_UTIL = new IOUtil();
        }
        PATH = path;
        return IO_UTIL;
    }

    public void matrixToFile(int[] binary, int number) {
        String path = PATH+"/"+number + "_" + UUID.randomUUID() + ".txt";
        try {
            PrintWriter pr = new PrintWriter(path);
            for (int i = 0; i < binary.length; i++) {
                pr.print(binary[i]);
            }
            pr.flush();
            pr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] fileToMatrix(File file) {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = br.readLine();
            for (char c : str.toCharArray()) {
                list.add(Integer.parseInt(c + ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * Convert Integer list to int array.
         */
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    public ArrayList<ArrayList<int[]>> listFilesInFolder(String path) {
        ArrayList<ArrayList<int[]>> trainingSet = new ArrayList<>();

        File folder = new File(path);
        File[] files = folder.listFiles((dir, name) -> {
            if (name.endsWith(".txt")) {
                if (name.charAt(0) >= '0' && name.charAt(0) <= '9') {
                    return true;
                }
            }
            return false;
        });

        for (File file : files){
            int type = Integer.parseInt(file.getName().charAt(0)+"");
            trainingSet.get(type).add(this.fileToMatrix(file));
        }

        return trainingSet;

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
