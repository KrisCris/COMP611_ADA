package assignment_2.View;

import javax.swing.*;

public class TSplitPane extends JSplitPane {

    public TSplitPane(int a){
        super(a);
    }
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
