package me.skiincraft.discord.core.view;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import me.skiincraft.discord.core.view.console.VanillaCommands;
import me.skiincraft.discord.core.view.editor.ViewContent;

public class OusuViewer extends Application {
	
	public static final String ousulogo = OusuViewer.class.getResource("ousu-logo128x.png").toString();
	public static final String ousuviewerfxml = OusuViewer.class.getResource("fx/OusuViewerFXML.fxml").toString();
	
	public static ViewContent viewerContent;
	
	public static String arguments;
	
	private static Runnable runafter;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("fx/OusuViewerFXML.fxml"));
			primaryStage.setScene(new Scene(root, 600, 400));
			root.getStylesheets().add(getClass().getResource("css/tabpane.css").toString());
			primaryStage.setTitle("OusuViewer");
			primaryStage.getIcons().add(new Image(ousulogo));
			primaryStage.setOnCloseRequest(alertEvent(primaryStage));
			primaryStage.setResizable(false);
			primaryStage.show();
			VanillaCommands.registerVanillaCommands();
			Platform.runLater(runafter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private EventHandler<WindowEvent> alertEvent(Stage primaryStage){
		return new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent t) {
		    	Alert alert = new Alert(AlertType.WARNING);
		    	alert.setTitle("OusuCore Warning");
		    	alert.setContentText("Voce tem certeza que quer fechar esta aplicação?"
		    			+ "\n" + "Ainda tem plugin(s)/bot(s) rodando...");
		    	alert.getButtonTypes().add(ButtonType.CANCEL);
		    	if (alert.showAndWait().get() == ButtonType.OK) {
			        Platform.exit();
			        System.exit(0);
		    	} else {
		    		t.consume();
		    	}
		    }
		};
	}

	public static void start(String[] args, Runnable runafterstart) {
		runafter = runafterstart;
		launch(args);
	}
}

