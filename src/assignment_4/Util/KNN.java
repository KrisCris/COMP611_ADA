package assignment_4.Util;

public class KNN {

    public double distance(int[] input, int[] sample) {
        int e = 0;
        for (int i = 0; i < input.length; i++) {
            e += (input[i] - sample[i]) * (input[i] - sample[i]);
        }
        return Math.sqrt(e);
    }

}
