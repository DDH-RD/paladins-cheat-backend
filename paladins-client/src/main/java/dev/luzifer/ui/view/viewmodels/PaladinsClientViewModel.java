package dev.luzifer.ui.view.viewmodels;

import dev.luzifer.Main;
import dev.luzifer.ui.view.ViewModel;

import java.util.Map;

public class PaladinsClientViewModel implements ViewModel {

    public int count() {
        return Main.getWebClient().count();
    }

    public Map<Integer, Integer> requestDeck(int championId) {
        return Main.getWebClient().requestDeck(championId);
    }

    public Map<Integer, Integer> requestTalent(int championId) {
        return Main.getWebClient().requestTalent(championId);
    }
}
