package assignment_2.View;


import assignment_2.Controller.TerrainController;
import assignment_2.Model.Node;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;

public class TerrainView extends JFrame implements ActionListener{
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 720;
    private static final int DEFAULT_SIZE = 5;
    private static final Color BACKGROUND = new Color(248, 248, 249);
    private static final Color FOREGROUND = new Color(23, 35, 61);
    private static final Color DIVIDER = new Color(220, 222, 226);
    private TerrainController controller;

    private NodeLabel[][] nodeLabels;

    private TSplitPane mainPanel;
    private JPanel settingPanel;
    private JPanel terrainPanel;
    private JScrollPane sp;
    private GridLayout terrainLayout;

    private JLabel tag;
    private JLabel costs;

    private JLabel widthTag;
    private JLabel heightTag;
    private JSpinner widthInput;
    private JSpinner heightInput;
    private JButton generate;

    //TODO show a demo before generate
    private JLabel demo;
    private JLabel height;
    private JLabel width;

    private JSeparator divider;

    private JLabel intelliTag;
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
        this.sp = new JScrollPane();
        this.terrainLayout = new GridLayout();
        this.terrainPanel.setLayout(this.terrainLayout);
        this.mainPanel.add(this.settingPanel,JSplitPane.LEFT);
        this.mainPanel.add(this.terrainPanel,JSplitPane.RIGHT);
        this.mainPanel.setBackground(DIVIDER);
        this.mainPanel.setEnabled(false);
        this.settingPanel.setBackground(BACKGROUND);
        this.terrainPanel.setBackground(BACKGROUND);
        this.mainPanel.setDividerLocation(1.0/3.0,WIDTH,HEIGHT);
        this.mainPanel.setDividerSize(1);
        this.tag = new JLabel("CURRENT DIFFICULTY",JLabel.CENTER);
        this.costs = new JLabel("0",JLabel.CENTER);
        this.widthTag = new JLabel("WIDTH",JLabel.CENTER);
        this.heightTag = new JLabel("HEIGHT",JLabel.CENTER);

        this.widthInput = new JSpinner(new SpinnerNumberModel(DEFAULT_SIZE,2,99,1));
        this.heightInput = new JSpinner(new SpinnerNumberModel(DEFAULT_SIZE,2,99,1));
        JFormattedTextField tmp = ((JSpinner.NumberEditor)widthInput.getEditor()).getTextField();
        ((NumberFormatter)tmp.getFormatter()).setAllowsInvalid(false);
        tmp = ((JSpinner.NumberEditor)heightInput.getEditor()).getTextField();
        ((NumberFormatter)tmp.getFormatter()).setAllowsInvalid(false);

        this.terrainLayout.setColumns(DEFAULT_SIZE);
        this.terrainLayout.setRows(DEFAULT_SIZE);

        this.generate = new JButton("GENERATE");

        this.divider = new JSeparator(SwingConstants.CENTER);
        this.divider.setBackground(DIVIDER);

        this.intelliTag = new JLabel("INTELLIGENCE",JLabel.CENTER);
        this.intelligence = new JSlider(JSlider.HORIZONTAL);
        this.prev = new JButton("PREV");
        this.next = new JButton("NEXT");

        /**
         * Absolute Position
         */
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
        this.widthTag.setForeground(FOREGROUND);
        this.heightTag.setForeground(FOREGROUND);
        this.settingPanel.add(this.widthTag);
        this.settingPanel.add(this.heightTag);

        Font font = new Font(Font.SANS_SERIF,Font.BOLD,14);
        this.widthInput.setForeground(FOREGROUND);
        this.widthInput.setBounds(20,120,140,30);
        this.widthInput.setFont(font);
        this.settingPanel.add(this.widthInput);

        this.heightInput.setForeground(FOREGROUND);
        this.heightInput.setBounds(200,120,140,30);
        this.heightInput.setFont(font);
        this.settingPanel.add(this.heightInput);

        this.generate.setBounds(20,165,320,35);
        this.generate.setForeground(FOREGROUND);
        this.settingPanel.add(this.generate);

        this.divider.setBounds(10,215,340,15);
        this.settingPanel.add(this.divider);

        this.intelliTag.setBounds(0,240,360,20);
        this.intelliTag.setForeground(FOREGROUND);
        this.settingPanel.add(this.intelliTag);

        this.intelligence.setBounds(20,270,320,40);
        this.intelligence.setToolTipText("To set how far this algorithm can see in each decision.");
        this.intelligence.setPaintTicks(true);
        this.intelligence.setPaintLabels(true);
        this.intelligence.setSnapToTicks(true);
        this.intelligence.setMinimum(1);
        this.intelligence.setMajorTickSpacing(1);
        this.setIntelliSlider(DEFAULT_SIZE);
        this.intelligence.setForeground(FOREGROUND);
        this.settingPanel.add(this.intelligence);

        this.next.setBounds(20,330,320,35);
        this.prev.setBounds(20,375,320,35);
        this.next.setForeground(FOREGROUND);
        this.prev.setForeground(FOREGROUND);
        this.next.setToolTipText("Automatically making the next decision.");
        this.prev.setToolTipText("To move one step back.");
        this.settingPanel.add(next);
        this.settingPanel.add(prev);

        /**
         * Set event listener.
         */
        this.generate.addActionListener(this);
        this.next.addActionListener(this);
        this.prev.addActionListener(this);
    }

    /**
     * Way to draw a number matrix:
     * Step.1
     */
    public void generateTerrain(Node[][] nodes,int width, int height){
        this.nodeLabels = new NodeLabel[height][width];
        for(int i = 0;i<height;i++){
            for(int j = 0;j<width;j++){
                NodeLabel nl = new NodeLabel(i,j,nodes[i][j].getValue());
                this.nodeLabels[i][j] = nl;
                nl.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        NodeLabel source = getSource(e);
                        TerrainView.this.controller.processClickEvent(source);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        NodeLabel source = getSource(e);
                        if(source.isMouseEnterColor()){
                            source.mousePressedColor();
                        } else if(source.isEnterGrColor()){
                            source.pressGrColor();
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        NodeLabel source = getSource(e);
                        if(source.isMousePressedColor()){
                            source.mouseEnterColor();
                        } else if(source.isPressGrColor()){
                            source.enterGrColor();
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        NodeLabel source = getSource(e);
                        if(source.isDefaultColor()){
                            source.mouseEnterColor();
                        } else if(source.isSelectableColor()){
                            source.enterGrColor();
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        NodeLabel source = getSource(e);
                        if(source.isMouseEnterColor()||source.isMousePressedColor()){
                            source.unselectColor();
                        } else if(source.isEnterGrColor()||source.isPressGrColor()){
                            source.selectableColor();
                        }
                    }

                    private NodeLabel getSource(MouseEvent e){
                        Object source = e.getSource();
                        return (NodeLabel) source;
                    }
                });
                if(i==0){
                    nl.selectableColor();
                    nl.setSelectable(true);
                }
            }
        }
    }

    /**
     * Step.2
     * @param width
     * @param height
     */
    public void setTerrainLayout(int width, int height){
        this.terrainLayout.setColumns(width);
        this.terrainLayout.setRows(height);
        this.terrainLayout.setHgap(2);
        this.terrainLayout.setVgap(2);
    }

    /**
     * Step.3
     */
    public void drawTerrain(){
        this.terrainPanel.removeAll();
        this.terrainPanel.repaint();
        for(int i = this.nodeLabels.length; i>0;i--){
            for(int j = 0;j<this.nodeLabels[0].length;j++){
                this.terrainPanel.add(this.nodeLabels[i-1][j]);
            }
        }
    }

    /**
     * Step.4
     */
    public void refreshTerrainPanel(){
        this.terrainPanel.repaint();
        this.terrainPanel.revalidate();
    }


    public void setIntelliSlider(int height){
        this.intelligence.setMaximum(height);
        this.intelligence.setValue(height);
    }

    public void registerController(TerrainController terrainController){
        this.controller = terrainController;
    }

    private int[] getInputValue(){
        try {
            this.widthInput.commitEdit();
            this.heightInput.commitEdit();
        } catch ( java.text.ParseException e ) {
            System.out.println(e.toString());
        }
        int width = (Integer) this.widthInput.getValue();
        int height = (Integer) this.heightInput.getValue();
        return new int[]{width, height};
    }

    public static void main(String[] args) {
        TerrainView terrainView = new TerrainView();
        terrainView.setVisible(true);
    }

    public void resizeWindow(int x){
        if(x==1){
            this.mainPanel.remove(this.terrainPanel);
            sp.add(this.terrainPanel);
            sp.setViewportView(this.terrainPanel);
            this.mainPanel.add(sp,JSplitPane.RIGHT);
            this.mainPanel.setDividerLocation(1.0/3.0,WIDTH,HEIGHT);
        } else {
            if(this.mainPanel.getRightComponent()==sp){
                this.mainPanel.remove(sp);
                this.mainPanel.add(this.terrainPanel,JSplitPane.RIGHT);
                this.mainPanel.setDividerLocation(1.0/3.0,WIDTH,HEIGHT);
            }
        }
    }

    public void updateDifficulty(int cost){
        this.costs.setText(cost+"");
    }

    public void unselectable(int row){
        for(NodeLabel each : this.nodeLabels[row]){
            if(each.isSelectableColor()){
                each.unselectColor();
                each.setSelectable(false);
            } else {
                each.setSelectable(false);
            }
        }
    }

    public void setSelectable(int row,int col){
        this.nodeLabels[row][col].selectableColor();
        this.nodeLabels[row][col].setSelectable(true);
    }

    public void clearAutoLabel(ArrayList<Node> shortest){
        boolean flag = true;
        for(int i = 0;i<shortest.get(0).getRow()+1;i++){
            NodeLabel[] nls = this.nodeLabels[i];
            for (NodeLabel nl:nls){
                //To make sure this point is not in the ultimate route.
                BREAK:
                for(Node node : shortest){
                    while(node!=null){
                        if(node.getRow()==nl.getRow() && node.getCol()==nl.getCol()){
                            flag = false;
                            break BREAK;
                        }
                        node = node.getLast();
                    }
                }
                //To deHighlight those points not in the final route.
                if((nl.isAutoSelectedColor() || nl.isWatershedColor())&&flag){
                    nl.unselectColor();
                }
                flag = true;

            }
        }
    }

    public NodeLabel getLabel(int row, int col){
        return this.nodeLabels[row][col];
    }

    public void disablePrev(){
        this.prev.setEnabled(false);
    }

    public void enablePrev(){
        this.prev.setEnabled(true);
    }

    public void disableNext(){
        this.next.setEnabled(false);
    }

    public void enableNext(){
        this.next.setEnabled(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == this.generate){
            int[] size = this.getInputValue();
            this.controller.processGenerateEvent(size[1],size[0]);
        } else if(source == this.next){
            this.controller.processNextEvent(this.intelligence.getValue());
        } else if(source == this.prev){
            this.controller.processPrevEvent();
        }
    }
}
