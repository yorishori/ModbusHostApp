package main.java.UserInterface;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.MqttException;

import main.java.mqtt.client.Main;


public class StartScreen implements ActionListener {
    // IP Settings Frame objects
    JFrame frameIP;
    JPanel panelIP;
    JLabel labelIP;
    JLabel labelErr;
    JTextField tfIP;
    JButton buttonIP;
    // Validation token to change screen
    PropertyChangeSupport token;

    public StartScreen() {
        initializeObjects();

        // Add action listeners
        buttonIP.addActionListener(this);
        tfIP.addActionListener(this);

        // Set panel layout
        panelIP.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));

        // Styize objects
        tfIP.setFont(new Font("Arial", Font.PLAIN, 16));
        tfIP.setBackground(Color.white);
        tfIP.setForeground(Color.gray);
        tfIP.setColumns(10);

        labelIP.setFont(new Font("Arial", Font.PLAIN, 16));
        labelIP.setForeground(Color.DARK_GRAY);

        labelErr.setFont(new Font("Arial", Font.PLAIN, 10));
        labelErr.setForeground(Color.red);
        labelErr.setVisible(false);


        // Add objects to panel
        panelIP.add(labelIP);
        panelIP.add(tfIP);
        panelIP.add(buttonIP);
        panelIP.add(labelErr);

        // Configure window frame
        frameIP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameIP.add(panelIP);
        frameIP.setSize(350,150);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frameIP.setLocation(dim.width/2-frameIP.getSize().width/2, dim.height/2-frameIP.getSize().height/2);
        frameIP.setResizable(false);
        frameIP.setVisible(true);
    }

    private void initializeObjects(){
        frameIP = new JFrame("Cliente MQTT");
        panelIP = new JPanel();
        labelIP = new JLabel("Introduzca la dirección IP de la Raspberry Pi:");
        labelErr = new JLabel("Error al conectar. Intente otra vez o con una IP distinta.");
        tfIP = new JTextField(Main.getPiIP(),10);
        buttonIP = new JButton("Conectar");
        token = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        token.addPropertyChangeListener(pcl);
    }

    public void updateScreen() {
        // Send update token to User interface object
        token.firePropertyChange("screen", "1", "0");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == tfIP || e.getSource() == buttonIP) {
            // Disable frame
            frameIP.setEnabled(false);
            frameIP.setTitle("Connectando...");
            // Update IP
            Main.setPiIP(tfIP.getText().replace(" ", ""));
            // Check if address is valid
            try {
                Main.getMqttSub().connectClient(Main.getPiIP());
                // Close current frame
                frameIP.dispose();
                // Send token to open main application
                updateScreen();

                Main.getMqttSub().sendMessage(String.valueOf(Main.getAdqTime()), "adqTime/");
            } catch (MqttException e1) {
                // Notify is IP is invalid
                labelErr.setVisible(true);
                e1.printStackTrace();
                // Enable frame
                frameIP.setEnabled(true);
                frameIP.setTitle("Cliente MQTT");
            }
        }
    }
}
