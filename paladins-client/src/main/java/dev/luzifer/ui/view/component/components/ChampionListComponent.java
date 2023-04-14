package dev.luzifer.ui.view.component.components;

import dev.luzifer.ui.view.component.AbstractLoadableComponent;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Comparator;
import java.util.function.Consumer;

public class ChampionListComponent extends AbstractLoadableComponent {

    private final TabPane tabPane = new TabPane();
    private final FilterComponent filterComponent = new FilterComponent();

    private final ListView<Label> mapListView = new ListView<>();
    private final ListView<Label> championListView = new ListView<>();

    private final Consumer<Label> onChampionClick;
    private final Consumer<Label> onMapClick;

    public ChampionListComponent(Consumer<Label> onMapClick, Consumer<Label> onChampionClick) {
        this.onChampionClick = onChampionClick;
        this.onMapClick = onMapClick;

        this.filterComponent.setVisible(false);

        setupTabs();
        getChildren().addAll(tabPane, filterComponent);
    }

    @Override
    public void setLoading(boolean loading) {
        super.setLoading(loading);
        loadSpecific(tabPane);
    }

    public void sortByName() {
        mapListView.getItems().sort(Comparator.comparing(Labeled::getText));
        championListView.getItems().sort(Comparator.comparing(Labeled::getText));
    }

    public void addChampionLabel(Label label) {
        championListView.getItems().add(label);
        label.setOnMouseClicked(event -> onChampionClick.accept(label));
    }

    public void addMapLabel(Label label) {
        mapListView.getItems().add(label);
        label.setOnMouseClicked(event -> onMapClick.accept(label));
    }

    public void setupFilterComponent() {
        filterComponent.setVisible(true);
        filterComponent.prefHeightProperty().bind(tabPane.heightProperty());
        filterComponent.prefWidthProperty().bind(tabPane.widthProperty());
        filterComponent.translateXProperty().bind(prefWidthProperty().subtract(230));
        filterComponent.setTranslateY(2);

        filterComponent.bind(mapListView);
        filterComponent.bind(championListView);
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
