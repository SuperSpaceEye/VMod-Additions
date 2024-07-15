package net.spaceeye.vmod_additions.fabric

import net.spaceeye.vmod_additions.utils.CommonFluidTank

object PlatformUtilsImpl {
    @JvmStatic
    fun getFluidTank(): CommonFluidTank = FabricFluidTank()
}