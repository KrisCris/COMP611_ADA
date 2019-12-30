package assignment_4.Util;

import assignment_4.Model.Outline;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class IOUtil {
    private static IOUtil IO_UTIL;
    public static final String PATH = "src/assignment_4/TrainingSet";

    private IOUtil() {
    }

    /**
     * Singleton pattern implemented.
     * This class cannot be cloned.
     *
     * @return
     */
    public static IOUtil getInstance() {
        if (IO_UTIL == null) {
            IO_UTIL = new IOUtil();
        }
        return IO_UTIL;
    }

    /**
     * This method is what we currently used to fetch training samples.
     *
     * @return
     */
    public ArrayList<ArrayList<int[]>> getTrainingSet() {
        ArrayList<ArrayList<int[]>> trainingSet = new ArrayList<>();
        /**
         * init list
         */
        for (int i = 0; i < 10; i++) {
            trainingSet.add(new ArrayList<>());
        }

        /**
         * read files;
         */
        File[] samples = getFilesInFolder();
        for (int i = 0; i < samples.length; i++) {
            /**
             * in case the files in file[] are not sorted as 0 to 9;
             */
            int index = Integer.parseInt(samples[i].getName().charAt(0) + "");
            trainingSet.set(index, getClassOfDigit(samples[i]));
        }
        return trainingSet;
    }

    /**
     * This method reads files in a specific folder.
     *
     * @return
     */
    public File[] getFilesInFolder() {
        File folder = new File(PATH);
        File[] files = folder.listFiles((dir, name) -> {
            if (name.endsWith(".txt")) {
                if (name.charAt(0) >= '0' && name.charAt(0) <= '9') {
                    return true;
                }
            }
            return false;
        });

        return files;
    }

    /**
     * This method converts a file containing multiple lines of 1 * n matrices into a list of int[].
     *
     * @param file
     * @return
     */
    public ArrayList<int[]> getClassOfDigit(File file) {
        ArrayList<int[]> group = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            /**
             * read a line of matrix,
             * str == null indicates the EOF.
             */
            while (true) {

                String str = br.readLine();
                if (str == null) {
                    break;
                }

                /**
                 * put binary pixels into a int[] pixel by pixel converted from char.
                 */
                int[] number = new int[str.length()];
                for (int i = 0; i < str.length(); i++) {
                    number[i] = Integer.parseInt(str.charAt(i) + "");
                }
                group.add(number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return group;
    }

    /**
     * Add a new sample of digit to the training set.
     *
     * @param number
     * @param matrix
     */
    public void appendNewSample(int number, int[] matrix) {
        File f = new File(PATH + "/" + number + ".txt");
        try {
            PrintWriter pw = new PrintWriter(f);
            pw.println(matrix);
            pw.flush();
            pw.close();
        } catch (Exception e) {

        }
    }


    /**
     * Following methods were used in training set converting periods.
     */

    /**
     * Save a int[] matrix containing handwriting digit to a txt file with number labeled.
     *
     * @param binary
     * @param number
     */
    public void matrixToFile(int[] binary, int number) {
        String path = PATH + "/" + number + "_" + UUID.randomUUID() + ".txt";
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

    /**
     * Legacy method.
     * This method convert a text file with a SINGLE matrix to a int[].
     *
     * @param file
     * @return
     */
    public int[] fileToMatrix(File file) {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = br.readLine();
            for (char c : str.toCharArray()) {
                try {
                    list.add(Integer.parseInt(c + ""));
                } catch (NumberFormatException e) {
                    System.out.println(file.getName());
                }

            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * Convert Integer list to int array.
         */
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * This method read all files in the folder and pass them to fileToMatrix();
     * However, this methods was used in the matrix-per-file periods.
     * This methods cannot be used since we put a class of training sample in one file now.
     *
     * @param path
     * @return
     */
    public ArrayList<ArrayList<int[]>> matricesInFolder(String path) {
        ArrayList<ArrayList<int[]>> trainingSet = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            trainingSet.add(new ArrayList<>());
        }

        File folder = new File(path);
        File[] files = folder.listFiles((dir, name) -> {
            if (name.endsWith(".txt")) {
                if (name.charAt(0) >= '0' && name.charAt(0) <= '9') {
                    return true;
                }
            }
            return false;
        });

        for (File file : files) {
            int type = Integer.parseInt(file.getName().charAt(0) + "");
            trainingSet.get(type).add(this.fileToMatrix(file));
        }

        return trainingSet;

    }

    /**
     * Converting a image sample to a txt of matrix.
     */
    public void JPGToTXT(String path) {
        File folder = new File(path);
        GraphicsUtil GU = GraphicsUtil.getInstance();

        File[] files = folder.listFiles((dir, name) -> {
            if (name.endsWith(".jpg")) {
                if (name.charAt(0) >= '0' && name.charAt(0) <= '9') {
                    return true;
                }
            }
            return false;
        });
        List<File> list = Arrays.asList(files);
        Iterator<File> fileIterator = list.iterator();

        while (fileIterator.hasNext()) {
            File f = fileIterator.next();
            try {
                BufferedImage img = ImageIO.read(f);
                double[] gray = GU.imageToGrayMatrix(img);
                int[] binary = GU.garyToBinaryMatrix(gray);
                Outline outline = GU.getOutline(binary);

                BufferedImage figureImage = GU.compressImage(
                        GU.getFigureImage(img, outline),
                        20,
                        20
                );
                binary = GU.garyToBinaryMatrix(GU.imageToGrayMatrix(figureImage));

                matrixToFile(binary, Integer.parseInt(f.getName().charAt(0) + ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * As described in some other methods,
     * this convert 35000 training sample txt files to 10 grouped files.
     */
    public void groupText() {
        ArrayList<ArrayList<int[]>> list = this.matricesInFolder(PATH);
        for (ArrayList<int[]> numbers : list) {
            int number = list.indexOf(numbers);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("src/assignment_4/a/" + number + ".txt"));
                for (int[] num : numbers) {
                    for (int b : num) {
                        bw.write(b + "");
                    }
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
