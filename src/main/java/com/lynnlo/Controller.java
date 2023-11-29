package com.lynnlo;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

public class Controller {
	public Button KeyR1;
	public Button KeyR2;
	public Button KeyR3;
	public Button KeyR4;
	public Button KeyR5;
	public Button KeyR6;
	public Button KeyR7;
	public Button KeyR8;
	public Button KeyR9;

	public Button KeySelect;

	public Button[] KeyR = new Button[9];
	public KeyCode[] KeyBinds = new KeyCode[9];
	public int selectedKey = 0;

	public void initialize() {
		System.out.println("Controller initialized");
		Button[] KeyR = { KeyR1, KeyR2, KeyR3, KeyR4, KeyR5, KeyR6, KeyR7, KeyR8, KeyR9 };

		for (int i = 0; i < KeyR.length; i++) {
			final int j = i;
			KeyR[i].setOnAction(e -> selectKey(j));
		}
	}

	public void buttonClicked() {
		System.out.println("Button clicked");
	}

	public void selectKey(int i) {
		System.out.println("Key " + i + " selected");
		KeySelect.setText("R" + (i + 1));
		selectedKey = i;
	}

	public void bindKey() {
		return;
	}

	public void clearKey() {
		System.out.println("Key " + selectedKey + " cleared");
	}
}
