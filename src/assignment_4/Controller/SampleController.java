package assignment_4.Controller;

import assignment_4.Model.Model;
import assignment_4.Model.Outline;
import assignment_4.Util.GraphicsUtil;
import assignment_4.Util.IOUtil;
import assignment_4.View.View;
import assignment_4.View.Windows.SampleView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class SampleController implements Controller {
    private View view;
    private Model model;

    @Override
    public void bindView(View v) {
        this.view = v;
        this.view.registerController(this);
    }

    @Override
    public void bindModel(Model m) {
        this.model = model;
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
                    BufferedImage panelImage = GU.panelToImage(sv.getSketchpadPanel());

                    double[] grayM = GU.imageToGrayMatrix(panelImage);
                    int[] binaryM = GU.garyToBinaryMatrix(grayM);

                    Outline outline = GU.getOutline(binaryM);
                    sv.getSketchpadPanel().setOutLine(outline);

                    BufferedImage figureImage = GU.compressImage(
                            GU.getFigureImage(
                                    panelImage,
                                    outline
                            ),
                            32,
                            32
                    );

                    int[] binaryFigure =
                            GU.garyToBinaryMatrix(
                                    GU.imageToGrayMatrix(figureImage)
                            );

                    IOU.matrixToFile(binaryFigure, -1);

                } else if (btn.getText().equals("Clear")) {
                    sv.getSketchpadPanel().clear();

                } else if (btn.getText().equals("Recognize")) {


                }
            }
        }


    }
}
