package dev.luzifer.application.util;

import dev.luzifer.Main;
import javafx.fxml.FXMLLoader;

/**
 * A utility class for loading FXML files.
 */
public final class FXMLUtil {

    private static final String FXML_EXTENSION = ".fxml";

    /**
     * Loads the FXML file for the given controller.
     */
    public static void loadFXML(Object controller) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(controller.getClass().getResource(controller.getClass().getSimpleName() + FXML_EXTENSION));

            loader.setController(controller);
            loader.setRoot(controller);

            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FXMLUtil() {
    }
}
