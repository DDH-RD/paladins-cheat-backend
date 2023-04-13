package dev.luzifer.ui.view.component.components;

import dev.luzifer.ui.view.component.AbstractComponent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.function.Consumer;

public class ChampionListComponent extends AbstractComponent {

    private final TabPane tabPane = new TabPane();

    private final ListView<Label> mapListView = new ListView<>();
    private final ListView<Label> championListView = new ListView<>();

    private final Consumer<Label> onChampionClick;
    private final Consumer<Label> onMapClick;

    public ChampionListComponent(Consumer<Label> onMapClick, Consumer<Label> onChampionClick) {
        this.onChampionClick = onChampionClick;
        this.onMapClick = onMapClick;

        FilterComponent filterComponent = new FilterComponent();
        filterComponent.prefHeightProperty().bind(tabPane.heightProperty());
        filterComponent.prefWidthProperty().bind(tabPane.widthProperty());
        filterComponent.translateXProperty().bind(prefWidthProperty().subtract(230));
        filterComponent.setTranslateY(2);

        filterComponent.textProperty().addListener((observable, oldValue, newValue) -> {
            championListView.getItems().forEach(label ->
                    label.setVisible(label.getText().toLowerCase().contains(newValue.toLowerCase())));
            mapListView.getItems().forEach(label ->
                    label.setVisible(label.getText().toLowerCase().contains(newValue.toLowerCase())));
        });

        setupTabs();
        getChildren().addAll(tabPane, filterComponent);
    }

    @Override
    public void setLoading(boolean loading) {
        super.setLoading(loading);
        loadSpecific(tabPane);
    }

    public void addChampionLabel(Label label) {
        championListView.getItems().add(label);
        label.setOnMouseClicked(event -> onChampionClick.accept(label));
    }

    public void addMapLabel(Label label) {
        mapListView.getItems().add(label);
        label.setOnMouseClicked(event -> onMapClick.accept(label));
    }

    private void setupTabs() {
        Tab mapsTab = new Tab("Maps");
        Tab championsTab = new Tab("Champions");

        mapsTab.setClosable(false);
        championsTab.setClosable(false);

        mapsTab.setContent(mapListView);
        championsTab.setContent(championListView);

        tabPane.getTabs().addAll(mapsTab, championsTab);
    }
}
