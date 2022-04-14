package main.java.UserInterface;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class MyTextField extends JTextField {

    public MyTextField() {
        Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,Color.LIGHT_GRAY,Color.BLACK);
        File font_file = new File("Fonts/DSEG7Classic-Bold.ttf");
        Font MyFont;
        try {
            MyFont = Font.createFont(Font.TRUETYPE_FONT, font_file);
            Font sizedFont = MyFont.deriveFont(20f);
            this.setFont(sizedFont);
        } catch (FontFormatException | IOException e) {
            // TODO Auto-generated catch block
            this.setFont(new Font("Arial", Font.PLAIN, 20));
            e.printStackTrace();
        }
        this.setBorder(b);
        this.setBackground(Color.white);
        this.setForeground(Color.black);
        this.setColumns(10);
        this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        this.setEditable(false);
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
