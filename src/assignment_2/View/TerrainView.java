package assignment_2.View;


import assignment_2.Controller.TerrainController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class TerrainView extends JFrame{
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 720;
    private static final Color BACKGROUND = new Color(248, 248, 249);
    private static final Color FOREGROUND = new Color(23, 35, 61);
    private TerrainController controller;

    private JSplitPane mainPanel;
    private JPanel settingPanel;
    private JPanel terrainPanel;

    /**
     * Components of TerrainGen settings
     */


    private JTextField widthInput;
    private JTextField heightInput;

    private JRadioButton auto;
    private JRadioButton manual;
    private ButtonGroup mode;

    private JSlider intelligence;
    private JButton generate;

    /**
     * Components show terrain staffs
     */
    private JLabel costs;
    private JPanel terrain;


    public TerrainView(){
        this.setSize(1080,720);
        this.setBackground(BACKGROUND);
        this.setForeground(FOREGROUND);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);


        this.add(this.mainPanel);
        this.settingPanel = new JPanel();
        this.terrainPanel = new JPanel();
        this.mainPanel.add(this.settingPanel,JSplitPane.LEFT);
        this.mainPanel.add(this.terrainPanel,JSplitPane.RIGHT);

        this.mainPanel.setBackground(BACKGROUND);
        this.mainPanel.setEnabled(false);
        this.settingPanel.setBackground(new Color(45,183,245));
        this.terrainPanel.setBackground(new Color(123,104,238));






        this.widthInput = new JTextField();
        this.heightInput = new JTextField();
        this.auto = new JRadioButton("AUTO");
        this.auto.setSelected(true);
        this.manual = new JRadioButton("MANUAL");
        this.mode = new ButtonGroup();
        this.mode.add(this.auto);
        this.mode.add(this.manual);
        this.intelligence = new JSlider(JSlider.HORIZONTAL);
        this.generate = new JButton("Generate");
        this.setVisible(true);
        this.mainPanel.setDividerLocation(1.0/3.0);
    }

    public void updateTerrain(){
        
    }

    public void registerController(TerrainController terrainController){
        this.controller = terrainController;
    }

    public static void main(String[] args) {
        TerrainView terrainView = new TerrainView();

    }
}
