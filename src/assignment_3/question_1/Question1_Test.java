package assignment_3.question_1;

import java.util.LinkedList;
import java.util.Random;

public class Question1_Test {
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
        for (int i : ints) {
            rbt.add(i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("RB Tree insertion time cost: " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        for (int i : ints) {
            mgt.add(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("MG Tree insertion time cost: " + (endTime - startTime) + " ms");

        System.out.println();

        /**
         * To find the minGap
         */
        int mingap1;
        int mingap2;

        /**
         * Using minGap() method to find minGap.
         * To display the time cost.
         */
        startTime = System.currentTimeMillis();
        mingap1 = mgt.minGap();
        endTime = System.currentTimeMillis();
        System.out.println("Augmented DS Methods time costs: " + (endTime - startTime) + " ms");

        /**
         * A brute force way to find the minGap.
         */
        startTime = System.currentTimeMillis();
        mingap2 = mgt.bruteForceGetMinGap();
        endTime = System.currentTimeMillis();
        System.out.println("Brute Force Methods time costs: " + (endTime - startTime) + " ms");

        System.out.println();

        /**
         * Display minGap
         *
         * If the answer goes wrong then print "WRONG".
         * But actually there is no way to go wrong. LOL
         */
        if (mingap1 != mingap2) {
            System.out.println("WRONG");
        } else {
            System.out.println("minGap = " + mingap1);
        }

    }
}
