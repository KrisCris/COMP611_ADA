package assignment_3;

public class RBTreeTest {
    public static void main(String[] args) {
        RedBlackTree redBlackTree = new RedBlackTree(8);
        redBlackTree.add(6);
        redBlackTree.add(10);
        redBlackTree.add(16);
        redBlackTree.add(12);
        redBlackTree.add(10);
        redBlackTree.add(4);
        redBlackTree.add(7);
        redBlackTree.add(0);
        redBlackTree.add(8);
        redBlackTree.add(100);
        redBlackTree.inOrderTraversal();
    }
}
