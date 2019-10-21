package assignment_2.View;


import assignment_2.Controller.TerrainController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class TerrainView extends JFrame{
    private TerrainController controller;
    private LinkedList<LinkedList<JLabel>> options;

    /**
     * Components of TerrainGen settings
     */
    private JPanel settingPanel;
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
        this.options = new LinkedList<>();
        this.settingPanel = new JPanel();
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

        this.widthInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    public void updateTerrain(){
        
    }


    public void registerController(TerrainController terrainController){
        this.controller = terrainController;
    };
}
