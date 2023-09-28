package dev.luzifer.application;

import dev.luzifer.Main;
import dev.luzifer.application.view.views.PaladinsClientView;
import dev.luzifer.application.view.views.PaladinsClientViewModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class PaladinsClient extends Application {

    @Override
    public void start(Stage stage) {
        ViewController viewController = new ViewController();
        PaladinsClientView view = new PaladinsClientView(new PaladinsClientViewModel(Main.getPaladinsApi()));
        view.setResizable(false);
        viewController.showView(view);
    }
}
