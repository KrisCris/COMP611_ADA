package assignment_4.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.server.UID;
import java.util.Observable;

public class FigureModel extends Observable {
    private int[][] figureMatrix;
    private int x = 180;
    private int y = 180;

    private int currentPointX;
    private int currentPointY;

    public FigureModel(){
        this.figureMatrix = new int[x][y];
        this.currentPointY = 0;
        this.currentPointX = 0;

        for (int i = 0; i<x;i++){
            for (int j = 0; j<y;j++){
                this.figureMatrix[i][j] = 0;
            }
        }

    }

    public void updateMatrix(int x, int y){
        this.currentPointX = x;
        this.currentPointY = y;

        for (int i = x; i < x + 10; i++) {
            for (int j = y; j < y + 10; j++) {
                figureMatrix[i][j] = 1;
            }
        }
        this.setChanged();
        this.notifyObservers(this);
    }

    public int getCurrentPointX() {
        return currentPointX;
    }

    public int getCurrentPointY() {
        return currentPointY;
    }

    public void saveSample() {
        try {
            PrintWriter pr = new PrintWriter(new UID().hashCode()+".txt");
            for(int i=0;i<180;i++) {
                for(int j=0;j<180;j++) {
//                    System.out.print(this.figureMatrix[i][j]+" ");
                    pr.print(this.figureMatrix[i][j]);
                }
//                System.out.println();
            }
            pr.flush();
            pr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
