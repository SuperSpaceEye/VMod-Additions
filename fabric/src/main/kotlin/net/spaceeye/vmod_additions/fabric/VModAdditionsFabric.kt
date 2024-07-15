package net.spaceeye.vmod_additions.fabric

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.spaceeye.vmod_additions.VABlockEntities
import net.spaceeye.vmod_additions.VModAdditions.init

class VModAdditionsFabric : ModInitializer {
    override fun onInitialize() {
        init()

        FluidStorage.SIDED.registerForBlockEntity({tank, direction -> (tank.tank) as FabricFluidTank}, VABlockEntities.FLUID_PIPE.get())
    }
}