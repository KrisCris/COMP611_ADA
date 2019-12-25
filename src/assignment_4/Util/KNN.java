package assignment_4.Util;

import assignment_4.Model.KNNInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class KNN {

    public KNN() {
    }

    public double euclideanDistance(int[] input, int[] sample) {
        int e = 0;
        for (int i = 0; i < input.length; i++) {
            e += (input[i] - sample[i]) * (input[i] - sample[i]);
        }
        return Math.sqrt(e);
    }

    public int knn(HashMap<Integer, int[]> trainingSet, int[] target, int k) {
        PriorityQueue<KNNInfo> pq = new PriorityQueue<>();

        for (Map.Entry<Integer, int[]> ts : trainingSet.entrySet()) {
            Double dist = this.euclideanDistance(ts.getValue(), target);
            pq.add(new KNNInfo(ts.getKey(), dist));
        }

        return this.majorityType(pq, k);
    }

    private int majorityType(PriorityQueue<KNNInfo> resultQ, int k) {
        int[] votePool = new int[10];
        while (!resultQ.isEmpty() && k > 0) {
            votePool[resultQ.poll().getType()]++;
        }

        int mostVoted = 0;
        for (int i = 0; i < votePool.length; i++) {
            if (votePool[i] > mostVoted) {
                mostVoted = i;
            }
        }

        return mostVoted;
    }

    public double accuracy(){
        //TODO cal the acc
        return 0;
    }


}
