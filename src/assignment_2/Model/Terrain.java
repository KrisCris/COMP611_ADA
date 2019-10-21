package assignment_2.Model;

import java.util.Random;

public class Terrain {
    private final int LOWER_BOUND = -5;
    private final int UPPER_BOUND = 15;
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
     */
    public void findRoute(int insight){
        int layer = 0;
        Node[][] tmp= new Node[insight][width];
        for (int i = 0;i<insight;i++){
            tmp[i] = matrix[layer++];
        }
        findRoute(tmp);
        layer++;
        while(height-layer>=insight){
            tmp= new Node[insight+1][width];
            for (int i = 0;i<=insight;i++){
                tmp[i] = matrix[layer-1];
                layer++;
            }
            findRoute(tmp);
        }
        int rest = height-layer;
        if(rest>0){
            tmp = new Node[rest+1][width];
            for(int i = 0; i<=rest;i++){
                tmp[i] = matrix[layer-1];
                layer++;
            }
            findRoute(tmp);
        }
    }

    private void findRoute(Node[][] subMatrix){
        Node min;
        for(int i = 1; i<subMatrix.length;i++){
            for(int j = 0; j<subMatrix[i].length;j++){
                int lastRow = i-1;
                if(j==0){
                    //Middle of each row
                    min = getMin(subMatrix[lastRow][j+1],subMatrix[lastRow][j]);
                } else if (j==subMatrix[i].length-1){
                    //Tail of each row
                    min = getMin(subMatrix[lastRow][j-1],subMatrix[lastRow][j]);
                } else {
                    min = getMin(subMatrix[lastRow][j-1],subMatrix[lastRow][j],subMatrix[lastRow][j+1]);
                }
                subMatrix[i][j].setLast(min);
            }
        }
    }

    public Node getShortestNode(){
        Node min;
        Node[] topLayer = matrix[height-1];
        min = topLayer[0];
        for (int i = 0;i<width-1;i++){
            if(min.getEffort()>topLayer[i+1].getEffort()){
                min = topLayer[i+1];
            }
        }
        return min;
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
        tr.findRoute(5);
        Node shortest = tr.getShortestNode();
        System.out.println(shortest.getRoute());

    }

}
