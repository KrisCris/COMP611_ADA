package assignment_2.Model;

import java.util.ArrayList;
import java.util.Random;

public class Terrain {
    private static final int LOWER_BOUND = -5;
    private static final int UPPER_BOUND = 15;
    private Node[][] matrix;
    private int height;
    private int width;
    private int layer;

    public Terrain(){
    }

    public Node[][] getMatrix() {
        return matrix;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getLayer() {
        return layer;
    }

    public void generateMatrix(int height, int width){
        layer = 0;
        this.width = width;
        this.height = height;
        Random rd = new Random();
        matrix = new Node[height][width];
        for(int i = 0;i<height;i++){
            for(int j = 0;j<matrix[i].length;j++){
                matrix[i][j] = new Node(genInt(LOWER_BOUND,UPPER_BOUND,rd),i,j);
                if(i == 0){
                    matrix[i][j].setLast(null);
                }
            }
        }
    }

    /**
     * layer indicates the next layer that needs to calculate the lowest difficulty.
     * @param insight
     * insight is the number of layer this program can see at once,
     * also means the Intelligence described in assignment2.pdf.
     *
     * If the intelligence is 100%, then only the first loop can be entered.
     * Otherwise, the second loop will be entered,
     * and in order to make sure only the partially shortest route determined by last loop can be chosen,
     * the difficulty of the rest of nodes in the same layer will be set to INF.
     *
     * BTW, method oneKeyFindRoute(int) is only called by the integrated main(str[]) method.
     * When using GUI, only findRoute(int) will be called.
     */
    public void oneKeyFindRoute(int insight){
        layer = 0;
        Node[][] tmp= new Node[insight][width];
        for (int i = 0;i<insight;i++){
            tmp[i] = matrix[layer++];
        }
        findRoute(tmp);
        while(height-layer>=insight){
            tmp = subMatrix(layer,insight);
            setInf(tmp,getShortestNode(layer));
            findRoute(tmp);
            layer += insight;
        }
        int rest = height-layer;
        if(rest>0){
            tmp = subMatrix(layer,rest);
            setInf(tmp,getShortestNode(layer));
            findRoute(tmp);
        }
    }

    /**
     * The GUI Controlled version of automatically route finding.
     * This methods will call the private findRoute(), where the real algorithm performed.
     * @param insight
     * @return
     */
    public ArrayList<Node> findRoute(int insight){
        Node[][] tmp= new Node[insight][width];
        int layersToConquer = height-layer;
        if(layer == 0 && this.height<=insight){
            for (int i = 0;i<height;i++){
                tmp[i] = matrix[layer++];
            }
            findRoute(tmp);
            return this.getShortestNode();
        } else if(layersToConquer>=insight && layer==0){
            for (int i = 0;i<insight;i++){
                tmp[i] = matrix[layer++];
            }
            findRoute(tmp);
            ArrayList<Node> partialShortest = this.getShortestNode(layer);
            for(Node node:partialShortest){
                node.setWatershed(true);
            }
            return partialShortest;
        } else if(layersToConquer>=insight){
            tmp = subMatrix(layer,insight);
            setInf(tmp,getShortestNode(layer));
            findRoute(tmp);
            layer += insight;
            ArrayList<Node> partialShortest = this.getShortestNode(layer);
            for(Node node:partialShortest){
                node.setWatershed(true);
            }
            return partialShortest;
        } else if(layersToConquer<insight && layersToConquer>0){
            tmp = subMatrix(layer,layersToConquer);
            setInf(tmp,getShortestNode(layer));
            layer = height;
            findRoute(tmp);
            return this.getShortestNode();
        }
        //dummy return
        return getShortestNode();
    }

    /**
     * The real decision making algorithm.
     * @param subMatrix
     */
    private void findRoute(Node[][] subMatrix){
        Node min;
        for(int i = 1; i<subMatrix.length;i++){
            for(int j = 0; j<subMatrix[i].length;j++){
                int lastRow = i-1;
                if(j==0){
                    min = getMin(subMatrix[lastRow][j+1],subMatrix[lastRow][j]);
                } else if (j==subMatrix[i].length-1){
                    min = getMin(subMatrix[lastRow][j-1],subMatrix[lastRow][j]);
                } else {
                    min = getMin(subMatrix[lastRow][j-1],subMatrix[lastRow][j],subMatrix[lastRow][j+1]);
                }
                subMatrix[i][j].setLast(min);
            }
        }
        //test, display the route when reaching the top.
        if(this.layer == height){
            for (Node shortest:getShortestNode()){
                System.out.println(shortest.getRoute() + "\tDIFFICULTY = "+shortest.getDist());
            }
        }
        //
    }

    /**
     * To get a part of the node Matrix.
     *
     * @param begin
     * The row this subMatrix begins.
     * @param bound
     * How many rows this subMatrix contains.
     * @return
     * A subMatrix.
     */
    private Node[][] subMatrix(Integer begin, int bound){
        Node[][] tmp = new Node[bound+1][width];
        for(int i = 0; i<=bound;i++){
            tmp[i] = matrix[begin-1];
            begin++;
        }
        return tmp;
    }

    /**
     * To set the dist of unreachable nodes to INF.
     * @param matrix
     * @param mins
     */
    private void setInf(Node[][] matrix, ArrayList<Node> mins){
        for(Node node : matrix[0]){
            if(!mins.contains(node)){
                node.setInf();
            }
        }
    }

    public ArrayList<Node> getShortestNode(){
        return getShortestNode(this.height);
    }

    /**
     *
     * @param top
     * Top[1..nLayer]
     * @return
     */
    public ArrayList<Node> getShortestNode(int top){
        Node min;
        ArrayList<Node> mins = new ArrayList<>();
        Node[] topLayer = matrix[top-1];
        min = topLayer[0];
        for (int i = 0;i<width-1;i++){
            if(min.getDist()>topLayer[i+1].getDist()){
                min = topLayer[i+1];
            }
        }
        for (Node each:topLayer){
            if(each.getDist() == min.getDist()){
                mins.add(each);
            }
        }
        return mins;
    }

    /**
     * @return
     * Return the minimum node among the 3 or 2 nodes reachable.
     */
    private Node getMin(Node side, Node middle){
        return side.getDist()<middle.getDist()?side:middle;
    }

    private Node getMin(Node left,Node middle, Node right){
        Node min = left.getDist()<right.getDist() ? left : right;
        return min.getDist()<middle.getDist()? min : middle;
    }

    public static int genInt(int lowerBound, int upperBound){
        Random rd = new Random();
        int tmp = rd.nextInt(upperBound-lowerBound+1);
        tmp += lowerBound;
        return tmp;
    }

    /**
     * Randomly generate integers in a range.
     * @param lowerBound
     * The lowest number can be generated.
     * @param upperBound
     * The largest number can be generated.
     * @param rd
     * A Random instance.
     * To avoid generating multiple Random instances and saving memories.
     * @return
     * A integer generated.
     */
    private static int genInt(int lowerBound, int upperBound, Random rd){
        int tmp = rd.nextInt(upperBound-lowerBound+1);
        tmp += lowerBound;
        return tmp;
    }

    /**
     * To link nodes being clicked together.
     * And set dist of other nodes to NIF.
     * @param row
     * @param col
     * @return
     */
    public int manual(int row,int col){
        Node current = this.matrix[row][col];
        ArrayList<Node> min;
        for(int i = 0;i<width;i++){
            if(i!=col){
                matrix[row][i].setInf();
            }
        }
        if(row != 0){
            min = this.getShortestNode(row);
            for(Node each:min){
                int x = each.getCol();
                if(x == col || x-1 == col || x+1 == col){
                    current.setLast(each);
                }
            }
        }
        layer++;
        return current.getDist();
    }

    /**
     * Implementing the step back functionality.
     * It decrease the layer level and set the EFFORT of current row of nodes to regular value than INF.
     * Only works when undo manual decision.
     *
     * It works more complex when undoing the automatic decision,
     * since there can be multiple shortest node in each row, and each situation can be different,
     * and it took me tons of efforts to debug and rewrite the code,
     * in the end I disabled it.
     *
     * @return
     * The node need to be undo.
     */
    public Node prev(){
        if(this.layer>0){
            Node undoNode = this.getShortestNode(layer).get(0);
            for(Node node:this.matrix[layer-1]){
                node.setLast(node.getLast());
            }
            layer--;
            return undoNode;
        }
        return null;
    }

    /**
     * Every node will be reset.
     * This method is provided for someone who want to play with the same terrain multiple times.
     */
    public void resetRoute(){
        this.layer = 0;
        for(Node[] nodes: matrix){
            for (Node node:nodes){
                node.reset();
            }
        }
    }

    public static void main(String[] args) {
        Terrain tr = new Terrain();
        tr.generateMatrix(5,5);
        int i = 0;
        for(Node[] layer:tr.getMatrix()){
            System.out.print("["+i+"]  ");
            for(Node node:layer){
                System.out.print(node+"  \t");
            }
            i++;
            System.out.println();
        }
        tr.oneKeyFindRoute(1);
        for (Node shortest:tr.getShortestNode()){
            System.out.println(shortest.getRoute() + "\tDIFFICULTY = "+shortest.getDist());
        }
    }

}
