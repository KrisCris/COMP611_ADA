package assignment_4.View.Windows;

import assignment_4.Controller.Controller;
import assignment_4.Controller.SampleController;
import assignment_4.View.Components.SketchpadPanel;
import assignment_4.View.View;

import javax.swing.*;


public class SampleView extends JFrame implements View {
    public final int HEIGHT = 420;
    public final int WIDTH = 400;


    private SketchpadPanel sketchpadPanel;
    private JButton saveBtn;
    private JButton clearBtn;
    private JButton recognizeBtn;

    public SampleView() {
        super("Sample figures");
        this.initComponents();
        this.setLayout();
    }

    private void initComponents() {
        this.setSize(WIDTH, HEIGHT);
        this.sketchpadPanel = new SketchpadPanel();
        this.saveBtn = new JButton("Save");
        this.clearBtn = new JButton("Clear");
        this.recognizeBtn = new JButton("Recognize");
    }

    private void setLayout() {
        this.setLayout(null);

        this.sketchpadPanel.setBounds(
                40,
                20,
                320,
                320
        );
        this.saveBtn.setBounds(
                40,
                360,
                100,
                20
        );
        this.clearBtn.setBounds(
                150,
                360,
                100,
                20
        );
        this.recognizeBtn.setBounds(
                260,
                360,
                100,
                20
        );

        this.add(sketchpadPanel);
        this.add(saveBtn);
        this.add(clearBtn);
        this.add(recognizeBtn);
    }

    public SketchpadPanel getSketchpadPanel() {
        return sketchpadPanel;
    }

    @Override
    public void registerController(Controller c) {
        this.saveBtn.addActionListener(c);
        this.clearBtn.addActionListener(c);
        this.recognizeBtn.addActionListener(c);
    }

    public static void main(String[] args) {
        SampleView sv = new SampleView();
        SampleController sc = new SampleController();
        sc.bindView(sv);
        sv.setVisible(true);
    }
}
