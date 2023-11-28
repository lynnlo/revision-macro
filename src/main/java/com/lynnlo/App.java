package com.lynnlo;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/RV1.fxml"));

        Scene scene = new Scene(root, 640, 400);

        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        /*
         * final SerialPort mainPort = SerialPort.getCommPort("COM6");
         * mainPort.setComPortParameters(9600, 8, 1, 0);
         * mainPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
         * 
         * if (mainPort.openPort()) {
         * System.out.println("Port is open :)");
         * } else {
         * System.out.println("Failed to open port :(");
         * return;
         * }
         * 
         * mainPort.addDataListener(new SerialPortDataListener() {
         * 
         * @Override
         * public int getListeningEvents() {
         * return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
         * }
         * 
         * @Override
         * public void serialEvent(SerialPortEvent event) {
         * 
         * if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
         * return;
         * }
         * 
         * final byte[] newData = new byte[mainPort.bytesAvailable()];
         * int numRead = mainPort.readBytes(newData, newData.length);
         * System.out.println("Read " + numRead + " bytes.");
         * 
         * if (numRead > 0) {
         * System.out.println("Data: " + new String(newData));
         * }
         * }
         * });
         */

    }
}
