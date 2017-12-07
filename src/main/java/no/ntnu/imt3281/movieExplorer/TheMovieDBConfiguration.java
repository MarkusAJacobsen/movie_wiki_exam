package no.ntnu.imt3281.movieExplorer;

/**
 * This class setsup all configuration for images
 * by returning correct url and size for wanted image and type
 */
public class TheMovieDBConfiguration {
    public JSON configuration;
    private String base_url = null;

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
        result = result.concat((String) configuration.get(0).get(0).get(size).getValue(""));
        result = result.concat("/" + query);
        return result;
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
}
