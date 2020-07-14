package me.skiincraft.discord.core.view.console;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Console extends OutputStream {

	private TextArea textArea;
	
	public Console(TextArea textArea) {
		this.textArea = textArea;
	}
	
	public void write(int arg0) throws IOException {
		Platform.runLater(() -> {
			textArea.appendText(String.valueOf((char) arg0));
		});
	}

}
