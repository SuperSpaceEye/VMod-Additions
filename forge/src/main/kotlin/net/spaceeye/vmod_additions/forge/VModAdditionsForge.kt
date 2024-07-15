package net.spaceeye.vmod_additions.forge;

import dev.architectury.platform.forge.EventBuses;
import net.spaceeye.vmod_additions.VModAdditions;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VModAdditions.MOD_ID)
public class VModAdditionsForge {
    public VModAdditionsForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(VModAdditions.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        VModAdditions.init();
    }
}