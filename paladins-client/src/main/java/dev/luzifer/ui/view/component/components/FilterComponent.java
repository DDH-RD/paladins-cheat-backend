package dev.luzifer.ui.view.component.components;

import dev.luzifer.ui.view.component.AbstractComponent;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterComponent extends AbstractComponent {

    private final Map<ListView<Label>, List<Label>> cache = new HashMap<>();
    private final TextField textField = new TextField();

    public FilterComponent() {
        textField.setPromptText("Filter for names");
        getChildren().add(new HBox(textField));
    }

    public void bind(ListView<Label> listView) {
        cache.put(listView, new ArrayList<>(listView.getItems()));
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            listView.getItems().clear();
            cache.get(listView).forEach(label -> {
                if(label.getText().toLowerCase().contains(newValue.toLowerCase())) {
                    listView.getItems().add(label);
                }
            });
        });
    }

    public StringProperty textProperty() {
        return textField.textProperty();
    }
}
