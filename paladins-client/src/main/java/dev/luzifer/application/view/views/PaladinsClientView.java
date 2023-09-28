package dev.luzifer.application.view.views;

import dev.luzifer.application.view.View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PaladinsClientView extends View<PaladinsClientViewModel> {

    @FXML
    private VBox contentHolder;

    @FXML
    private Label gameCount;

    public PaladinsClientView(PaladinsClientViewModel viewModel) {
        super(viewModel);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        Platform.runLater(() -> {
            System.out.println(contentHolder.getHeight());
            System.out.println(contentHolder.getWidth());
        });
    }

    public void setContent(Pane pane) {
        contentHolder.getChildren().setAll(pane);
    }
}
