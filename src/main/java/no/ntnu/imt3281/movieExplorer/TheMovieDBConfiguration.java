package no.ntnu.imt3281.movieExplorer;

/**
 * 
 */
public class TheMovieDBConfiguration {
    public JSON configuration;
    private String base_url = null;

    public TheMovieDBConfiguration(String json) {
        configuration = new JSON(json);
        base_url = (String) configuration.get(0).get(3).getValue("base_url");
    }

    public String getBackdropURL(String query) {
        String result;

        result = base_url;
        int size = configuration.get(0).get(2).size();
        size = size-2;
        result = result.concat((String) configuration.get(0).get(2).get(size).getValue(""));
        result = result.concat("/" + query);
        return result;
    }

    public String getLogoURL(String query) {
        String result;

        result = base_url;
        int size = configuration.get(0).get(4).size();
        size = size-2;
        result = result.concat((String) configuration.get(0).get(4).get(size).getValue(""));
        result = result.concat("/" + query);
        return result;
    }

    public String getPosterURL(String query) {
        String result;

        result = base_url;
        int size = configuration.get(0).get(0).size();
        size = size-2;
        result = result.concat((String) configuration.get(0).get(0).get(size).getValue(""));
        result = result.concat("/" + query);
        return result;
    }

    public String getProfileURL(String query) {
        String result;

        result = base_url;
        int size = configuration.get(0).get(6).size();
        size = size-2;
        result = result.concat((String) configuration.get(0).get(6).get(size).getValue(""));
        result = result.concat("/" + query);
        return result;
    }

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
