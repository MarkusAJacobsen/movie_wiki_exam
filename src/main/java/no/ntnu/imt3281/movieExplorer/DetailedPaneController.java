package no.ntnu.imt3281.movieExplorer;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class DetailedPaneController {
    @FXML
    private Text title;
    @FXML
    private TextArea description;

    private JSON movieDetails;

    DetailedPaneController(long id) {
        movieDetails = Search.movie(id);
    }

    @FXML
    /**
     * Called when the object has been created and connected to the fxml file. All components defined in the fxml file is
     * ready and available.
     */
   public void initialize() {
       title.setText(movieDetails.get(3).getValue("title").toString());
       description.setText(movieDetails.get(12).getValue("overview").toString());
    }
}
