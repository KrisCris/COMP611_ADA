package assignment_4.Controller;

import assignment_4.Util.GraphicsUtil;
import assignment_4.Util.IOUtil;
import assignment_4.Util.KNN;
import assignment_4.View.Windows.SampleView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SampleController implements ActionListener {
    private SampleView view;

    public SampleController(SampleView view){
        this.view = view;
        this.view.registerController(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        /**
         * If this event comes from a JButton and sv.
         */
        if (view instanceof SampleView) {
            SampleView sv = (SampleView) view;
            if (o instanceof JButton) {
                JButton btn = (JButton) o;

                GraphicsUtil GU = GraphicsUtil.getInstance();
                IOUtil IOU = IOUtil.getInstance();

                if (btn.getText().equals("Save")) {
                    int number = sv.getNumberList().getSelectedIndex();

                    int[] binaryFigure = GU.panelToBinaryFigureMatrix(sv.getSketchpadPanel());

                    IOU.matrixToFile(binaryFigure, number);

                } else if (btn.getText().equals("Clear")) {
                    sv.getSketchpadPanel().clear();

                } else if (btn.getText().equals("Recognize")) {
                    int[] targetMatrix = GU.panelToBinaryFigureMatrix(sv.getSketchpadPanel());
                    
                    KNN knnCore = KNN.getKnnCore();
                    int result = knnCore.getResult(targetMatrix, 6);
                    JOptionPane.showMessageDialog(null,"Estimated: "+result,"result",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }


    }
}
