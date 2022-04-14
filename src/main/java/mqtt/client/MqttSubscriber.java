package main.java.mqtt.client;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MqttSubscriber implements MqttCallback {
    MqttClient client;
    MqttConnectOptions connOpt;

    public MqttSubscriber() {}

    public void connectClient(String hostIP) throws MqttException {
        client = new MqttClient("tcp://" + hostIP, MqttAsyncClient.generateClientId());
        System.out.println("Setting up");
        connOpt = new MqttConnectOptions();
        connOpt.setAutomaticReconnect(true);
        connOpt.setCleanSession(true);
        connOpt.setConnectionTimeout(2);
        client.connect(connOpt);
        System.out.println("Connected");
        client.setCallback(this);
    }

    public void sendMessage(String msg, String topic) throws MqttPersistenceException, MqttException {
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(0);
        client.publish(topic, message);
    }

    public void  subscribeToTopic(String t) throws MqttException {
        client.subscribe(t);
    }

    public void disconectClient() {
        try {
            client.disconnect();
            client.close();
            System.out.println("Client Disconnected");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection Lost: " + cause.toString());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Message recieved from '" + topic + "': " + message);

        if(message.toString().contains("n") || message.toString().contains("N")) return;

        if(topic.contains("parameters/voltage")) Main.setVoltage(Double.parseDouble(message.toString()));
        if(topic.contains("parameters/current")) Main.setCurrent(Double.parseDouble(message.toString()));
        if(topic.contains("parameters/pf")) Main.setPf(Double.parseDouble(message.toString())*100.0);
        if(topic.contains("parameters/phase")) Main.setPhase(Double.parseDouble(message.toString()));
        if(topic.contains("parameters/frequency")) Main.setFrequency(Double.parseDouble(message.toString()));

        if(topic.contains("energy/import/active")) Main.setActiveI(Double.parseDouble(message.toString()));
        if(topic.contains("energy/import/reactive")) Main.setReactiveI(Double.parseDouble(message.toString()));
        if(topic.contains("energy/export/active")) Main.setActiveE(Double.parseDouble(message.toString()));
        if(topic.contains("energy/export/reactive")) Main.setReactiveE(Double.parseDouble(message.toString()));

        if(topic.contains("power/active")) {
            Main.setActiveP(Double.parseDouble(message.toString()));
            Main.setActivePArr(Main.addToArray(Main.getActivePArr(), Main.getActiveP()));
        }
        if(topic.contains("power/reactive")) {
            Main.setReactiveP(Double.parseDouble(message.toString()));
            Main.setReactivePArr(Main.addToArray(Main.getReactivePArr(), Main.getReactiveP()));
        }
        if(topic.contains("power/apparent")) {
            Main.setApparentP(Double.parseDouble(message.toString()));
            Main.setApparentPArr(Main.addToArray(Main.getApparentPArr(), Main.getApparentP()));
        }

        Main.getUI().updateUI();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}