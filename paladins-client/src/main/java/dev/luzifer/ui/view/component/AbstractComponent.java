package dev.luzifer.ui.view.component;

import dev.luzifer.ui.view.overlay.LoadingOverlay;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public abstract class AbstractComponent extends Pane implements LoadableComponent {

    private final LoadingOverlay loadingOverlay = new LoadingOverlay();

    protected AbstractComponent() {
        getChildren().add(loadingOverlay);
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
        loadingOverlay.setVisible(false);
    }

    @Override
    public void setLoading(boolean loading) {
        loadingOverlay.setVisible(loading);
        loadingOverlay.toFront();
    }

    @Override
    public boolean isLoading() {
        return loadingOverlay.isVisible();
    }

    protected void loadSpecific(Region node) {
        loadingOverlay.bind(node);
    }
}
