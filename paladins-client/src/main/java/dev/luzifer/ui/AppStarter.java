package dev.luzifer.ui;

import dev.luzifer.ui.view.ViewController;
import dev.luzifer.ui.view.viewmodels.PaladinsClientViewModel;
import dev.luzifer.ui.view.views.PaladinsClientView;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppStarter extends Application {

    @Override
    public void start(Stage stage) {
        ViewController viewController = new ViewController();
        viewController.showView(new PaladinsClientView(new PaladinsClientViewModel()));
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
