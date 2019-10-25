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
        this.view.disableReset();
    }

    /**
     * @param height
     * @param width
     * The size of the terrain to be generated.
     */
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
        this.view.disableReset();
    }

    /**
     * The reaction of each Label being clicked.
     * @param source
     */
    public void processClickEvent(NodeLabel source){
        if(source.isSelectable()){
            this.view.enableReset();
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

    /**
     * Automatically find the shortest path when the NEXT button is clicked.
     * @param intelli
     * The intelligence, decides how many rows the decision making algorithm can see at this time.
     */
    public void processNextEvent(int intelli){
        this.view.disablePrev();
        this.view.enableReset();
        int layer = this.model.getLayer();
        int row, col;
        this.view.unselectable(layer);
        ArrayList<Node> shortest = this.model.findRoute(intelli);
        this.view.clearAutoLabel(shortest);
        for(Node node:shortest){
            this.view.updateDifficulty(node.getDist());
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
                    nl.setToolTipText("The farthest node reached with intelligence = "+intelli);
                } else {
                    nl.selectedColor();
                }
                node = node.getLast();
            }
        }
    }

    /**
     * To undo manual controlled steps.
     */
    public void processPrevEvent(){
        Node undoNode = this.model.prev();
        System.out.println("UndoNode: "+undoNode.toString()+" Dist: "+undoNode.getDist());   //debug
        int row = undoNode.getRow();
        this.view.unselectable(row+1);
        if(row<1){
            for(int i = 0;i<this.model.getWidth();i++){
                this.view.setSelectable(0,i);
                this.view.updateDifficulty(0);
                this.view.disablePrev();
                this.view.disableReset();
            }
        } else {
            this.view.unselectable(row);
            int prevRow = undoNode.getLast().getRow();
            int prevCol = undoNode.getLast().getCol();
            System.out.println("prev back to "+undoNode.getLast().toString()+"\n"); //debug
            this.view.updateDifficulty(undoNode.getLast().getDist());
            nextToSelect(prevRow,prevCol);
            //disable PREV functionality when meet auto decision.
            if(!this.view.getLabel(prevRow,prevCol).isUserSelectedColor()){
                this.view.disablePrev();
            }
        }
    }

    /**
     * Every node will be reset.
     * This method is provided for someone who want to play with the same terrain multiple times.
     */
    public void processResetEvent(){
        if(this.model.getLayer()>0){
            this.view.resetRound(this.model.getShortestNode(this.model.getLayer()));
            this.view.refreshTerrainPanel();
            this.view.disableReset();
            this.model.resetRoute();
        }
    }

    /**
     * To highlight the label of nodes that can be clicked next.
     * @param row
     * @param col
     */
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

    /**
     * Do something when this Controller is registered to the View.
     */
    public void VCRegistered(){
        this.view.initFirstRound();
    }
}
