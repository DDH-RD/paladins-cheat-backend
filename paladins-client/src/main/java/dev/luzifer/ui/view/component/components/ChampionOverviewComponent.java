package dev.luzifer.ui.view.component.components;

import dev.luzifer.Main;
import dev.luzifer.distribution.TaskForce1;
import dev.luzifer.paladins.PaladinsChampion;
import dev.luzifer.ui.view.component.AbstractLoadableComponent;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.Map;

public class ChampionOverviewComponent extends AbstractLoadableComponent {

    private final TitledCustomFlowPane talentFlowPane = new TitledCustomFlowPane("Best Talent");
    private final TitledCustomFlowPane deckFlowPane = new TitledCustomFlowPane("Best Cards");
    private final TitledCustomFlowPane itemFlowPane = new TitledCustomFlowPane("Coming Soon...");

    private final int champId;

    public ChampionOverviewComponent(int champId) {
        this.champId = champId;

        VBox vBox = new VBox(talentFlowPane, new Separator(), deckFlowPane, new Separator(), itemFlowPane);
        getChildren().addAll(vBox);
    }

    public void loadContent() {
        setLoading(true);
        TaskForce1.ChainedTask chainedTask = new TaskForce1.ChainedTask(() -> setLoading(false));

        loadTalent(chainedTask);
        loadDeckCards(chainedTask);

        TaskForce1.order(chainedTask);
    }

    private void loadTalent(TaskForce1.ChainedTask chainedTask) {
        Map<Integer, Integer> talentMap = Main.getWebClient().requestTalent(champId);
        PaladinsChampion paladinsChampion = Main.getPaladinsChampionMapper().getChampion(champId);
        int bestTalentId = 0;
        int bestTalentValue = 0;
        for(Map.Entry<Integer, Integer> entry : talentMap.entrySet()) {
            int talentId = entry.getKey();
            int talentValue = entry.getValue();
            if(talentValue > bestTalentValue) {
                bestTalentId = talentId;
                bestTalentValue = talentValue;
            }
        }

        int finalBestTalentId = bestTalentId;
        chainedTask.addTask(() -> {
            PaladinsChampion.Talent talent = null;
            for(PaladinsChampion.Talent t : paladinsChampion.getTalents())
                if(t.getCard_id2() == finalBestTalentId) talent = t;
            if(talent == null) throw new IllegalStateException("Talent not found");

            Image image = new Image(talent.getArtwork(), 64, 64, false, true);
            Label label = new Label(talent.getName(), new ImageView(image));
            Platform.runLater(() -> talentFlowPane.add(label));
        });
    }

    private void loadDeckCards(TaskForce1.ChainedTask chainedTask) {
        Map<Integer, Integer> deckMap = Main.getWebClient().requestDeck(champId);
        PaladinsChampion paladinsChampion = Main.getPaladinsChampionMapper().getChampion(champId);

        chainedTask.addTask(() -> {
            for(Map.Entry<Integer, Integer> entry : deckMap.entrySet()) {
                int cardId = entry.getKey();
                for(PaladinsChampion.Card card : paladinsChampion.getCards()) {
                    if(card.getCard_id2() == cardId) {
                        Image image = new Image(card.getArtwork(), 64, 64, false, true);
                        Label label = new Label(card.getName() + "(" + entry.getValue() + ")", new ImageView(image));
                        Platform.runLater(() -> deckFlowPane.add(label));
                    }
                }
            }
        });
    }

    /**
     * A FlowPane with a title above which is pre-set centered and with a gap of 10px
     */
    private static class TitledCustomFlowPane extends VBox {

        private final FlowPane flowPane = new FlowPane();

        public TitledCustomFlowPane(String title) {
            flowPane.setHgap(10);
            flowPane.setVgap(10);
            flowPane.prefWrapLengthProperty().bind(widthProperty());

            Label titleLabel = new Label(title);
            titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            getChildren().addAll(titleLabel, flowPane);
        }

        public void add(Node node) {
            flowPane.getChildren().add(node);
        }
    }
}
