package no.ntnu.imt3281.movieExplorer;



import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.GLOBAL_LOGGER_NAME;

/**
 * This class setsup all configuration for images
 * by returning correct url and size for wanted image and type
 */
class TheMovieDBConfiguration {
    private JSON configuration;
    private String base_url = null;
    private PreferencesHandler preferences = PreferencesHandler.getPreferenceInstance();
    private String delimiter = "/";
    private String extention = "jpg";
    private static final Logger LOGGER = Logger.getLogger(GLOBAL_LOGGER_NAME);

    /**
     * Constructor takes a JSON string and creates a JSON to
     * represent the available configurations
     * @param json configuration string from themoviedb
     */
    TheMovieDBConfiguration(String json) {
        configuration = new JSON(json);
        base_url = (String) configuration.get(0).get(3).getValue("base_url");
    }

    /**
     * Returns correct URL for BackDrop Image
     * @param query image to include in URL
     * @return URL for BackDropImage
     */
    String getBackdropURL(String query) {
        String result;
        result = base_url;
        int size = configuration.get(0).get(2).size();
        size = size-2;
        String imageSize = (String) configuration.get(0).get(2).get(size).getValue("");
        String absolutePath = preferences.getBasedirectory();

        Path path = Paths.get(absolutePath + delimiter + imageSize + delimiter + query);
        Path pathToFolder = Paths.get(absolutePath+delimiter+imageSize);
        if(path.toFile().exists()){
            return String.valueOf(path);
        } else {
            result = result.concat(imageSize+delimiter+query);
            return fetchAndSaveImage(path, result, pathToFolder);

        }
    }



    /**
     * Returns correct URL for Logo Image
     * @param query image to include in URL
     * @return URL for LogoImage
     */
    String getLogoURL(String query) {
        String result;
        result = base_url;
        int size = configuration.get(0).get(4).size();
        size = size-2;
        String imageSize = (String) configuration.get(0).get(4).get(size).getValue("");
        String absolutePath = preferences.getBasedirectory();

        Path path = Paths.get(absolutePath+delimiter+query);
        Path pathToFolder = Paths.get(absolutePath+delimiter+imageSize);
        if(path.toFile().exists()){
            return String.valueOf(path);
        } else {
            result = result.concat(imageSize+delimiter+query);
            return fetchAndSaveImage(path, result, pathToFolder);
        }
    }

    /**
     * Returns correct URL for Poster image
     * @param query image to include in URL
     * @return URL for Poster Image
     */
    String getPosterURL(String query) {
        String result;
        result = base_url;
        int size = configuration.get(0).get(0).size();
        size = size-2;
        String imageSize = (String) configuration.get(0).get(0).get(size).getValue("");
        String absolutePath = preferences.getBasedirectory();

        Path path = Paths.get(absolutePath+delimiter+imageSize+delimiter+query);
        Path pathToFolder = Paths.get(absolutePath+delimiter+imageSize);
        if(path.toFile().exists()){
            return String.valueOf(path);
        } else {
            result = result.concat(imageSize+"/"+query);
            return fetchAndSaveImage(path, result, pathToFolder);
        }
    }



    /**
     * Returns correct URL for Profile Image
     * @param query image to include in URL
     * @return URL for Profile Image
     */
    String getProfileURL(String query) {
        String result;
        result = base_url;
        int size = configuration.get(0).get(6).size();
        size = size-2;
        String imageSize = (String) configuration.get(0).get(6).get(size).getValue("");
        String absolutePath = preferences.getBasedirectory();

        Path path = Paths.get(absolutePath+delimiter+imageSize+delimiter+query);
        Path pathToFolder = Paths.get(absolutePath+delimiter+imageSize);
        if(path.toFile().exists()){
            return String.valueOf(path);
        } else {
            result = result.concat(imageSize+delimiter+query);
            return fetchAndSaveImage(path, result, pathToFolder);
        }
    }

    /**
     * Returns correct URL for Still Image
     * @param query image to include in URL
     * @return URL for Still Image
     */
    String getStillURL(String query) {
        String result;
        result = base_url;
        int size = configuration.get(0).get(5).size();
        size = size-2;
        String imageSize = (String) configuration.get(0).get(5).get(size).getValue("");
        String absolutePath = preferences.getBasedirectory();

        Path path = Paths.get(absolutePath+delimiter+imageSize+delimiter+query);
        Path pathToFolder = Paths.get(absolutePath+delimiter+imageSize);
        if(path.toFile().exists()){
            return String.valueOf(path);
        } else {
            result = result.concat(imageSize+"/"+query);
            return fetchAndSaveImage(path, result, pathToFolder);
        }
    }

    /**
     * Will fetch a image from the WEB
     * @param path Path to save the image
     * @param result Path to the URL of the image
     * @param pathToFolder Path to folder without image to check if folder exist
     * @return String of image path
     */
    private String fetchAndSaveImage(Path path, String result, Path pathToFolder) {
        folderExists(pathToFolder);
        //Conversion courtesy of https://stackoverflow.com/a/22972314/7036624
        BufferedImage imageFromURL = null;
        try {
            URL url = new URL(result);
            imageFromURL = ImageIO.read(url);
            saveImage(imageFromURL, String.valueOf(path));
        } catch (IOException e) {
            return result;
        }
        return String.valueOf(path);
    }

    /**
     * Saves the image on disk
     * @param imageFromURL BufferedImage
     * @param path Path to save the image
     */
    private void saveImage(BufferedImage imageFromURL, String path) {
        File file = new File(path);
        try {
            ImageIO.write(imageFromURL, extention, file);  // ignore returned boolean
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }

    private void folderExists(Path path) {
        if(!path.toFile().exists()) {
            path.toFile().mkdir();
        }
    }
}
