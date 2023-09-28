package dev.luzifer.application;

import dev.luzifer.Main;
import dev.luzifer.application.view.views.PaladinsClientView;
import dev.luzifer.application.view.views.PaladinsClientViewModel;
import dev.luzifer.application.view.views.component.PlayerSearchComponent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class PaladinsClient extends Application {

    @Override
    public void start(Stage stage) {
        ViewController viewController = new ViewController();
        PaladinsClientView view = new PaladinsClientView(
                new PaladinsClientViewModel(Main.getPaladinsApi(), Main.getWebclient()));

        Platform.runLater(() -> {
            view.setHeight(800);
            view.setWidth(1200);
            view.setResizable(false);
            view.setContent(new PlayerSearchComponent());
        });

        viewController.showView(view);
    }
}
