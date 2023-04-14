package dev.luzifer.ui.view.component;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractComponent extends Pane implements Initializable {

    protected AbstractComponent() {
        setupListener();
        // load();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Override this method to initialize your component
    }

    protected <T extends Node> T load() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setRoot(this);
            loader.setController(this);
            loader.load(getClass().getResource(getClass().getSimpleName() + ".fxml").openStream());
            return (T) this;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupListener() {
        getChildren().addListener((ListChangeListener<Node>) change -> {
            change.next();
            if(change.wasAdded()) {
                for(Node node : change.getAddedSubList()) {
                    if(node instanceof Region) {
                        Region region = (Region) node;
                        region.prefWidthProperty().bind(widthProperty());
                        region.prefHeightProperty().bind(heightProperty());
                    }
                }
            }
        });
    }
}
