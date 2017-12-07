package no.ntnu.imt3281.movieExplorer;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class detailedPaneController {
    @FXML
    private Text title;
    private JSON movieDetails;

    public detailedPaneController(long id) {
        movieDetails = Search.movie(id);
    }

    @FXML
    /**
     * Called when the object has been created and connected to the fxml file. All components defined in the fxml file is
     * ready and available.
     */
    public void initialize() {

    }
}
