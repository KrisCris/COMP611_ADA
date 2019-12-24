package assignment_4;

import assignment_4.Util.GraphicsUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class test {
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setSize(400,400);

        JPanel jp = new JPanel();
        jp.setSize(320,320);

        jf.setLayout(null);
        jf.add(jp);

        jp.setBounds(40,40,320,320);

        JButton jb = new JButton("save");
        jf.add(jb);

        jb.setBounds(40, 370, 320, 20);
        jp.setVisible(true);
        jf.setVisible(true);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicsUtil graphicsUtil = GraphicsUtil.getInstance();
                graphicsUtil.panelToGrayMatrix(jp);
            }
        });

        jp.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Graphics2D g = (Graphics2D) jp.getGraphics();
                g.setColor(Color.black);
                g.setStroke(new BasicStroke(10));
                g.drawLine(e.getX(),e.getY(),e.getX(),e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }
}
