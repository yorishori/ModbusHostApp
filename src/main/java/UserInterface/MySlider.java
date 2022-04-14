package main.java.UserInterface;

import java.awt.Font;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

public class MySlider extends LogarithmicJSlider{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MySlider() {
        this.setOrientation(JSlider.HORIZONTAL);
        this.setPaintTicks(true);
        this.setPaintTrack(true);
        this.setFont(new Font("Arial", Font.BOLD, 20));
        this.setMaximum(60*60); //60min * 60 s/min * 1000m/s
        this.setMinimum(1);
        this.setMajorTickSpacing(5*60*1000);
        Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put(1, new JLabel("1s"));
        labelTable.put(10, new JLabel("10s"));
        labelTable.put(60, new JLabel("1m"));
        labelTable.put(60*60, new JLabel("1h"));
        this.setLabelTable(labelTable);
        this.setPaintLabels(true);
    }
}
