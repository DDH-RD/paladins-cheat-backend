package dev.luzifer.application.view.views;

import dev.luzifer.application.view.ViewModel;
import me.skiincraft.api.paladins.Paladins;

public class PaladinsClientViewModel implements ViewModel {

    private final Paladins paladinsApi;

    public PaladinsClientViewModel(Paladins paladinsApi) {
        this.paladinsApi = paladinsApi;
    }
}
