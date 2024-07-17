package net.spaceeye.vmod_additions.forge

import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.spaceeye.vmod_additions.VA
import net.spaceeye.vmod_additions.VA.init

@Mod(VA.MOD_ID)
class VModAdditionsForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(VA.MOD_ID, FMLJavaModLoadingContext.get().modEventBus)
        init()
    }
}