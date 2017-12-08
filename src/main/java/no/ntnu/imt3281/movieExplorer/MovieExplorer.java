package no.ntnu.imt3281.movieExplorer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main of the application, launches the main stage
 */
public class MovieExplorer extends Application {
	/**
	 * Starts up primary stage and also checks preferences so no nasty errors pops up
	 * @param primaryStage Primary start view
	 * @throws Exception Error in creating the view
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Movie explorer");
	       BorderPane gui = FXMLLoader.load(getClass().getResource("GUI.fxml"));
	       Scene myScene = new Scene(gui);
	       primaryStage.setScene(myScene);
	       primaryStage.show();

	       PreferencesHandler preferences = PreferencesHandler.getPreferenceInstance();
	       if(Objects.equals(preferences.getBasedirectory(), "BASE_DIR")) {
	       		preferences.setBasedirectory(System.getProperty("user.dir"));
		   }
	}

    /**
     * Main
     * @param args
     */
	public static void main(String[] args) {
		launch(args);
	}
}
