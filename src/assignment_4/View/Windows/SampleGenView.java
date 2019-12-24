package assignment_4.View.Windows;

import assignment_4.View.Components.SketchpadPanel;
import javax.swing.*;


public class SampleGenView extends JFrame {
    public final int HEIGHT = 300;
    public final int WIDTH = 200;


    private SketchpadPanel sketchpadPanel;
    private JButton saveBtn;
    private JButton clearBtn;

    public SampleGenView() {
        this.initComponents();
        this.setLayout();
    }

    private void initComponents() {
        this.setSize(WIDTH, HEIGHT);
        this.sketchpadPanel = new SketchpadPanel();
        this.saveBtn = new JButton("Save sample");
        this.clearBtn = new JButton("Clear sketchpad");
    }

    private void setLayout() {
        this.setLayout(null);

        this.sketchpadPanel.setBounds(
                10,
                10,
                sketchpadPanel.WIDTH,
                sketchpadPanel.HEIGHT
        );
        this.saveBtn.setBounds(
                200,
                10,
                WIDTH / 2 - 15,
                20
        );
        this.clearBtn.setBounds(
                200,
                WIDTH / 2 + 5,
                WIDTH / 2 - 15,
                20
        );

        this.add(sketchpadPanel);
        this.add(saveBtn);
        this.add(clearBtn);
    }



    public static void main(String[] args) {
        SampleGenView sgf = new SampleGenView();
        sgf.setVisible(true);
    }
}
