package assignment_2.Model;

public class Node {
    /**
     * VALUE is the difficulty of this node;
     * EFFORT is the add all difficulty that a program took to travel to this node;
     * LAST indicates the last node which takes the lowest effort.
     */
    private int value;
    private int effort;
    private Node last;
    private int row;
    private int col;

    public Node(int value){
        this.value = value;
    }

    public Node(int value, int row, int col){
        this.value = value;
        this.setPos(row,col);
    }

    public void setPos(int row, int col){
        this.row = row;
        this.col = col;
    }

    public Node getLast() {
        return last;
    }

    public void setLast(Node last) {
        this.last = last;
        setEffort();
    }

    public int getEffort(){
        return effort;
    }

    /**
     * To set effort internally determined by the last node.
     * This method will be called when node is set.
     */
    private void setEffort(){
        if(this.last == null){
            this.effort = this.value;
        } else {
            this.effort = this.last.getEffort() + this.value;
        }
    }

    public String toString(){
        return this.value+" ("+row+","+col+")";
    }

    public String getRoute(){
        if(this.last == null){
            return toString()+"";
        } else {
            return toString()+"  <-  "+last.getRoute();
        }
    }
}
