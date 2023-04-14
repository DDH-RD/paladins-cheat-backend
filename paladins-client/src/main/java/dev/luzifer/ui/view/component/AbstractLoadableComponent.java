package dev.luzifer.ui.view.component;

import dev.luzifer.ui.view.overlay.LoadingOverlay;

public abstract class AbstractLoadableComponent extends AbstractComponent implements Loadable {

    private final LoadingOverlay loadingOverlay = new LoadingOverlay();

    protected AbstractLoadableComponent() {
        super();

        adjustLoadingOverlay();
        getChildren().add(loadingOverlay);
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
    
    private void adjustLoadingOverlay() {
        loadingOverlay.setVisible(false);

        loadingOverlay.prefWidthProperty().bind(widthProperty());
        loadingOverlay.prefHeightProperty().bind(heightProperty());
        
        loadingOverlay.translateXProperty().bind(widthProperty().subtract(loadingOverlay.widthProperty()).divide(2));
        loadingOverlay.translateYProperty().bind(heightProperty().subtract(loadingOverlay.heightProperty()).divide(2));
    }
}
