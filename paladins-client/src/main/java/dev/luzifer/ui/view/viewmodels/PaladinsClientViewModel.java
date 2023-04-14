package dev.luzifer.ui.view.viewmodels;

import dev.luzifer.Main;
import dev.luzifer.ui.view.ViewModel;

public class PaladinsClientViewModel implements ViewModel {

    public int count() {
        return Main.getWebClient().count();
    }
}
