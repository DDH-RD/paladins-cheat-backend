package dev.luzifer.ui.view.component;

import dev.luzifer.ui.view.overlay.LoadingOverlay;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public abstract class AbstractLoadableComponent extends AbstractComponent implements Loadable {

    private final LoadingOverlay loadingOverlay = new LoadingOverlay();

    protected AbstractLoadableComponent() {
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
}
