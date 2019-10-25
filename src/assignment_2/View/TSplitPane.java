package assignment_2.View;

import javax.swing.*;

/**
 * Just for overriding the setDividerLocation() method, since the origin one is problematic.
 */
public class TSplitPane extends JSplitPane {

    public TSplitPane(int a){
        super(a);
    }

    /**
     *
     * @param proportionalLocation
     * The same as its super class.
     * @param width
     * @param height
     * To set width & height manually V.S. automatically get width and height AFTER father component is setVisible as did by super class in such dumb way.
     */
    public void setDividerLocation(double proportionalLocation,int width,int height) {
        if (proportionalLocation < 0.0 ||
                proportionalLocation > 1.0) {
            throw new IllegalArgumentException("proportional location must " +
                    "be between 0.0 and 1.0.");
        }
        if (getOrientation() == VERTICAL_SPLIT) {
            setDividerLocation((int)((double)(height - getDividerSize()) *
                    proportionalLocation));
        } else {
            setDividerLocation((int)((double)(width - getDividerSize()) *
                    proportionalLocation));
        }
    }
}
