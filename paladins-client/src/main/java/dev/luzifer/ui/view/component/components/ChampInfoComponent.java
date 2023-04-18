package dev.luzifer.ui.view.component.components;

import dev.luzifer.Main;
import dev.luzifer.distribution.TaskForce1;
import dev.luzifer.paladins.PaladinsChampion;
import dev.luzifer.ui.view.component.AbstractLoadableComponent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class ChampInfoComponent extends AbstractLoadableComponent implements Initializable {
    
    private final PaladinsChampion champion;
    
    
    @FXML
    private FlowPane bestCardsFlowPane;
    
    @FXML
    private FlowPane bestItemsFlowPane;
    
    @FXML
    private Label bestTalentNameLabel;
    
    @FXML
    private Circle bestTalentPictureCircle;
    
    @FXML
    private Label champNameLabel;
    
    @FXML
    private Circle champPictureCircle;
    
    public ChampInfoComponent(PaladinsChampion champion) {
        this.champion = champion;
        
        load();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        setLoading(true);
        
        champNameLabel.setText(champion.getName());
        
        TaskForce1.ChainedTask chainedTask = new TaskForce1.ChainedTask(() -> setLoading(false));
        chainedTask.addTask(() -> {
            Image image = new Image(champion.getArtwork());
            Platform.runLater(() -> champPictureCircle.setFill(pictureToPaint(image)));
        });
        chainedTask.addTask(() -> {
            Map<Integer, Integer> bestTalentsMap = Main.getWebClient().requestTalent(champion.getId());
            int bestTalentId = bestTalentsMap.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
            
            AtomicReference<PaladinsChampion.Talent> bestTalent = new AtomicReference<>();
            champion.getTalents().stream().filter(talent -> talent.getCard_id2() == bestTalentId)
                    .findFirst().ifPresent(bestTalent::set);
            
            Image image = new Image(bestTalent.get().getArtwork());
            Platform.runLater(() -> {
                bestTalentNameLabel.setText(bestTalent.get().getName());
                bestTalentPictureCircle.setFill(pictureToPaint(image));
            });
        });
        chainedTask.addTask(() -> {
            Map<Integer, Integer> bestCardsMap = Main.getWebClient().requestDeck(champion.getId());
            bestCardsMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(3).forEach(entry -> {
                AtomicReference<PaladinsChampion.Card> card = new AtomicReference<>();
                champion.getCards().stream().filter(c -> c.getCard_id2() == entry.getKey())
                        .findFirst().ifPresent(card::set);
                
                Image image = new Image(card.get().getArtwork());
                Platform.runLater(() -> bestCardsFlowPane.getChildren().add(new Circle(50, pictureToPaint(image))));
            });
        });
        
        TaskForce1.order(chainedTask);
    }
    
    @FXML
    void onBack(ActionEvent event) {
    
    }
    
    private Paint pictureToPaint(Image image) {
        return new ImagePattern(image);
    }
}
