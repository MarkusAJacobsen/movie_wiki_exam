package no.ntnu.imt3281.movieExplorer;

import java.util.prefs.Preferences;

/**
 * PreferenceHandler holds a users preferences, in this instance is it only baseDirectory
 */
class PreferencesHandler {
    private static final String BASEDIRECTORY= "BASE_DIR";


    private Preferences root;

    // Singleton
    private static PreferencesHandler instance;

    /**
     * This function makes it possible to get
     * an instance of the object
     * @return Instance of PreferencesHandler
     */
    static PreferencesHandler getPreferenceInstance() {
        if (instance == null) {
            instance = new PreferencesHandler();
            return instance;
        }
        return instance;
    }

    private PreferencesHandler() {
        root = Preferences.userNodeForPackage(PreferencesHandler.class);
    }

    /**
     * Sets the base directory preference
     * @param basedirectory supplied absolute path
     */
    void setBasedirectory(String basedirectory) {
        this.root.put(BASEDIRECTORY, basedirectory);
    }

    /**
     * Gets saved base directory
     * @return basedirectory URL
     */
    String getBasedirectory() {
        return (root.get(BASEDIRECTORY, ""));
    }
}
