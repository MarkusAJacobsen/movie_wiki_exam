package no.ntnu.imt3281.movieExplorer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MovieExplorer extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Movie explorer");
	       HBox gui = (HBox)FXMLLoader.load(getClass().getResource("GUI.fxml"));
	       Scene myScene = new Scene(gui);
	       primaryStage.setScene(myScene);
	       primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
