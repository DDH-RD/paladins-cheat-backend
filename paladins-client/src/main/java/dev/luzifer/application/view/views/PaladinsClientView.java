package dev.luzifer.application.view.views;

import dev.luzifer.application.view.View;
import dev.luzifer.application.view.views.component.PlayerSearchComponent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class PaladinsClientView extends View<PaladinsClientViewModel> {

    @FXML
    private MenuBar headerMenuBar;

    @FXML
    private GridPane contentHolder;

    @FXML
    private HBox footerHBox;

    public PaladinsClientView(PaladinsClientViewModel viewModel) {
        super(viewModel);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setContent(new PlayerSearchComponent());
    }

    private void setContent(Pane pane) {
        pane.prefWidthProperty().bind(contentHolder.widthProperty());
        pane.prefHeightProperty().bind(contentHolder.heightProperty());
        contentHolder.getChildren().setAll(pane);
    }
}
