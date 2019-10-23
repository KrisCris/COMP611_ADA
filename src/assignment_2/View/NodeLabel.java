package assignment_2.View;

import javax.swing.*;
import java.awt.*;

public class NodeLabel extends JLabel {
    private static final Color BACKGROUND = new Color(23, 35, 61);
    private static final Color FOREGROUND = new Color(248, 248, 249);
    private static final Color MOUSE_ENTER = new Color(81, 90, 110);
    private static final Color MOUSE_PRESSED = new Color(128, 134, 149);
    private static final Color SELECTABLE = new Color(25,190,107);
    private static final Color ENTER_GR = new Color(50, 213, 131);
    private static final Color PRESS_GR = new Color(109, 236, 172);
    private static final Color AUTO_SELECTED = new Color(45,183,245);
    private static final Color WATERSHED = new Color(255,153,0);
    private static final Color USER_SELECTED = new Color(123,104,238);
    private int row;
    private int col;
    private boolean selectable;



    public NodeLabel(int row, int col, int str){
        super(str+"",JLabel.CENTER);
        this.row = row;
        this.col = col;
        this.setOpaque(true);
        this.unselectColor();
        this.setForeground(FOREGROUND);
        this.setPreferredSize(new Dimension(25,25));
        this.selectable = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * Color setting methods
     */
    public void selectableColor(){
        this.setBackground(SELECTABLE);
    }

    public void selectedColor(){
        this.setBackground(AUTO_SELECTED);
    }

    public void unselectColor(){
        this.setBackground(BACKGROUND);
    }

    public void watershedColor(){
        this.setBackground((WATERSHED));
    }

    public void userSelectColor(){
        this.setBackground(USER_SELECTED);
    }

    public void mouseEnterColor(){
        this.setBackground(MOUSE_ENTER);
    }

    public void mousePressedColor(){
        this.setBackground(MOUSE_PRESSED);
    }

    public void enterGrColor(){
        this.setBackground(ENTER_GR);
    }

    public void pressGrColor(){
        this.setBackground(PRESS_GR);
    }

    public boolean isUserSelectedColor(){
        if(this.getBackground() == USER_SELECTED) return true;
        else return false;
    }

    /**
     * @return
     * Whether the label is in this color.
     */
    public boolean isDefaultColor(){
        if(this.getBackground() == BACKGROUND) return true;
        else return false;
    }

    public boolean isMouseEnterColor(){
        if(this.getBackground() == MOUSE_ENTER) return true;
        else return false;
    }

    public boolean isMousePressedColor(){
        if(this.getBackground() == MOUSE_PRESSED) return true;
        else return false;
    }

    public boolean isSelectableColor(){
        if(this.getBackground() == SELECTABLE) return true;
        else return false;
    }

    public boolean isEnterGrColor(){
        if(this.getBackground() == ENTER_GR) return true;
        else return false;
    }

    public boolean isPressGrColor(){
        if(this.getBackground() == PRESS_GR) return true;
        else return false;
    }

    public boolean isWatershedColor(){
        if(this.getBackground() == WATERSHED) return true;
        else return false;
    }

    public boolean isAutoSelectedColor(){
        if(this.getBackground() == AUTO_SELECTED) return true;
        else return false;
    }

}
