package assignment_4.Model;

public class KNNInfo implements Comparable<KNNInfo> {

    private double distance;
    private final int type;
    private int index;

    public KNNInfo(int type,double distance, int index){
        this.type = type;
        this.distance = distance;
        this.index = index;
    }

    public int getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public double getDistance() {
        return distance;
    }

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
