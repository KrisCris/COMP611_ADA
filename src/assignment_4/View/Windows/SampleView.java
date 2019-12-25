package assignment_4.View.Windows;

import assignment_4.Controller.Controller;
import assignment_4.Controller.SampleController;
import assignment_4.View.Components.SketchpadPanel;
import assignment_4.View.View;

import javax.swing.*;


public class SampleView extends JFrame implements View {
    public final int HEIGHT = 480;
    public final int WIDTH = 400;


    private SketchpadPanel sketchpadPanel;
    private JComboBox<Integer> numberList;
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
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.sketchpadPanel = new SketchpadPanel();
        this.numberList = new JComboBox<>();
        for (int i = 0; i < 10; i++) {
            this.numberList.addItem(i);
        }
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
                35
        );
        this.clearBtn.setBounds(
                150,
                360,
                100,
                35
        );
        this.recognizeBtn.setBounds(
                260,
                360,
                100,
                35
        );

        this.numberList.setBounds(
                40,
                405,
                320,
                35
        );

        this.add(sketchpadPanel);
        this.add(saveBtn);
        this.add(clearBtn);
        this.add(recognizeBtn);
        this.add(numberList);
    }

    public SketchpadPanel getSketchpadPanel() {
        return sketchpadPanel;
    }

    public JComboBox<Integer> getNumberList() {
        return numberList;
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
