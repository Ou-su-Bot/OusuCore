package me.skiincraft.discord.core.view.controllers;

import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import me.skiincraft.discord.core.view.console.Console;
import me.skiincraft.discord.core.view.console.ConsoleListener;

public class ConsoleController implements Initializable {

    @FXML
    private TextArea consoleViewer;

    @FXML
    private TextField consoleTextField;

    @FXML
    private Button consoleClearButton;
    
    @FXML
    private Button consoleButton;

    public static Console console;
    public static PrintStream sys;
    @FXML
    private Pane consolePane;
    
	public void initialize(URL arg0, ResourceBundle arg1) {
		consoleViewer.setText("");
		Console console = new Console(consoleViewer);
		new Thread(()-> {
		sys = new PrintStream(console, true);
		System.setOut(sys);
		System.setErr(sys);
		
		System.err.flush();System.out.flush();
		}).run();
		ConsoleController.console = console;
	}
	
	
	public void sendCommandToConsole() {
		Platform.runLater(() -> {
			if (consoleTextField.getText().length() == 0)return;
			System.out.println("> " + consoleTextField.getText());
			executeCommand();
		});
	}
	
	public void clearConsole() {
		consoleViewer.setText("");
	}
	
	public void sendCommandEnterToConsole(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			Platform.runLater(() -> {
				if (consoleTextField.getText().length() == 0)return;
				System.out.println("> "+ consoleTextField.getText());
				executeCommand();
			});
		}
	}
	
	private void executeCommand() {
		Platform.runLater(()-> {
		ConsoleListener.executeCommand(consoleTextField.getText().split(" "));
		consoleTextField.setText("");
		});
	}
    
    

}
