package assignment_4.Util;

import assignment_4.Model.KNNInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

public class KNN {

    private static KNN knnCore;
    public static ArrayList<ArrayList<int[]>> DefaultTrainingSet;

    private KNN() {
        IOUtil IOU = IOUtil.getInstance();
        if (DefaultTrainingSet == null) {
            DefaultTrainingSet = IOU.getTrainingSet();
        }
    }

    public static KNN getKnnCore() {
        if (knnCore == null) {
            knnCore = new KNN();
        }
        return knnCore;
    }

    public double euclideanDistance(int[] input, int[] sample) {
        int e = 0;
        for (int i = 0; i < input.length; i++) {
            e += (input[i] - sample[i]) * (input[i] - sample[i]);
        }
        return Math.sqrt(e);
    }


    public int getResult(int[] target, int k) {
        return getResult(DefaultTrainingSet, target, k);
    }

    private int getResult(ArrayList<ArrayList<int[]>> trainingSet, int[] target, int k) {
        /**
         * KNN steps
         */
        ArrayList<KNNInfo> knnSet = knn(trainingSet, target, k);

//        System.out.println("#######");
//        System.out.println("#1 = " + knnSet.get(0).getType());

        double[] distribution = getKDistribution(knnSet);

//        for (int i = 0; i < distribution.length; i++) {
//            System.out.println(i + "\t" + distribution[i]);
//        }
//        for (int i = 0;i< knnSet.size();i++){
//            System.out.println("#"+i+"\t"+knnSet.get(i).getType());
//        }

        int major = getMajorClass(distribution);




//        for (int i = 0; i < knnSet.size(); i++) {
//            KNNInfo knnInfo = knnSet.get(i);
//            int type = knnInfo.getType();
//            int index = knnInfo.getIndex();
//
//            ArrayList<KNNInfo> set = knn(trainingSet, DefaultTrainingSet.get(type).get(index), 10);
//            int[] DSTB = getKDistribution(set);
//            int MJ = getMajorClass(DSTB);
//            if (MJ != knnInfo.getType()) {
//                knnSet.remove(knnInfo);
//            }
//        }
//
//        System.out.println("#2 = " + knnSet.get(0).getType());
//        for (int i = 0; i < distribution.length; i++) {
//            System.out.println(i + "\t" + distribution[i]);
//        }
//        distribution = getKDistribution(knnSet);
//        major = getMajorClass(distribution);
        return major;

    }

    /**
     * This method returns a votePool of K nearest neighbors,
     * instead of the actual result for further comparison.
     *
     * @param trainingSet
     * @param target
     * @param k
     * @return
     */
    public ArrayList<KNNInfo> knn(ArrayList<ArrayList<int[]>> trainingSet, int[] target, int k) {
        PriorityQueue<KNNInfo> pq = new PriorityQueue<>();

        for (int i = 0; i < trainingSet.size(); i++) {
            for (int[] sample : trainingSet.get(i)) {
                Double dist = this.euclideanDistance(target, sample);
                pq.add(new KNNInfo(i, dist, trainingSet.get(i).indexOf(sample)));
            }
        }

        ArrayList<KNNInfo> knnSet = new ArrayList<>();
        for (int i = 0; i < k && !pq.isEmpty(); i++) {
            knnSet.add(pq.poll());
        }

        return knnSet;

    }


    private double[] getKDistribution(ArrayList<KNNInfo> knnSet) {
        double avg = 0;
        for (KNNInfo info : knnSet){
            avg += info.getDistance();
        }
        avg = avg / knnSet.size();

        double[] votePool = new double[10];
        Iterator<KNNInfo> it = knnSet.iterator();
        while (it.hasNext()) {
            KNNInfo info = it.next();
            votePool[info.getType()] += Math.pow(avg/(info.getDistance()),1);
        }

        return votePool;
    }

    public int getMajorClass(double[] distribution) {
        int mostVoted = 0;
        for (int i = 0; i < distribution.length; i++) {
            if (distribution[i] > distribution[mostVoted]) {
                mostVoted = i;
            }
        }
        return mostVoted;
    }

    public double accuracy() {

        ArrayList<ArrayList<int[]>> sample = new ArrayList<>();
        ArrayList<ArrayList<int[]>> test = new ArrayList<>();
        int index = 0;
        for (ArrayList<int[]> a : DefaultTrainingSet) {
            sample.add(new ArrayList<>());
            test.add(new ArrayList<>());
            for (int i = 0; i < a.size(); i++) {
                if (i % 2 == 0) {
                    test.get(index).add(a.get(i));
                } else {
                    sample.get(index).add(a.get(i));
                }
            }
            index++;
        }

        for (int k = 4; k < 5; k++) {
            int[] count = new int[10];
            index = 0;
            for (ArrayList<int[]> list : test) {
                for (int[] num : list) {
                    int result = this.getResult(sample, num, k);
                    if (result == index) {
                        count[index]++;
                    }
                }
                index++;
            }
            System.out.println("================\tk = " + k);
            for (int i = 0; i < count.length; i++) {
                System.out.println(i + "   :   " + ((count[i] + 1.0 - 1.0) / test.get(i).size()) * 100 + " %");
            }
        }

        return 0;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static void main(String[] args) {
        KNN knn = KNN.getKnnCore();
        knn.accuracy();
    }
}
