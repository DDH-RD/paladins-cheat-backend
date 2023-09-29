package dev.luzifer.application.view.views.component;

import dev.luzifer.application.util.FXMLUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerSearchComponent extends Pane implements Initializable {

    @FXML
    private ListView<Label> searchPlayerListView;

    @FXML
    private TextField searchPlayerTextField;

    public PlayerSearchComponent() {
        FXMLUtil.loadFXML(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
