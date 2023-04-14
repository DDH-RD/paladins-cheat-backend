package dev.luzifer.ui.view.component;

import dev.luzifer.ui.view.overlay.LoadingOverlay;

public abstract class AbstractLoadableComponent extends AbstractComponent implements Loadable {

    private final LoadingOverlay loadingOverlay = new LoadingOverlay();

    protected AbstractLoadableComponent() {
        super();

        loadingOverlay.setVisible(false);
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
}
