package assignment_3.question_1;

import java.util.LinkedList;
import java.util.Random;

public class MinGapTest {
    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();
        MinGapTree mgt = new MinGapTree();

        Random rd = new Random();
        LinkedList<Integer> ints = new LinkedList<>();

        for (int i = 0; i < 100000; i++) {
            ints.add(rd.nextInt(1000000));
        }

        /**
         * To compare insertion time.
         */
        long startTime = System.currentTimeMillis();
        for (int i : ints){
            rbt.add(i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("RB Tree insertion time cost: "+(endTime-startTime)+" ms");

        startTime = System.currentTimeMillis();
        for (int i : ints){
            mgt.add(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("MG Tree insertion time cost: "+(endTime-startTime)+" ms");

        System.out.println();
        /**
         * To find the minGap
         */
        int mingap1;
        int mingap2;
        startTime = System.currentTimeMillis();
        mingap1 = mgt.getMinGap();
        endTime = System.currentTimeMillis();
        System.out.println("Augmented DS Methods time costs: "+ (endTime-startTime)+" ms");

        startTime = System.currentTimeMillis();
        mingap2 = mgt.bruteForceGetMinGap();
        endTime = System.currentTimeMillis();
        System.out.println("Brute Force Methods time costs: "+ (endTime-startTime)+" ms");

        System.out.println();

        if(mingap1 != mingap2){
            System.out.println("WRONG");
        } else {
            System.out.println("MINGAP = "+mingap1);
        }

    }
}
