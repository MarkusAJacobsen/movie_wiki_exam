package no.ntnu.imt3281.movieExplorer;

import java.util.prefs.Preferences;

public class PreferencesHandler {
    private static final String BASEDIRECTORY= "BASE_DIR";


    private Preferences root;

    // Singleton
    private static PreferencesHandler instance;

    /**
     * This function makes it possible to get
     * an instance of the object
     * @return Instance of PreferencesHandler
     */
    public static PreferencesHandler getPreferenceInstance() {
        if (instance == null) {
            instance = new PreferencesHandler();
            return instance;
        }
        return instance;
    }

    private PreferencesHandler() {
        root = Preferences.userNodeForPackage(PreferencesHandler.class);
    }

    public void setBasedirectory(String basedirectory) {
        this.root.put(BASEDIRECTORY, basedirectory);
    }

    public String getBasedirectory() {
        return (root.get(BASEDIRECTORY, ""));
    }
}
