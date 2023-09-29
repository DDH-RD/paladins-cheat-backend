package dev.luzifer.application.view.views;

import dev.luzifer.application.view.ViewModel;
import dev.luzifer.webclient.Webclient;

public class PaladinsClientViewModel implements ViewModel {

    private final Webclient webclient;

    public PaladinsClientViewModel(Webclient webclient) {
        this.webclient = webclient;
    }

    public int getCount() {
        return webclient.makeCountRequest();
    }

    public int getChampCount() {
        return webclient.makeCountChampsRequest();
    }
}
