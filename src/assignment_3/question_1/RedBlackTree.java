package assignment_3.question_1;

import java.util.LinkedList;
import java.util.Random;

public class RedBlackTree {
    protected Node root;

    public RedBlackTree() {
        this.root = null;
    }

    public RedBlackTree(int root) {
        this.root = new Node(root);
    }

    public RedBlackTree(Node root) {
        this.root = root;
    }

    public boolean add(int val) {
        /**
         * To check and deal with when the tree is empty, or the Node existed.
         */
        Node childNode = new Node(val);
        Node parentNode = this.addCheck(childNode);
        if (parentNode == null)
            return false;

        /**
         * To add a new node descend from parentNode.
         */

        this.add(childNode,parentNode);

        /**
         * To balance the tree.
         */
        this.balance(childNode);

        return true;
    }

    protected Node add(Node childNode, Node parentNode){
        childNode.setParent(parentNode);
        if (childNode.getVal() < parentNode.getVal()) {
            parentNode.setLeftChild(childNode);
        } else {
            parentNode.setRightChild(childNode);
        }

        return childNode;
    }

    protected Node addCheck(Node node){
        /**
         * In case if this tree is empty.
         */
        if (this.root == null) {
            this.root = node;
            this.root.setRed(false);
            return null;
        }

        /**
         * To get the parent node or the existed node with same value.
         * If node existed, then RETURN FALSE.
         */
        Node parentNode = this.contains(this.root, node.val);
        if (parentNode.equals(node.val)) {
//            System.out.println("---DUP---");
            return null;
        }

        return parentNode;
    }

    public Node remove() {
        //TODO Remove a node
        return new Node(0);
    }

    /**
     * Remove all the node from this tree.
     * This method roughly sets the root node to null.
     *
     * @return The previous root.
     */
    public Node removeAll() {
        Node temp = this.root;
        this.root = null;
        return temp;
    }

    /**
     *
     * @param val
     * @return If the tree contains val, return true;
     */
    public boolean contains(int val) {
        if (this.contains(root, val).equals(val)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param node
     * @param val
     * @return If the tree contains
     */
    protected Node contains(Node node, int val) {
        if (node.equals(val)) {
            return node;
        }

        if (node.val < val) {
            if (node.rightChild != null) {
                return contains(node.rightChild, val);
            } else {
                return node;
            }
        } else {
            if (node.leftChild != null) {
                return contains(node.leftChild, val);
            } else {
                return node;
            }
        }
    }

    protected void balance(Node node) {
        Node uncle = null;
        while (node.getParent() != null && node.getParent().isRed()) {
            /**
             * Uncle can be set before if() thanks to the getSibling() method.
             */
            uncle = node.getParent().getSibling();

            if (node.getParent().isLeftChild()) {
                /**
                 * When node.parent is a left child:
                 */
//                uncle = node.getParent().getRightChild();
                if (uncle != null && uncle.isRed()){
                    /**
                     * Case.1 when only need to change color:
                     */
                    node.getParent().setRed(false);
                    uncle.setRed(false);
                    node.getParent().getParent().setRed(true);
                    node = node.getParent().getParent();
                } else {
                    /**
                     * When rotation required:
                     */
                    if (!node.isLeftChild()){
                        /**
                         * Case.2 when node is a right child
                         */
                        node = node.getParent();
                        leftRotate(node);
                    }
                    /**
                     * Case.3
                     */
                    node.getParent().setRed(false);
                    node.getParent().getParent().setRed(true);
                    rightRotate(node.getParent().getParent());
                }
            } else {

                /**
                 * When node.parent is a left child:
                 * Same as above but interchange left and right
                 */
//                uncle = node.getParent().getLeftChild();
                if (uncle != null && uncle.isRed()){
                    node.getParent().setRed(false);
                    uncle.setRed(false);
                    node.getParent().getParent().setRed(true);
                    node = node.getParent().getParent();
                } else {
                    if (node.isLeftChild()){
                        node = node.getParent();
                        rightRotate(node);
                    }
                    node.getParent().setRed(false);
                    node.getParent().getParent().setRed(true);
                    leftRotate(node.getParent().getParent());
                }
            }
        }
        this.root.setRed(false);
    }

    public void inOrderTraversal() {
        this.inOrderTraversal(this.root);
        System.out.println("");
    }

    /**
     * In order traversal and print each node with its color.
     * @param node
     */
    private void inOrderTraversal(Node node) {
        if (node == null) {
            return;
        } else {
            inOrderTraversal(node.leftChild);
            String ANSI_RESET = "\u001B[0m";
            String ANSI_RED = "\u001B[31m";
            if (node.isRed) System.out.print(ANSI_RED + node.getVal() + ANSI_RESET + "  ");
            else System.out.print(node.getVal() + "  ");

            inOrderTraversal(node.rightChild);
        }
    }

    /**
     * Right rotate the node and its child.
     *
     * @param node
     */
    protected void rightRotate(Node node) {
//        System.out.println("rr called");
        Node subNode = node.leftChild;
        node.setLeftChild(subNode.rightChild);
        if (subNode.rightChild != null) {
            subNode.rightChild.parent = node;
        }

        swap(node, subNode);

        subNode.rightChild = node;
        node.parent = subNode;
    }

    /**
     * Left rotate the node and its child.
     *
     * @param node
     */
    protected void leftRotate(Node node) {
//        System.out.println("lr called");
        Node subNode = node.rightChild;
        node.setRightChild(subNode.leftChild);
        if (subNode.leftChild != null) {
            subNode.leftChild.parent = node;
        }

        swap(node, subNode);

        subNode.leftChild = node;
        node.parent = subNode;
    }

    /**
     * To swap the relationship between node and the subNode.
     *
     * @param node
     * @param subNode
     */
    private void swap(Node node, Node subNode) {
        subNode.parent = node.parent;
        if (node.parent == null) {
            this.root = subNode;
        } else if (node == node.parent.leftChild) {
            node.parent.leftChild = subNode;
        } else {
            node.parent.rightChild = subNode;
        }
    }

    class Node {
        protected int val;
        protected Node leftChild;
        protected Node rightChild;
        protected Node parent;
        protected boolean isRed;

        public Node(int val, Node parent) {
            this.val = val;
            this.parent = parent;
            this.leftChild = null;
            this.rightChild = null;
            this.isRed = true;
        }

        public Node(int val, Node parent, Node leftChild, Node rightChild) {
            this.val = val;
            this.parent = parent;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.isRed = true;
        }

        public Node(int val) {
            this.val = val;
            this.parent = null;
            this.leftChild = null;
            this.rightChild = null;
            this.isRed = true;
        }

        public boolean equals(int val) {
            if (val == this.val) return true;
            return false;
        }

        /**
         * To check if this node is a leaf node.
         * @return
         */
        public boolean isLeaf() {
            if (this.leftChild == null && this.rightChild == null)
                return true;
            return false;
        }

        /**
         * To check if this node is a left child of its parent.
         * @return is left child return TRUE, is right child return FALSE.
         */
        public boolean isLeftChild() {
            if (this.parent != null) {
                if (this.parent.getLeftChild() == this) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /**
         *
         * @return To return the sibling of current node.
         */
        public Node getSibling() {
            if (this.parent != null) {
                if (this.isLeftChild()) {
                    return this.parent.rightChild;
                } else {
                    return this.parent.leftChild;
                }
            } else {
                return null;
            }
        }

        @Override
        public boolean equals(Object obj) {
            return this.val == ((Node) obj).getVal();
        }

        @Override
        public int hashCode() {
            return this.val;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public Node getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public Node getRightChild() {
            return rightChild;
        }

        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public boolean isRed() {
            return isRed;
        }

        public void setRed(boolean red) {
            isRed = red;
        }


    }

    /**
     * This main methods is for debug use,
     * please use the Question1_Test class to test the assignment 3.1 requirements.
     * @param args
     */
    public static void main(String[] args) {
        RedBlackTree redBlackTree = new RedBlackTree();
        int NUM = 10;
        Random rd = new Random();
        LinkedList<Integer> ints = new LinkedList<>();
        for (int i = 0; i < NUM; i++) {
            ints.add(rd.nextInt(100));
        }
        for (int i : ints){
            System.out.println();
            System.out.println("ADD: "+ i);
            redBlackTree.add(i);
            redBlackTree.inOrderTraversal();
        }
        redBlackTree.inOrderTraversal();
    }
}
