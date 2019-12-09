package assignment_3.question_1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class MinGapTree extends RedBlackTree {

    public int minGap(){
        return ((MinGapNode) this.root).minGap;
    }

    @Override
    public boolean add(int val) {

        MinGapNode childNode = new MinGapNode(val);

        Node parentNode = this.addCheck(childNode);
        if (parentNode == null)
            return false;

        this.add(childNode, parentNode);

        /**
         * Update minGap information of childNode and pass it upon.
         */
        this.updateMinGap(childNode);

        this.balance(childNode);

        return true;
    }

    private Node updateMinGap(MinGapNode node) {
        /**
         * If haven't exceeded root upon.
         */
        if (node != null) {
            /**
             * To calculate the minGap,
             * if false, finish update.
             */
            this.minGap(node);
            return updateMinGap((MinGapNode) node.parent);
        }

        return null;
    }

    private void minGap(MinGapNode node){
        int minGap = node.minGap;
        int min = node.min;
        int max = node.max;

        MinGapNode sub;
        int newGap;

        if (node.leftChild != null) {
            sub = (MinGapNode) node.leftChild;

            /**
             * To update the minimum value if exists.
             */
            if (min > sub.min) {
                min = sub.min;
            }

            /**
             * if the gap between subMax and node is smaller then the one of subNode and node,
             * then update the value by a subtraction.
             */
            newGap = node.val - sub.max;
            if (newGap < sub.minGap && newGap < minGap) {
                minGap = newGap;

            } else if (sub.minGap < minGap) {
                /**
                 * If newGap is larger, then check the minGap in subNode.
                 */
                minGap = sub.minGap;

            }
        }

        if (node.rightChild != null) {
            sub = (MinGapNode) node.rightChild;

            /**
             * To update the maximum value if exists.
             */
            if (max < sub.max) {
                max = sub.max;
            }

            newGap = sub.min - node.val;
            if (newGap < sub.minGap && newGap < minGap) {
                minGap = newGap;

            } else if (sub.minGap < minGap) {
                minGap = sub.minGap;

            }
        }

        /**
         * Update attributes of Node, if unchanged, stop recursion.
         */
        node.updateAttr(minGap, min, max);
    }

    @Override
    protected void rightRotate(Node node) {
        super.rightRotate(node);
        fixRotatedGap((MinGapNode) node);
    }

    @Override
    protected void leftRotate(Node node) {
        super.leftRotate(node);
        fixRotatedGap((MinGapNode) node);
    }

    private void fixRotatedGap(MinGapNode node) {
        int i = 0;
        while (i < 2 && node != null) {
            node.resetMinGap();
            this.minGap(node);
            node = (MinGapNode) node.parent;
            i++;
        }
    }

    public int bruteForceGetMinGap(){
        ArrayList<Integer> list = new ArrayList<>();
        this.inOrderTraversal(this.root, list);
        int minGap = list.get(1) - list.get(0);
        for(int i=2;i<list.size();i++){
            if(list.get(i)-list.get(i-1)<minGap){
                minGap = list.get(i) - list.get(i-1);
            }
        }
        return minGap;
    }
    private ArrayList<Integer> inOrderTraversal(Node node, ArrayList<Integer> list){
        if(node == null){
            return null;
        } else {
            inOrderTraversal(node.leftChild, list);
            list.add(node.getVal());
            inOrderTraversal(node.rightChild, list);
        }
        return list;
    }

    class MinGapNode extends Node {
        private static final int INF = 999999999;
        /**
         * min : the minimum value in subtree rooted as Node;
         * max : Vise Versa;
         * minGap : the minGap among nodes in the subtree rooted as Node;
         * between : the two nodes with minimum gap. Only used when check which two share the minGap.
         */
        protected int minGap;

        protected int min;
        protected int max;

        public MinGapNode(int val, Node parent) {
            super(val, parent);
            initMinGapNodeAttr();
        }

        public MinGapNode(int val, Node parent, Node leftChild, Node rightChild) {
            super(val, parent, leftChild, rightChild);
            initMinGapNodeAttr();
        }

        public MinGapNode(int val) {
            super(val);
            initMinGapNodeAttr();
        }


        private void initMinGapNodeAttr() {
            this.minGap = INF;
            this.min = this.val;
            this.max = this.val;

        }

        private void resetMinGap(){
            this.minGap = INF;
            this.min = this.val;
            this.max = this.val;
        }

        /**
         * @param minGap
         * @param min
         * @param max
         * @return if all the attributes are unchanged, then return false.
         */
        public boolean updateAttr(int minGap, int min, int max) {
            this.minGap = minGap;
            this.min = min;
            this.max = max;

            return true;
        }

    }

    public static void main(String[] args) {
        int NUM = 100;
        Random rd = new Random();

        for(int r =0; r<100;r++){
            MinGapTree minGapTree = new MinGapTree();
            LinkedList<Integer> ints = new LinkedList<>();
            System.out.println("\n### ROUND "+r);
            for (int i = 0; i < NUM; i++) {
                ints.add(rd.nextInt(10000));
            }
            for (int i : ints){
//                System.out.println();
//                System.out.println("ADD: "+ i);
                minGapTree.add(i);
//                minGapTree.inOrderTraversal();
//                System.out.println(minGapTree.getMinGap());
            }
            minGapTree.inOrderTraversal();

            System.out.println("MINGAP = "+minGapTree.minGap());
            if(minGapTree.minGap() > minGapTree.bruteForceGetMinGap()){
                System.out.println("WRONG ANSWER ");
                break;
            }
        }


    }
}
