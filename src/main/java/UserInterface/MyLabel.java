package main.java.UserInterface;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class MyLabel extends JLabel {

    public MyLabel(String t) {
        this.setFont(new Font("Arial", Font.BOLD, 20));
        this.setForeground(Color.black);
        this.setText(t);
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
