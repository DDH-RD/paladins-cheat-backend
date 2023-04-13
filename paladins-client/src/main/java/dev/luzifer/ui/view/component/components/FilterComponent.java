package dev.luzifer.ui.view.component.components;

import dev.luzifer.ui.view.component.AbstractComponent;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class FilterComponent extends AbstractComponent {

    private final TextField textField = new TextField();

    public FilterComponent() {
        textField.setPromptText("Filter for names");
        getChildren().add(new HBox(textField));
    }

    public StringProperty textProperty() {
        return textField.textProperty();
    }
}
