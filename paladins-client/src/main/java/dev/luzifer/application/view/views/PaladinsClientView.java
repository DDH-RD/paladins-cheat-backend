package dev.luzifer.application.view.views;

import dev.luzifer.application.view.View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class PaladinsClientView extends View<PaladinsClientViewModel> {

    @FXML
    private VBox contentHolder;

    @FXML
    private Label gameCountLabel;

    @FXML
    private Label champCountLabel;

    public PaladinsClientView(PaladinsClientViewModel viewModel) {
        super(viewModel);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        CompletableFuture.supplyAsync(getViewModel()::getCount)
                .thenAccept(count -> Platform.runLater(() -> gameCountLabel.setText("Games: " + count)));

        CompletableFuture.supplyAsync(getViewModel()::getChampCount)
                .thenAccept(count -> Platform.runLater(() -> champCountLabel.setText("Champs: " + count)));

        Thread thread = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int count = getViewModel().getCount();
                int champCount = getViewModel().getChampCount();
                Platform.runLater(() -> gameCountLabel.setText("Games: " + count));
                Platform.runLater(() -> champCountLabel.setText("Champs: " + champCount));
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void setContent(Pane pane) {
        contentHolder.getChildren().setAll(pane);
    }
}
