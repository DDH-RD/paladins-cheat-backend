package dev.luzifer.ui.view.overlay;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class LoadingOverlay extends StackPane {

    private final Rectangle background;
    private final ProgressIndicator progressIndicator;
    private final Label loadingLabel;

    public LoadingOverlay() {
        background = new Rectangle(0, 0, Color.rgb(192, 192, 192, 0.4));
        progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle("-fx-progress-color: white;");
        progressIndicator.setPrefSize(50, 50);

        loadingLabel = new Label("Loading");
        loadingLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        loadingLabel.setFont(loadingLabel.getFont().font(20));

        guaranteeClickability();
        setupPositioning();
        setupLoadingWheel();

        getChildren().addAll(background, progressIndicator, loadingLabel);
    }

    private void guaranteeClickability() {
        setMouseTransparent(true);
        setBackground(null);
    }

    private void setupPositioning() {
        widthProperty().addListener((observableValue, oldWidth, newWidth) -> background.setWidth(newWidth.doubleValue()));
        heightProperty().addListener((observableValue, oldHeight, newHeight) -> background.setHeight(newHeight.doubleValue()));

        setAlignment(background, Pos.CENTER);
        setAlignment(progressIndicator, Pos.CENTER);
        setAlignment(loadingLabel, Pos.CENTER);
        loadingLabel.setPadding(new Insets(75, 0, 0, 0));
    }

    private void setupLoadingWheel() {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), progressIndicator);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.play();
    }
}
