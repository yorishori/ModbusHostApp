package main.java.mqtt.client;

import main.java.UserInterface.UserInterface;

public class Main {
    static double[] activePArr	 = new double[50];
    static double[] reactivePArr = new double[50];
    static double[] apparentPArr = new double[50];

    static double activeP	= 0;
    static double reactiveP	= 0;
    static double apparentP	= 0;

    static double activeI	= 0;
    static double activeE	= 0;
    static double reactiveE = 0;
    static double reactiveI = 0;

    static double voltage	= 0;
    static double current	= 0;
    static double pf		= 0;

    static double phase		= 0;
    static double frequency	= 0;

    static String piIP = "192.168.1.105";
    static MqttSubscriber mqttSub;

    static UserInterface UI;

    static int adqTime = 1000;		// In miliseconds


    public static void main(String[] args) {
        // Start UI: input IP Address
        mqttSub = new MqttSubscriber();
        UI = new UserInterface();
    }

    public static double[] addToArray(double arr[], double d){
        int n = arr.length - 1;
        // create a new array of size n
        double newarr[] = new double[arr.length];

        for (int i = 0; i < n; i++) {
            newarr[i] = arr[i + 1];
        }
        newarr[n] = d;

        return newarr;
    }

    // Convert miliseconds to a time formated string (hh:mm:ss:ms)
    public static String msTimeFormat(int ms_in) {
        String out = "";

        if(ms_in <= 0)return out;

        int ms = ms_in % 1000;
        if(ms > 0) out = ms + "ms";
        int s  = ms_in / 1000 % 60;
        if(s > 0) out = s + "s " + out;
        int m  = ms_in / (1000*60) % 60;
        if(m > 0) out = m + "m " + out;

        return out;
    }


    // Setters and Getters
    public static double getActiveP() {
        return activeP;
    }

    public static void setActiveP(double activeP) {
        Main.activeP = activeP;
    }

    public static double getReactiveP() {
        return reactiveP;
    }

    public static void setReactiveP(double reactiveP) {
        Main.reactiveP = reactiveP;
    }

    public static double getApparentP() {
        return apparentP;
    }

    public static void setApparentP(double apparentP) {
        Main.apparentP = apparentP;
    }

    public static double getActiveI() {
        return activeI;
    }

    public static void setActiveI(double activeI) {
        Main.activeI = activeI;
    }

    public static double getActiveE() {
        return activeE;
    }

    public static void setActiveE(double activeE) {
        Main.activeE = activeE;
    }

    public static double getReactiveE() {
        return reactiveE;
    }

    public static void setReactiveE(double reactiveE) {
        Main.reactiveE = reactiveE;
    }

    public static double getReactiveI() {
        return reactiveI;
    }

    public static void setReactiveI(double reactiveI) {
        Main.reactiveI = reactiveI;
    }

    public static double getVoltage() {
        return voltage;
    }

    public static void setVoltage(double voltage) {
        Main.voltage = voltage;
    }

    public static double getCurrent() {
        return current;
    }

    public static void setCurrent(double current) {
        Main.current = current;
    }

    public static double getPf() {
        return pf;
    }

    public static void setPf(double pf) {
        Main.pf = pf;
    }

    public static double getPhase() {
        return phase;
    }

    public static void setPhase(double phase) {
        Main.phase = phase;
    }

    public static double getFrequency() {
        return frequency;
    }

    public static void setFrequency(double frequency) {
        Main.frequency = frequency;
    }

    public static String getPiIP() {
        return piIP;
    }

    public static void setPiIP(String piIP) {
        Main.piIP = piIP;
    }

    public static double[] getActivePArr() {
        return activePArr;
    }

    public static void setActivePArr(double[] activePArr) {
        Main.activePArr = activePArr;
    }

    public static double[] getReactivePArr() {
        return reactivePArr;
    }

    public static void setReactivePArr(double[] reactivePArr) {
        Main.reactivePArr = reactivePArr;
    }

    public static double[] getApparentPArr() {
        return apparentPArr;
    }

    public static void setApparentPArr(double[] apparentPArr) {
        Main.apparentPArr = apparentPArr;
    }

    public static MqttSubscriber getMqttSub() {
        return mqttSub;
    }

    public static void setMqttSub(MqttSubscriber mqttSub) {
        Main.mqttSub = mqttSub;
    }

    public static UserInterface getUI() {
        return UI;
    }

    public static void setUI(UserInterface uI) {
        UI = uI;
    }

    public static int getAdqTime() {
        return adqTime;
    }

    public static void setAdqTime(int adqTime) {
        Main.adqTime = adqTime;
    }
}
