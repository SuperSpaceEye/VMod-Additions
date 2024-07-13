package net.spaceeye.vmod_additions.fabric;

import net.spaceeye.vmod_additions.VModAdditions;
import net.fabricmc.api.ModInitializer;

public class VModAdditionsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        VModAdditions.init();
    }
}