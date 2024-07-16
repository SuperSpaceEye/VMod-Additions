package net.spaceeye.vmod_additions.fabric

import net.spaceeye.vmod_additions.sharedContainers.CommonEnergyTank
import net.spaceeye.vmod_additions.sharedContainers.CommonFluidTank

object PlatformUtilsImpl {
    @JvmStatic
    fun getFluidTank(): CommonFluidTank = FabricFluidTank()

    @JvmStatic
    fun getEnergyTank(): CommonEnergyTank = FabricEnergyTank()
}