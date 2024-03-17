package com.lynnlo;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class Controller {
	public Stage stage;

	// #region Keys
	public Button KeyR1;
	public Button KeyR2;
	public Button KeyR3;
	public Button KeyR4;
	public Button KeyR5;
	public Button KeyR6;
	public Button KeyR7;
	public Button KeyR8;
	public Button KeyR9;

	public Button KeySelect; // Selected key

	public Button[] KeyR = new Button[9];
	public KeyCode[] KeyBinds = new KeyCode[9];

	public int selectedKey = 0;
	// #endregion

	public CheckBox PortCheck;

	public Button Minimize;
	public Button Close;

	public TextField KeyCurrent;
	public Button ClearKey;
	public Button BindKey;
	public Pane Modal;

	public TextField KeyEnter;
	public KeyCode KeyEnterBind;
	public Button KeyEnterReset;
	public Button KeyEnterConfirm;

	public Boolean isBinding = false;

	public Robot robot;

	SerialPort mainPort;

	public int get_key(String key) {
		try {
			return Integer.parseInt(key);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public Controller(Stage stage) {
		this.stage = stage;
	}

	public void initialize() throws AWTException {
		System.out.println("Controller initialized");
		Button[] KeyR = { KeyR1, KeyR2, KeyR3, KeyR4, KeyR5, KeyR6, KeyR7, KeyR8, KeyR9 };

		for (int i = 0; i < KeyR.length; i++) {
			final int j = i;
			KeyR[i].setOnAction(e -> selectKey(j));
		}

		robot = new Robot();

		BindKey.setOnAction(e -> {
			Modal.setVisible(true);
			isBinding = true;

			stage.getScene().setOnKeyPressed(ke -> {
				KeyEnter.setText(ke.getCode().toString());
				KeyEnterBind = ke.getCode();
			});
		});

		Minimize.setOnAction(e -> stage.setIconified(true));

		KeyEnterReset.setOnAction(e -> {
			KeyEnter.setText("");
			KeyEnterBind = null;
		});

		KeyEnterConfirm.setOnAction(e -> {
			if (KeyEnterBind != null) {
				KeyBinds[selectedKey] = KeyEnterBind;
				KeyCurrent.setText(KeyEnterBind.toString());
				System.out.println("Key " + selectedKey + " bound to " + KeyEnterBind.toString());
			} else {
				System.out.println("Key " + selectedKey + " binding cleared");
				KeyBinds[selectedKey] = null;
				KeyCurrent.setText("");
			}

			KeyEnter.setText("");
			KeyEnterBind = null;
			Modal.setVisible(false);
			isBinding = false;

			stage.getScene().setOnKeyPressed(null);
		});

		Close.setOnAction(e -> close());
	}

	public void selectKey(int i) {
		System.out.println("Key " + i + " selected");
		KeySelect.setText("R" + (i + 1));
		selectedKey = i;
		KeyCurrent.setText(KeyBinds[i] == null ? "" : KeyBinds[i].toString());
	}

	public void minimize() {
		return;
	}

	public void clearKey() {
		System.out.println("Key " + selectedKey + " cleared");
		KeyBinds[selectedKey] = null;
		KeyCurrent.setText("");
	}

	public void close() {
		if (mainPort != null && mainPort.isOpen()) {
			System.out.println("Closing port...");
			mainPort.closePort();
		}
		System.exit(0);
		return;
	}

	public void connect() {
		mainPort = SerialPort.getCommPort("COM6");
		mainPort.setComPortParameters(9600, 8, 1, 0);
		mainPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

		if (mainPort.openPort()) {
			System.out.println("Port is open :)");
			PortCheck.setSelected(true);

			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					System.out.println("Shutting down...");
					mainPort.closePort();
				}
			}, "Shutdown-thread"));
		} else {
			System.out.println("Failed to open port :(");
			return;
		}

		mainPort.addDataListener(new SerialPortDataListener() {

			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}

			@Override
			public void serialEvent(SerialPortEvent event) {
				if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
					return;
				byte[] newData = new byte[mainPort.bytesAvailable()];
				int numRead = mainPort.readBytes(newData, newData.length);
				System.out.println("Received data of size: " + numRead);

				if (numRead > 0 && !isBinding) {
					var data = new String(newData);
					var d_type = data.substring(0, 1);
					var d_key = data.substring(1);
					System.out.println("Type: " + d_type);
					System.out.println("Data: " + d_key);

					try {
						int code = KeyBinds[get_key(d_key)].getCode();

						if (d_type.equals("P")) {
							robot.keyPress(code);
							System.out.println("Pressed: " + code);
						} else if (d_type.equals("R")) {
							robot.keyRelease(code);
							System.out.println("Released: " + code);
						}
					} catch (Exception e) {
						System.out.println("Key not found");
					}
				}
				System.out.println("Data received \n");
				return;
			}
		});
	}
}
