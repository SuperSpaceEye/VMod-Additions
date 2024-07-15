package net.spaceeye.vmod_additions.forge

import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.spaceeye.vmod_additions.VModAdditions
import net.spaceeye.vmod_additions.VModAdditions.init

@Mod(VModAdditions.MOD_ID)
class VModAdditionsForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(VModAdditions.MOD_ID, FMLJavaModLoadingContext.get().modEventBus)
        init()
    }
}