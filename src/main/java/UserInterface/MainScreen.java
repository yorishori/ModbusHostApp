package main.java.UserInterface;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

import main.java.mqtt.client.Main;

public class MainScreen implements ActionListener {
    // Frame
    JFrame frameMain;
    // Menu Bar
    JMenuBar 	mb;
    JMenu 		m1;
    JMenuItem 	adqTimeMenuItem;
    JMenuItem	reconnectMenuItem;
    JMenuItem	exitMenuItem;
    // Root Panels
    JPanel mainPanel;
    JPanel parPanel;
    JPanel graphPanel;
    // Labels
    MyLabel vLabel;
    MyLabel aLabel;
    MyLabel pfLabel;
    MyLabel fLabel;
    MyLabel pLabel;
    MyLabel iLabel;
    // Text Fields
    MyTextField vTF;
    MyTextField aTF;
    MyTextField pfTF;
    MyTextField fTF;
    MyTextField pTF;
    // Graphs
    XYChart xyChart;
    XChartPanel<XYChart> xyPanel;
    CategoryChart barChart;
    XChartPanel<CategoryChart> barPanel;
    // Adquisition Time
    JFrame adqFrame;
    JPanel adqPanel;
    JTextField adqTF;
    MySlider adqSlider;
    JButton adqButton;
    // Validation token to change screen
    PropertyChangeSupport token;

    public MainScreen() {
        initializeObjects();

        // Add action listeners
        adqTimeMenuItem.addActionListener(this);
        reconnectMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);

        // Add menu items to menu bar
        mb.add(m1);
        m1.add(adqTimeMenuItem);
        m1.add(reconnectMenuItem);
        m1.add(exitMenuItem);

        // Set Panel Layouts
        mainPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 50, 10));
        parPanel.setLayout(new GridLayout(6,2,10,25));
        graphPanel.setLayout(new BoxLayout(graphPanel,BoxLayout.Y_AXIS));

        parPanel.setPreferredSize(new Dimension(250,350));
        graphPanel.setPreferredSize(new Dimension(750,500));


        // Add items to parameters panel
        parPanel.add(vTF);
        parPanel.add(vLabel);
        parPanel.add(aTF);
        parPanel.add(aLabel);
        parPanel.add(pfTF);
        parPanel.add(pfLabel);
        parPanel.add(fTF);
        parPanel.add(fLabel);
        parPanel.add(pTF);
        parPanel.add(pLabel);

        // Create Graphs
        createGraphs();

        // Add graph to panel
        xyPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,Color.black,Color.white));
        barPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,Color.black,Color.white));
        graphPanel.add(xyPanel);
        graphPanel.add(barPanel);

        // Add root panels to main panel
        mainPanel.add(parPanel);
        mainPanel.add(graphPanel);

        updateParameters();

        // Set frame options and display it
        frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMain.setSize(1150,600);
        frameMain.add(mb, BorderLayout.NORTH);
        frameMain.add(mainPanel, BorderLayout.CENTER);
        frameMain.add(iLabel, BorderLayout.SOUTH);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frameMain.setLocation(dim.width/2-frameMain.getSize().width/2, dim.height/2-frameMain.getSize().height/2);
        frameMain.setResizable(false);
        frameMain.setVisible(true);
    }

    private void adqScreen() {

        // Add action listeners
        adqButton.addActionListener(this);
        adqTF.addActionListener(this);
        adqSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                adqTF.setText(String.valueOf(adqSlider.getValue()));
            }
        });

        // Set panel layout
        adqPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));

        // Styize objects
        adqSlider.setPreferredSize(new Dimension(300,50));
        adqSlider.setValue(Main.getAdqTime()/1000);

        // Styize objects
        adqTF.setFont(new Font("Arial", Font.PLAIN, 16));
        adqTF.setBackground(Color.white);
        adqTF.setForeground(Color.gray);
        adqTF.setColumns(10);

        // Add objects to panel
        adqPanel.add(adqSlider);
        adqPanel.add(adqTF);
        adqPanel.add(adqButton);

        // Configure window frame
        adqFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adqFrame.add(adqPanel);
        adqFrame.setSize(350,150);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        adqFrame.setLocation(dim.width/2-adqFrame.getSize().width/2, dim.height/2-adqFrame.getSize().height/2);
        adqFrame.setResizable(false);
        adqFrame.setVisible(true);

    }

    private void initializeObjects() {
        // Frame
        frameMain = new JFrame("Monitorización");

        // Menu bar
        mb  = new JMenuBar();
        m1  = new JMenu("Opciones");
        adqTimeMenuItem = new JMenuItem("Tiempo de Adquisición");
        reconnectMenuItem = new JMenuItem("Reconectar");
        exitMenuItem = new JMenuItem("Salir");

        // Panels
        mainPanel  = new JPanel();
        parPanel   = new JPanel();
        graphPanel = new JPanel();

        // Labels and Text fields
        vLabel  = new MyLabel("V");
        aLabel  = new MyLabel("A");
        pfLabel = new MyLabel("% (FP)");
        fLabel  = new MyLabel("Hz");
        pLabel  = new MyLabel("º (Fase)");
        iLabel  = new MyLabel("  Conectado a la Raspberry Pi W @  " + Main.getPiIP() +
                "    ::    Tiempo de Adquisición " + Main.msTimeFormat(Main.getAdqTime()));
        iLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        iLabel.setForeground(Color.BLACK);

        vTF  = new MyTextField();
        aTF  = new MyTextField();
        pfTF = new MyTextField();
        fTF  = new MyTextField();
        pTF  = new MyTextField();

        // Adquisition Time Panel
        adqFrame = new JFrame("Tiempo de Adquisición");
        adqPanel = new JPanel();
        adqSlider = new MySlider();
        adqTF = new JTextField();
        adqButton = new JButton("Aplicar");

        token = new PropertyChangeSupport(this);
    }

    private void createGraphs() {
        // X-Y Chart
        xyChart = new XYChartBuilder()
                .width(650).height(250)
                .title("Consumo de Potencia")
                .xAxisTitle("Adquisiciones").yAxisTitle("Potencia [W / VA]")
                .build();
        XYSeries a,r,s;
        a = xyChart.addSeries("Activa", null, Main.getActivePArr());
        a.setLineColor(XChartSeriesColors.BLUE);
        a.setMarker(SeriesMarkers.NONE);
        a.setLineStyle(SeriesLines.SOLID);
        r = xyChart.addSeries("Reactiva", null, Main.getReactivePArr());
        r.setLineColor(XChartSeriesColors.RED);
        r.setMarker(SeriesMarkers.NONE);
        r.setLineStyle(SeriesLines.SOLID);
        s = xyChart.addSeries("Aparente", null, Main.getApparentPArr());
        s.setLineColor(XChartSeriesColors.PURPLE);
        s.setMarker(SeriesMarkers.NONE);
        s.setLineStyle(SeriesLines.SOLID);

        xyChart.getStyler().setPlotBackgroundColor(new Color(255, 255, 255));
        xyChart.getStyler().setPlotGridLinesColor(new Color(140, 140, 140));
        xyChart.getStyler().setChartBackgroundColor(new Color(240, 240, 240));
        xyChart.getStyler().setLegendBackgroundColor(new Color(255, 255, 255));
        xyChart.getStyler().setChartFontColor(Color.black);
        xyChart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 16));
        xyChart.getStyler().setLegendFont(new Font("Arial", Font.PLAIN, 8));
        xyChart.getStyler().setLegendPosition(LegendPosition.OutsideE);
        xyChart.getStyler().setLegendSeriesLineLength(10);
        xyChart.getStyler().setAxisTitleFont(new Font("Arial", Font.PLAIN, 10));
        xyChart.getStyler().setAxisTickLabelsFont(new Font("Arial", Font.PLAIN, 8));
        xyChart.getStyler().setDecimalPattern("#0");
        xyChart.getStyler().setPlotGridVerticalLinesVisible(false);
        xyChart.getStyler().setPlotGridLinesStroke(new BasicStroke(0.5f));
        xyChart.getStyler().setXAxisTicksVisible(false);


        xyPanel = new XChartPanel<XYChart>(xyChart);

        // Bar chart
        barChart = new CategoryChartBuilder()
                .width(650).height(250)
                .title("Consumo de Energia")
                .yAxisTitle("Energia [kWh / kVAh]")
                .build();
        CategorySeries i, e;

        i = barChart.addSeries("Activa",
                new ArrayList<String>(Arrays.asList(new String[] { "Importada","Exportada" })),
                new ArrayList<Double>(Arrays.asList(new Double[] { Main.getActiveI(), Main.getActiveE()})));
        i.setLineColor(XChartSeriesColors.BLACK);
        i.setFillColor(XChartSeriesColors.BLUE);
        e = barChart.addSeries("Reactiva",
                new ArrayList<String>(Arrays.asList(new String[] { "Importada","Exportada" })),
                new ArrayList<Double>(Arrays.asList(new Double[] { Main.getReactiveI(), Main.getReactiveE()})));
        e.setLineColor(XChartSeriesColors.BLACK);
        e.setFillColor(XChartSeriesColors.RED);

        barChart.getStyler().setPlotBackgroundColor(new Color(255, 255, 255));
        barChart.getStyler().setPlotGridLinesColor(new Color(140, 140, 140));
        barChart.getStyler().setChartBackgroundColor(new Color(240, 240, 240));
        barChart.getStyler().setLegendBackgroundColor(new Color(255, 255, 255));
        barChart.getStyler().setChartFontColor(Color.black);
        barChart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 16));
        barChart.getStyler().setLegendFont(new Font("Arial", Font.PLAIN, 8));
        barChart.getStyler().setLegendPosition(LegendPosition.OutsideE);
        barChart.getStyler().setLegendSeriesLineLength(15);
        barChart.getStyler().setAxisTitleFont(new Font("Arial", Font.PLAIN, 10));
        barChart.getStyler().setAxisTickLabelsFont(new Font("Arial", Font.PLAIN, 8));
        barChart.getStyler().setDecimalPattern("#0.00");
        //barChart.getStyler().setHasAnnotations(true);
        barChart.getStyler().setPlotGridVerticalLinesVisible(false);
        barChart.getStyler().setPlotGridLinesStroke(new BasicStroke(0.5f));

        barPanel = new XChartPanel<CategoryChart>(barChart);
    }

    public void updateGraphs() {
        xyChart.updateXYSeries("Activa", null, Main.getActivePArr(), null);
        xyChart.updateXYSeries("Reactiva", null, Main.getReactivePArr(), null);
        xyChart.updateXYSeries("Aparente", null, Main.getApparentPArr(), null);
        xyPanel.repaint();

        barChart.updateCategorySeries("Activa",
                new ArrayList<String>(Arrays.asList(new String[] { "Importada","Exportada" })),
                new ArrayList<Double>(Arrays.asList(new Double[] { Main.getActiveI(), Main.getActiveE()})),
                null);
        barChart.updateCategorySeries("Reactiva",
                new ArrayList<String>(Arrays.asList(new String[] { "Importada","Exportada" })),
                new ArrayList<Double>(Arrays.asList(new Double[] { Main.getReactiveI(), Main.getReactiveE()})),
                null);
        barPanel.repaint();
    }

    public void updateParameters() {
        DecimalFormat df = new DecimalFormat("#0.00");
        vTF.setText(df.format(Main.getVoltage()));
        aTF.setText(df.format(Main.getCurrent()));
        pfTF.setText(df.format(Main.getPf()));
        fTF.setText(df.format(Main.getFrequency()));
        pTF.setText(df.format(Main.getPhase()));
        iLabel.setText("  Conectado a la Raspberry Pi W @  " + Main.getPiIP() +
                "    ::    Tiempo de Adquisición @ " + Main.msTimeFormat(Main.getAdqTime()));
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
        if(e.getSource() == exitMenuItem) {
            System.exit(0);
        }
        if(e.getSource() == adqTimeMenuItem) {
            adqScreen();
        }
        if(e.getSource() == reconnectMenuItem) {
            adqFrame.dispose();
            frameMain.dispose();
            Main.getMqttSub().disconectClient();
            updateScreen();
        }
        if(e.getSource() == adqButton) {
            Main.setAdqTime(adqSlider.getValue()*1000);
            try {
                Main.getMqttSub().sendMessage(String.valueOf(Main.getAdqTime()), "adqTime/");
                adqFrame.dispose();
                updateParameters();
            } catch (MqttException e1) {
                System.out.println("Failed to send message to MQTT server.");
                adqFrame.setTitle("Vuelva a Intentarlo");
                e1.printStackTrace();
            }
        }
        if(e.getSource() == adqTF) {
            try {
                adqSlider.setValue(Integer.parseInt(adqTF.getText()));
            } catch (NumberFormatException en) {
                adqTF.setText("Solo Numeros");
                adqTF.selectAll();
            }
        }
    }
}
