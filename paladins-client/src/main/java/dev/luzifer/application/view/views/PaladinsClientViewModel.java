package dev.luzifer.application.view.views;

import dev.luzifer.application.view.ViewModel;
import dev.luzifer.webclient.Webclient;
import me.skiincraft.api.paladins.Paladins;

public class PaladinsClientViewModel implements ViewModel {

    private final Paladins paladinsApi;
    private final Webclient webclient;

    public PaladinsClientViewModel(Paladins paladinsApi, Webclient webclient) {
        this.paladinsApi = paladinsApi;
        this.webclient = webclient;
    }

    public int getCount() {
        return webclient.makeCountRequest();
    }

    public int getChampCount() {
        return webclient.makeCountChampsRequest();
    }
}
