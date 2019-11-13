package assignment_3;

public class RedBlackTree {
    private Node root;

    public RedBlackTree(int root){
        this.root = new Node(root);
    }

    public boolean add(int val){
        //The ultimate node found based on the val to be inserted.
        Node node = this.contains(this.root,val);
        //If exist
        if(node.equals(val)){
            return false;
        }
        //Insert Node
        Node child = new Node(val,node);
        if(val<node.getVal()){
            node.setLeftChild(child);
        } else {
            node.setRightChild(child);
        }
        //Balance
        this.balance(child);

        //
        return true;
    }

    public boolean remove(){

        return true;
    }

    public boolean contains(int val){
        if(this.contains(root, val).equals(val)){
            return true;
        }
        return false;
    }

    private Node contains(Node node, int val){
        if(node.isLeaf() || node.equals(val)){
            return node;
        } else if(val<node.getVal()){
            //problems occurred
            if(node.getLeftChild() == null)
                return node;
            return contains(node.leftChild, val);
        } else {
            if(node.getRightChild() == null)
                return node;
            return contains(node.rightChild, val);
        }
    }

    private void balance(Node node){
        //init
        Node current = node;
        Node parent = current.getParent();
        Node uncle = null;

        while(parent != null && parent.isRed()){
            uncle = parent.getSibling();
            if(parent.isLeftChild()){
                //case.1
                if(uncle != null && uncle.isRed()){
                    parent.setRed(false);
                    uncle.setRed(false);
                    parent.getParent().setRed(true);
                    //moving 2 levels up
                    current = parent.getParent();
                } else {
                    //case.2
                    //Left-Right situation, to rotate current and its parent to Left-Left position first.
                    if(!current.isLeftChild()){
                        this.leftRotate(parent);
                    }
                    //Left-Left situation, to right-rotate parent and grandpa thus made grandpa to be the right child of parent.
                    parent.setRed(false);
                    parent.getParent().setRed(true);
                    this.rightRotate(parent.getParent());

                    current = parent;
                }
                parent = current.getParent();
            } else {
                //Vise versa:
                if(uncle != null && uncle.isRed()){
                    parent.setRed(false);
                    uncle.setRed(false);
                    parent.getParent().setRed(true);
                } else {
                    if(current.isLeftChild()){
                        this.rightRotate(parent);
                    }
                    parent.setRed(false);
                    parent.getParent().setRed(true);
                    this.leftRotate(parent.getParent());
                }
            }
        }
        this.root.setRed(false);
    }

    public void inOrderTraversal(){
        this.inOrderTraversal(this.root);
    }

    private void inOrderTraversal(Node node){
        if(node == null){
            return;
        } else {
            inOrderTraversal(node.leftChild);
            System.out.print(node.getVal()+"\t");
            inOrderTraversal(node.rightChild);
        }
    }

    /**
     * Rotate the node and its child.
     * @param node
     */
    private Node rightRotate(Node node){
        Node left = node.getLeftChild();
        Node parent = node.getParent();
        if(node.isLeftChild()){
            parent.setLeftChild(left);
        } else {
            parent.setRightChild(left);
        }
        left.setParent(parent);
        left.setRightChild(node);
        node.setParent(left);
        node.setLeftChild(left.getRightChild());
        left.getRightChild().setParent(node);
        return left;
    }

    private Node leftRotate(Node node){
        Node right = node.getRightChild();
        Node parent = node.getParent();
        if(node.isLeftChild()){
            parent.setRightChild(right);
        } else {
            parent.setLeftChild(right);
        }
        right.setParent(parent);
        right.setLeftChild(node);
        node.setParent(right);
        node.setRightChild(right.getLeftChild());
        right.getLeftChild().setParent(node);
        return right;
    }


    class Node{
        private int val;
        private Node leftChild;
        private Node rightChild;
        private Node parent;
        private boolean isRed;
//        private int minGap;

        public Node(int val, Node parent){
            this.val = val;
            this.parent = parent;
            this.leftChild = null;
            this.rightChild = null;
            this.isRed = true;
        }

        public Node(int val, Node parent, Node leftChild, Node rightChild){
            this.val = val;
            this.parent = parent;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.isRed = true;
        }

        public Node (int val){
            this.val = val;
            this.parent = null;
            this.leftChild = null;
            this.rightChild = null;
            this.isRed = false;
        }

        public boolean equals(int val){
            if(val == this.val) return true;
            return false;
        }

        public boolean isLeaf(){
            if(this.leftChild == null && this.rightChild == null)
                return true;
            return false;
        }

        public boolean isLeftChild(){
            if(this.parent != null){
                if(this.parent.getLeftChild() == this){
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        public Node getSibling(){
            if(this.parent != null){
                if(this.isLeftChild()){
                    return this.parent.rightChild;
                } else {
                    return this.parent.leftChild;
                }
            } else {
                return null;
            }
        }

//        public int getMinGap() {
//            return minGap;
//        }
//
//        public void setMinGap(int minGap) {
//            this.minGap = minGap;
//        }

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
}
