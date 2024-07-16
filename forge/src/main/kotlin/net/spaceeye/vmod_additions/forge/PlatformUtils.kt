package net.spaceeye.vmod_additions.forge

import net.spaceeye.vmod_additions.sharedContainers.CommonEnergyTank
import net.spaceeye.vmod_additions.sharedContainers.CommonFluidTank

object PlatformUtilsImpl {
    @JvmStatic
    fun getFluidTank(): CommonFluidTank = ForgeFluidTank()

    @JvmStatic
    fun getEnergyTank(): CommonEnergyTank = ForgeEnergyTank()
}