package assignment_2.View;


import assignment_2.Controller.TerrainController;
import assignment_2.Model.Node;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class TerrainView extends JFrame implements ActionListener{
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 720;
    private static final int DEFAULT_SIZE = 10;
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

    private JSeparator divider;

    private JLabel intelliTag;
    private JLabel intelliValue;
    private JLabel intelliTag2;
    private JLabel intelliPercentage;
    private JSlider intelligence;
    private JButton prev;
    private JButton next;
    private JButton reset;

    /**
     * Initialized all static components in the view.
     */
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
        this.tag = new JLabel("CURRENT COST",JLabel.CENTER);
        this.costs = new JLabel("0",JLabel.CENTER);
        this.widthTag = new JLabel("WIDTH",JLabel.CENTER);
        this.heightTag = new JLabel("HEIGHT",JLabel.CENTER);

        this.widthInput = new JSpinner(new SpinnerNumberModel(DEFAULT_SIZE,1,99,1));
        this.heightInput = new JSpinner(new SpinnerNumberModel(DEFAULT_SIZE,1,99,1));
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

        this.intelliValue = new JLabel(DEFAULT_SIZE+"",JLabel.CENTER);
        this.intelliTag2 = new JLabel("Line(s). Approx.",JLabel.CENTER);
        this.intelliPercentage = new JLabel("100%",JLabel.CENTER);
        this.prev = new JButton("PREV");
        this.next = new JButton("NEXT");
        this.reset = new JButton("RESET");

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
        this.intelliTag.setFont(font);
        this.settingPanel.add(this.intelliTag);

        this.intelliValue.setBounds(30,270,90,50);
        this.intelliValue.setForeground(FOREGROUND);
        this.intelliValue.setFont(font);
        this.intelliTag2.setBounds(120,270,120,50);
        this.intelliTag2.setForeground(FOREGROUND);
        this.intelliPercentage.setBounds(240,270,90,50);
        this.intelliPercentage.setForeground(FOREGROUND);
        this.intelliPercentage.setFont(font);
        this.settingPanel.add(this.intelliValue);
        this.settingPanel.add(this.intelliTag2);
        this.settingPanel.add(this.intelliPercentage);

        this.intelligence.setBounds(20,310,320,40);
        this.intelligence.setToolTipText("To set how far this algorithm can see in each decision.");

        this.setIntelliSlider(DEFAULT_SIZE);
        this.intelligence.setForeground(FOREGROUND);
        this.settingPanel.add(this.intelligence);

        this.next.setBounds(20,360,320,35);
        this.prev.setBounds(20,405,320,35);
        this.reset.setBounds(20,450,320,35);
        this.next.setForeground(FOREGROUND);
        this.prev.setForeground(FOREGROUND);
        this.reset.setForeground(FOREGROUND);
        this.next.setToolTipText("Automatic decision-making based on the INTELLIGENCE.");
        this.prev.setToolTipText("To move one step back.\nOnly works when this decision is made manually.");
        this.reset.setToolTipText("Reset all the approaches made in this round.");
        this.settingPanel.add(next);
        this.settingPanel.add(prev);
        this.settingPanel.add(reset);

        /**
         * Set event listener.
         */
        this.generate.addActionListener(this);
        this.next.addActionListener(this);
        this.prev.addActionListener(this);
        this.reset.addActionListener(this);
        this.intelligence.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                int current = slider.getValue();
                int max = slider.getMaximum();
                double percentageD = 100.0*current/max;
                String percentage = "";
                if(percentageD<100 && percentageD>99){
                    percentage = "99%";
                } else {
                    percentage = (int)percentageD+"%";
                }
                TerrainView.this.updateIntelli(current,percentage);
            }
        });
    }

    /**
     * Way to draw a number matrix:
     *
     * Step.1
     * To generate a matrix contains labels that display the value of each node.
     *
     * @param nodes
     * Nodes contains the value generated by Terrain model.
     * @param width
     * The number of columns.
     * @param height
     * The number of rows.
     */
    public void generateTerrain(Node[][] nodes,int width, int height){
        this.nodeLabels = new NodeLabel[height][width];
        for(int i = 0;i<height;i++){
            for(int j = 0;j<width;j++){
                NodeLabel nl = new NodeLabel(i,j,nodes[i][j].getValue());
                this.nodeLabels[i][j] = nl;
                /**
                 * A mouse listener that controls how the label will react when mouse event triggered.
                 * Like the click event triggered by selecting a ideal node label.
                 * And other mouse motion events that changed how labels look.
                 */
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
                /**
                 * Set all the nodes in first row selectable.
                 */
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
     * To display these nodes to the view.
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
     * To refresh the view to make sure everything looks alright.
     */
    public void refreshTerrainPanel(){
        this.terrainPanel.repaint();
        this.terrainPanel.revalidate();
    }

    /**
     *
     * @param rows
     * How many rows each decision is based.
     * @param percentage
     * Just for look.
     */
    public void updateIntelli(int rows,String percentage){
        this.intelliValue.setText(rows+"");
        this.intelliPercentage.setText(percentage);
    }

    /**
     * This method changes the look of the intelligence JSlider.
     *
     * @param height
     * The total rows of this matrix.
     */
    public void setIntelliSlider(int height){
        this.intelligence.setSnapToTicks(true);
        this.intelligence.setPaintTicks(true);
        this.intelligence.setMinimum(1);
        this.intelligence.setMaximum(height);
        this.intelligence.setValue(height);
        if(height<=16){
            this.intelligence.setPaintLabels(true);
            this.intelligence.setMajorTickSpacing(1);
            this.intelligence.setMinorTickSpacing(0);
        } else {
            this.intelligence.setPaintLabels(false);
            this.intelligence.setMajorTickSpacing(5);
            this.intelligence.setMinorTickSpacing(1);
        }

    }

    /**
     * To bind the view with controller.
     * @param terrainController
     */
    public void registerController(TerrainController terrainController){
        this.controller = terrainController;
        this.controller.VCRegistered();//tell controller
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

    /**
     * To let this View able to display a monolithic matrix without letting each label shrinks heavily.
     *
     * @param x
     * A fixed value controls how this method performs.
     */
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

    /**
     * This method makes sure those label once being highlighted but wasn't chosen as the final route being deHighlighted, when intelligence lower than 100%.
     * In other words, this method ensures only the ultimate route will be highlighted.
     * @param shortest
     */
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

    /**
     * Every node will be reset.
     * This method is provided for someone who want to play with the same terrain multiple times.
     *
     * @param shortest
     * By tracing shortest nodes to the bottom, every highlighted label can be deHighlighted effortlessly.
     */
    public void resetRound(ArrayList<Node> shortest){
        this.costs.setText("0");
        for(Node each : shortest){
            int row = 0;
            int col = 0;
            if(each.getRow()<this.nodeLabels.length-1){
                col = each.getCol();
                row = each.getRow()+1;
                this.nodeLabels[row][col].unselectColor();
                this.nodeLabels[row][col].setSelectable(false);
                if(col-1>=0){
                    this.nodeLabels[row][col-1].unselectColor();
                    this.nodeLabels[row][col-1].setSelectable(false);
                }
                if(col+1<=this.nodeLabels[0].length-1){
                    this.nodeLabels[row][col+1].unselectColor();
                    this.nodeLabels[row][col+1].setSelectable(false);
                }
            }
            NodeLabel nl = null;
            while(each != null){
                System.out.println(each);
                row = each.getRow();
                col = each.getCol();
                nl = this.nodeLabels[row][col];
                nl.setSelectable(false);
                nl.unselectColor();
                each = each.getLast();
                System.out.println("last = "+each);
            }
        }
        for(NodeLabel nl : this.nodeLabels[0]){
            nl.setSelectable(true);
            nl.selectableColor();
        }
        this.disablePrev();
        this.enableNext();
    }

    public void initFirstRound(){
        int[] size = this.getInputValue();
        this.controller.processGenerateEvent(size[1],size[0]);
        this.setIntelliSlider(size[1]);
    }

    public NodeLabel getLabel(int row, int col){
        return this.nodeLabels[row][col];
    }

    public TerrainController getController() {
        return controller;
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

    public void disableReset(){
        this.reset.setEnabled(false);
    }

    public void enableReset(){
        this.reset.setEnabled(true);
    }

    /**
     * How these buttons works.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == this.generate){
            int[] size = this.getInputValue();
            this.controller.processGenerateEvent(size[1],size[0]);
            this.setIntelliSlider(size[1]);
        } else if(source == this.next){
            this.controller.processNextEvent(this.intelligence.getValue());
        } else if(source == this.prev){
            this.controller.processPrevEvent();
        } else if(source == this.reset){
            this.controller.processResetEvent();
        }
    }
}
