package assignment_2.Model;

import java.util.ArrayList;
import java.util.Random;

public class Terrain {
    private static final int LOWER_BOUND = -5;
    private static final int UPPER_BOUND = 15;
    private Node[][] matrix;
    private int height;
    private int width;

    public Terrain(){

    }

    public Node[][] getMatrix() {
        return matrix;
    }

    public void generateMatrix(int height, int width){
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
     */
    public void findRoute(int insight){
        Integer layer = 0;
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

    private Node[][] subMatrix(Integer begin, int bound){
        Node[][] tmp = new Node[bound+1][width];
        for(int i = 0; i<=bound;i++){
            tmp[i] = matrix[begin-1];
            begin++;
        }
        return tmp;
    }

    private void setInf(Node[][] matrix, ArrayList<Node> mins){
        for(Node node : matrix[0]){
            if(!mins.contains(node)){
                node.setInf();
            }
        }
    }

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
    }

    public ArrayList<Node> getShortestNode(){
        return getShortestNode(this.height);
    }

    private ArrayList<Node> getShortestNode(int top){
        Node min;
        ArrayList<Node> mins = new ArrayList<>();
        Node[] topLayer = matrix[top-1];
        min = topLayer[0];
        for (int i = 0;i<width-1;i++){
            if(min.getEffort()>topLayer[i+1].getEffort()){
                min = topLayer[i+1];
            }
        }
        for (Node each:topLayer){
            if(each.getEffort() == min.getEffort()){
                mins.add(each);
            }
        }
        return mins;
    }

    private Node getMin(Node side, Node middle){
        return side.getEffort()<middle.getEffort()?side:middle;
    }

    private Node getMin(Node left,Node middle, Node right){
        Node min = left.getEffort()<right.getEffort() ? left : right;
        return min.getEffort()<middle.getEffort()? min : middle;
    }

    public static int genInt(int lowerBound, int upperBound){
        Random rd = new Random();
        int tmp = rd.nextInt(upperBound-lowerBound+1);
        tmp += lowerBound;
        return tmp;
    }

    private static int genInt(int lowerBound, int upperBound, Random rd){
        int tmp = rd.nextInt(upperBound-lowerBound+1);
        tmp += lowerBound;
        return tmp;
    }

    public static void main(String[] args) {
        Terrain tr = new Terrain();
        tr.generateMatrix(10,10);
        int i = 0;
        for(Node[] layer:tr.getMatrix()){
            System.out.print("["+i+"]  ");
            for(Node node:layer){
                System.out.print(node+"  \t");
            }
            i++;
            System.out.println();
        }
        tr.findRoute(10);
        for (Node shortest:tr.getShortestNode()){
            System.out.println(shortest.getRoute() + "\tDIFFICULTY = "+shortest.getEffort());
        }


    }

}
