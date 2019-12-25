package assignment_4.Model;

public class KNNInfo implements Comparable<KNNInfo> {

    private double distance;
    private final int type;

    public KNNInfo(int type,double distance){
        this.type = type;
        this.distance = distance;
    }

    public int getType() {
        return type;
    }

    @Override
    public int compareTo(KNNInfo other) {
        int flag = 0;
        if (this.distance < other.distance) {
            flag = 1;
        }

        if (this.distance == other.distance) {
            flag = 0;
        }

        if (this.distance > other.distance) {
            flag = -1;
        }
        return flag;
    }
}
