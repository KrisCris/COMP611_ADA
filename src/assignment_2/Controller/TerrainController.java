package assignment_2.Controller;

import assignment_2.Model.Node;
import assignment_2.Model.Terrain;
import assignment_2.View.NodeLabel;
import assignment_2.View.TerrainView;

import java.util.ArrayList;

public class TerrainController {
    TerrainView view;
    Terrain model;

    public TerrainController(Terrain model,TerrainView view){
        this.view = view;
        this.model = model;
        this.view.setVisible(true);
        this.view.disablePrev();
        this.view.disableNext();
    }

    public void processGenerateEvent(int height, int width){
        this.view.updateDifficulty(0);
        this.model.generateMatrix(height,width);
        this.view.generateTerrain(this.model.getMatrix(),width,height);
        this.view.setTerrainLayout(width,height);
        if(width>20||height>20){
            this.view.resizeWindow(1);
        } else {
            this.view.resizeWindow(0);
        }
        this.view.drawTerrain();
        this.view.refreshTerrainPanel();
        this.view.enableNext();
        this.view.disablePrev();
    }

    public void processClickEvent(NodeLabel source){
        if(source.isSelectable()){
            //model
            int row = source.getRow();
            int col = source.getCol();
            this.view.updateDifficulty(this.model.manual(row,col));
            //view
            ArrayList<Node> test = model.getShortestNode(model.getLayer());
            System.out.println(test.get(0));
            this.view.clearAutoLabel(test);
            source.userSelectColor();
            this.view.unselectable(row);
            if(row < this.model.getHeight()-1){
                nextToSelect(row,col);
                this.view.enablePrev();
            } else {
                this.view.disablePrev();
                this.view.disableNext();
                //TODO Game finished. Print best solution maybe
            }
        }
    }

    public void processNextEvent(int intelli){
        this.view.disablePrev();
        int layer = this.model.getLayer();
        int row, col;
        this.view.unselectable(layer);
        ArrayList<Node> shortest = this.model.findRoute(intelli);
        this.view.clearAutoLabel(shortest);
        for(Node node:shortest){
            this.view.updateDifficulty(node.getEffort());
            if(this.model.getLayer() < this.model.getHeight()){
                this.nextToSelect(node.getRow(),node.getCol());
            } else {
                this.view.disableNext();
            }
            for(int i = node.getRow();i>=layer;i--){
                row = node.getRow();
                col = node.getCol();
                NodeLabel nl = this.view.getLabel(row,col);
                if(node.isWatershed()){
                    nl.watershedColor();
                    nl.setToolTipText("A farthest node reached with intelligence = "+intelli);
                } else {
                    nl.selectedColor();
                }
                node = node.getLast();
            }
        }
    }

    public void processPrevEvent(){
        Node undoNode = this.model.prev();
        int row = undoNode.getRow();
        this.view.unselectable(row+1);
        if(row<1){
            for(int i = 0;i<this.model.getWidth();i++){
                this.view.setSelectable(0,i);
                this.view.disablePrev();
            }
        } else {
            this.view.unselectable(row);
            int prevRow = undoNode.getLast().getRow();
            int prevCol = undoNode.getLast().getCol();
            nextToSelect(prevRow,prevCol);
        }
    }

    public void nextToSelect(int row, int col){
        if(col==0){
            this.view.setSelectable(row+1,0);
            this.view.setSelectable(row+1,1);
        } else if(col==this.model.getWidth()-1){
            this.view.setSelectable(row+1,col);
            this.view.setSelectable(row+1,col-1);
        } else {
            this.view.setSelectable(row+1,col);
            this.view.setSelectable(row+1,col-1);
            this.view.setSelectable(row+1,col+1);
        }
    }




}
