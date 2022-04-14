package main.java.UserInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.paho.client.mqttv3.MqttException;

import main.java.mqtt.client.Main;

public class UserInterface implements PropertyChangeListener {
    // GUI Objects
    static StartScreen ss;
    static MainScreen ms;

    public UserInterface() {
        // Start with the IP Screen
        startScreen();
    }

    private void startScreen() {
        // Create GUI and add the token listener
        ss = new StartScreen();
        ss.addPropertyChangeListener(this);
    }

    private void mainScreen() {
        // Create Main app GUI
        ms = new MainScreen();
        try {
            Main.getMqttSub().subscribeToTopic("parameters/#");
            Main.getMqttSub().subscribeToTopic("energy/#");
            Main.getMqttSub().subscribeToTopic("power/#");
        } catch (MqttException e) {
            e.printStackTrace();
        }
        ms.addPropertyChangeListener(this);
    }

    public void updateUI() {
        // Update GUI elements and graph from Main App Gui
        ms.updateGraphs();
        ms.updateParameters();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // If token recieved, change to Main Screen GUI
        if(evt.getSource() == ss) {
            mainScreen();
        }
        if(evt.getSource() == ms) {
            startScreen();
        }
    }
}
