package dev.luzifer.ui.view.views;

import dev.luzifer.Main;
import dev.luzifer.distribution.TaskForce1;
import dev.luzifer.paladins.PaladinsChampion;
import dev.luzifer.ui.view.View;
import dev.luzifer.ui.view.component.components.ChampionListComponent;
import dev.luzifer.ui.view.viewmodels.PaladinsClientViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PaladinsClientView extends View<PaladinsClientViewModel> {

    @FXML
    private VBox contentHolder;

    @FXML
    private Label countLabel;

    public PaladinsClientView(PaladinsClientViewModel viewModel) {
        super(viewModel);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countLabel.setText("Ranked Matches: 69420");

        ChampionListComponent championListComponent = setupChampionListComponent();
        setContent(championListComponent);
    }

    public void setContent(Pane node) {

        node.setMaxWidth(Double.MAX_VALUE);
        node.prefWidthProperty().bind(contentHolder.widthProperty());

        VBox.setVgrow(node, Priority.ALWAYS);

        contentHolder.getChildren().setAll(node);
    }

    private ChampionListComponent setupChampionListComponent() {
        ChampionListComponent championListComponent = new ChampionListComponent(
                mapLabel -> {},
                champLabel -> {}
        );
        championListComponent.setLoading(true);

        TaskForce1.ChainedTask chainedTask = new TaskForce1.ChainedTask(() -> Platform.runLater(() -> {
            championListComponent.setLoading(false);
            championListComponent.sortByName();
            championListComponent.setupFilterComponent();
        }));
        for(PaladinsChampion paladinsChampion : Main.getPaladinsChampionMapper().getChampions().values()) {
            chainedTask.addTask(() -> {
                Image image = new Image(paladinsChampion.getArtwork(), 64, 64, false, true);
                Platform.runLater(() -> championListComponent.addChampionLabel(new Label(paladinsChampion.getName(), new ImageView(image))));
            });
        }

        TaskForce1.order(chainedTask);
        return championListComponent;
    }
}
