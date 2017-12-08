package no.ntnu.imt3281.movieExplorer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class setsup all configuration for images
 * by returning correct url and size for wanted image and type
 */
public class TheMovieDBConfiguration {
    public JSON configuration;
    private String base_url = null;
    private PreferencesHandler preferences = PreferencesHandler.getPreferenceInstance();

    /**
     * Constructor takes a JSON string and creates a JSON to
     * represent the available configurations
     * @param json configuration string from themoviedb
     */
    public TheMovieDBConfiguration(String json) {
        configuration = new JSON(json);
        base_url = (String) configuration.get(0).get(3).getValue("base_url");
    }

    /**
     * Returns correct URL for BackDrop Image
     * @param query image to include in URL
     * @return URL for BackDropImage
     */
    public String getBackdropURL(String query) {
        String result;

        result = base_url;
        int size = configuration.get(0).get(2).size();
        size = size-2;
        result = result.concat((String) configuration.get(0).get(2).get(size).getValue(""));
        result = result.concat("/" + query);
        return result;
    }

    /**
     * Returns correct URL for Logo Image
     * @param query image to include in URL
     * @return URL for LogoImage
     */
    public String getLogoURL(String query) {
        String result;

        result = base_url;
        int size = configuration.get(0).get(4).size();
        size = size-2;
        result = result.concat((String) configuration.get(0).get(4).get(size).getValue(""));
        result = result.concat("/" + query);
        return result;
    }

    /**
     * Returns correct URL for Poster image
     * @param query image to include in URL
     * @return URL for Poster Image
     */
    public String getPosterURL(String query) {
        String result;
        result = base_url;
        int size = configuration.get(0).get(0).size();
        size = size-2;
        String imageSize = (String) configuration.get(0).get(0).get(size).getValue("");
        String absolutePath = preferences.getBasedirectory();

        Path path = Paths.get(absolutePath+"/"+imageSize+"/"+query);
        if(path.toFile().exists()){
            return String.valueOf(path);
        } else {
            result = result.concat(imageSize+"/"+query);
            //Conversion courtesy of https://stackoverflow.com/a/22972314/7036624
            BufferedImage imageFromURL = null;
            try {
                URL url = new URL(result);
                imageFromURL = ImageIO.read(url);
                saveImage(imageFromURL, String.valueOf(path));
                return String.valueOf(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    /**
     * Returns correct URL for Profile Image
     * @param query image to include in URL
     * @return URL for Profile Image
     */
    public String getProfileURL(String query) {
        String result;

        result = base_url;
        int size = configuration.get(0).get(6).size();
        size = size-2;
        result = result.concat((String) configuration.get(0).get(6).get(size).getValue(""));
        result = result.concat("/" + query);
        return result;
    }

    /**
     * Returns correct URL for Still Image
     * @param query image to include in URL
     * @return URL for Still Image
     */
    public String getStillURL(String query) {
        String result;

        result = base_url;
        int size = configuration.get(0).get(5).size();
        size = size-2;
        result = result.concat((String) configuration.get(0).get(5).get(size).getValue(""));
        result = result.concat("/" + query);
        return result;
    }

    private void saveImage(BufferedImage imageFromURL, String path) {
        File file = new File(path);
        BufferedImage image = imageFromURL;
        try {
            ImageIO.write(image, "jpg", file);  // ignore returned boolean
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
