package no.ntnu.imt3281.movieExplorer;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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

    DetailedPaneController(long id) {
        movieDetails = Search.movie(id);

        String req;
        try {
            req = Unirest.get("https://api.themoviedb.org/3/configuration?api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
            config = new TheMovieDBConfiguration(req);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @FXML
    /**
     * Called when the object has been created and connected to the fxml file. All components defined in the fxml file is
     * ready and available.
     */
   public void initialize() {
       title.setText(movieDetails.get(3).getValue("title").toString());
       description.setText(movieDetails.get(12).getValue("overview").toString());

       String imageNamePoster = movieDetails.get(15).getValue("poster_path").toString();
       String imageURL = config.getPosterURL(imageNamePoster);

       //Conversion courtesy of https://stackoverflow.com/a/22972314/7036624
        BufferedImage imageFromURL = null;
        try {
            URL url = new URL(imageURL);
            imageFromURL = ImageIO.read(url);
            Image toFx = SwingFXUtils.toFXImage(imageFromURL, null);
            this.image.setImage(toFx);
        } catch (IOException e) {
            e.printStackTrace();
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
