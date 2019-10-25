package assignment_2.Model;

public class Node {
    /**
     * VALUE is the difficulty of this node;
     * DIST is the minimum total difficulty that a program took to travel to this node;
     * LAST indicates the last node which takes the lowest effort.
     */
    private static final int INF = 99999999;
    private int value;
    private int dist;
    private Node last;
    private int row;
    private int col;
    private boolean watershed;

    public Node(int value){
        this.value = value;
        this.watershed = false;
    }

    /**
     *
     * @param value
     * The cost of this node.
     * @param row
     * The row this node is positioned.
     * @param col
     * The column this node is positioned.
     */
    public Node(int value, int row, int col){
        this(value);
        this.setPos(row,col);
    }

    public boolean isWatershed() {
        return watershed;
    }

    /**
     *
     * @param watershed
     * When intelligence less than 100%, each node of the decision made will be set to watershed = true.
     * Which means next round of decision making will starting from this node.
     * And a node with watershed = true will be displayed in orange color in the View.
     */
    public void setWatershed(boolean watershed) {
        this.watershed = watershed;
    }

    public void setPos(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }

    public void setInf(){
        this.dist = INF;
    }

    public Node getLast() {
        return last;
    }

    public void setLast(Node last) {
        this.last = last;
        setEffort();
    }

    public int getDist(){
        return dist;
    }

    /**
     * To set effort internally determined by the last node.
     * This method will be called when node is set.
     */
    private void setEffort(){
        if(this.last == null){
            this.dist = this.value;
        } else {
            this.dist = this.last.getDist() + this.value;
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

    /**
     * If you want to play with this terrain again without change the cost of each node,
     * this method will be called.
     */
    public void reset(){
        if(this.row == 0){
            this.dist = this.value;
        } else {
            this.dist = 0;
        }
        this.last = null;
        this.watershed = false;
    }
}
