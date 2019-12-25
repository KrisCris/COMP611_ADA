package assignment_4.Model;

public class KNNInfo implements Comparable<KNNInfo> {

    private double distance;
    private String type;
    private int[] value;

    

    @Override
    public int compareTo(KNNInfo other) {
        int flag = 0;
        if (this.distance < other.distance) {
            flag = -1;
        }

        if (this.distance == other.distance) {
            flag = 0;
        }

        if (this.distance > other.distance) {
            flag = 1;
        }
        return flag;
    }
}
