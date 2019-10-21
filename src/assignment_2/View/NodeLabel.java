package assignment_2.View;

import javax.swing.*;
import java.awt.*;

public class NodeLabel extends JLabel {
    private int row;
    private int col;

    public NodeLabel(int row, int col, String str){
        super(str);
        this.row = row;
        this.col = col;
    }


    public void selectableColor(){
        this.setBackground(new Color(25,190,107));
    }

    public void selectedColor(){
        this.setBackground(new Color(45,183,245));
    }

    public void unselectColor(){
        this.setBackground(new Color(220,222,226));
    }

    public void watershedColor(){
        this.setBackground((new Color(255,153,0)));
    }

    public void userSelectColor(){
        this.setBackground(new Color(123,104,238));
    }
}
