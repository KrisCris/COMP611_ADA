package assignment_2.View;


import assignment_2.Controller.TerrainController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Base64;
import java.util.LinkedList;

public class TerrainView extends JFrame{
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 720;
    private static final Color BACKGROUND = new Color(248, 248, 249);
    private static final Color FOREGROUND = new Color(23, 35, 61);
    private static final Color DIVIDER = new Color(232, 234, 236);
    private TerrainController controller;

    private TSplitPane mainPanel;
    private JPanel settingPanel;
    private JPanel terrainPanel;

    private JLabel tag;
    private JLabel costs;

    private JLabel widthTag;
    private JLabel heightTag;
    private JSpinner widthInput;
    private JSpinner heightInput;
    private JButton generate;

    private JSeparator divider;

    private JSlider intelligence;
    private JButton prev;
    private JButton next;

    public TerrainView(){
        this.setSize(WIDTH,HEIGHT);
        this.setResizable(false);
        this.setBackground(BACKGROUND);
        this.setForeground(FOREGROUND);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.mainPanel = new TSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.add(this.mainPanel);
        this.settingPanel = new JPanel();
        this.terrainPanel = new JPanel();
        this.mainPanel.add(this.settingPanel,JSplitPane.LEFT);
        this.mainPanel.add(this.terrainPanel,JSplitPane.RIGHT);
        this.mainPanel.setBackground(BACKGROUND);
        this.mainPanel.setEnabled(false);
        this.settingPanel.setBackground(BACKGROUND);
        this.terrainPanel.setBackground(BACKGROUND);
        this.mainPanel.setDividerLocation(1.0/3.0,WIDTH,HEIGHT);
        this.mainPanel.setDividerSize(0);
        this.tag = new JLabel("CURRENT DIFFICULTY",JLabel.CENTER);
        this.costs = new JLabel("0",JLabel.CENTER);
        this.widthTag = new JLabel("WIDTH",JLabel.CENTER);
        this.heightTag = new JLabel("HEIGHT",JLabel.CENTER);

        this.widthInput = new JSpinner(new SpinnerNumberModel(5,2,99,1));
        this.heightInput = new JSpinner(new SpinnerNumberModel(5,2,99,1));
        
        this.generate = new JButton("GENERATE");

        this.divider = new JSeparator(SwingConstants.CENTER);
        this.divider.setPreferredSize(new Dimension(this.settingPanel.getWidth(),20));
        this.divider.setBackground(DIVIDER);

        this.intelligence = new JSlider(JSlider.HORIZONTAL);
        this.prev = new JButton("PREV");
        this.next = new JButton("NEXT");

        /**
         * Absolute Position
         */
        System.out.println(this.mainPanel.getDividerSize());
        this.settingPanel.setLayout(null);
        this.tag.setOpaque(true);
        this.tag.setBackground(FOREGROUND);
        this.tag.setForeground(BACKGROUND);
        this.tag.setBounds(0,0,360,30);
        this.settingPanel.add(this.tag);

        this.costs.setOpaque(true);
        this.costs.setBackground(FOREGROUND);
        this.costs.setForeground(BACKGROUND);
        this.costs.setBounds(0,30,360,60);
        this.costs.setFont(new Font(Font.SANS_SERIF,Font.BOLD,26));
        this.settingPanel.add(this.costs);

        this.widthTag.setBounds(0,100,180,20);
        this.heightTag.setBounds(180,100,180,20);
        this.settingPanel.add(this.widthTag);
        this.settingPanel.add(this.heightTag);

        Font inputFont = new Font(Font.SANS_SERIF,Font.BOLD,14);
        this.widthInput.setForeground(FOREGROUND);
        this.widthInput.setBounds(20,120,140,30);
        this.widthInput.setFont(inputFont);
        this.settingPanel.add(this.widthInput);

        this.heightInput.setForeground(FOREGROUND);
        this.heightInput.setBounds(200,120,140,30);
        this.heightInput.setFont(inputFont);
        this.settingPanel.add(this.heightInput);






    }

    public void showWindow(){
        this.setVisible(true);
    }

    public void updateTerrain(){
        
    }

    public void registerController(TerrainController terrainController){
        this.controller = terrainController;
    }

    public static void main(String[] args) {
        TerrainView terrainView = new TerrainView();
        terrainView.showWindow();

    }
}
