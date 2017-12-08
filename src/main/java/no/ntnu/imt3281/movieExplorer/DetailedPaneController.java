package no.ntnu.imt3281.movieExplorer;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.GLOBAL_LOGGER_NAME;

/**
 * The detailedPaneController will keep all variables related
 * to the detailedPane correct
 */
public class DetailedPaneController {
    @FXML
    private Text title;
    @FXML
    private TextArea description;
    @FXML
    private ImageView image;
    @FXML
    private FlowPane genresBox;

    private JSON movieDetails;
    private TheMovieDBConfiguration config;
    private static final Logger LOGGER = Logger.getLogger(GLOBAL_LOGGER_NAME);

    /**
     * Constructor for detailedPane
     * @param id Movie ID to fill in the Pane with
     */
    DetailedPaneController(long id) {
        movieDetails = Search.movie(id);

        String req;
        try {
            req = Unirest.get("https://api.themoviedb.org/3/configuration?api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
            config = new TheMovieDBConfiguration(req);
        } catch (UnirestException e) {
           LOGGER.log(Level.SEVERE, e.toString());
        }
    }


    /**
     * Called when the object has been created and connected to the fxml file. All components defined in the fxml file is
     * ready and available. Set all fields related to the detailedPane from movieDetails
     */
    @FXML
   public void initialize() {
       title.setText(movieDetails.get(3).getValue("title").toString());
       description.setText(movieDetails.get(12).getValue("overview").toString());

       String imageNamePoster = movieDetails.get(15).getValue("poster_path").toString();
       String imageURL = config.getPosterURL(imageNamePoster);

       //Conversion courtesy of https://stackoverflow.com/a/22972314/7036624
        BufferedImage imageFromURL = null;
        try {
            imageFromURL = ImageIO.read(new File(imageURL));
            Image toFx = SwingFXUtils.toFXImage(imageFromURL, null);
            this.image.setImage(toFx);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }

        JSON genresList = movieDetails.get(6);
        for(int i = 0; i < genresList.size(); i++) {
            if(genresList.get(i).get(0).getValue("name") != null) {
                Text genre = new Text(genresList.get(i).get(0).getValue("name").toString());
                genresBox.getChildren().add(genre);
            }
        }

    }
}
