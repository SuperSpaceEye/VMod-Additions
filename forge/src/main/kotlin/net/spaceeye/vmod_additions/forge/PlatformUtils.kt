package net.spaceeye.vmod_additions.forge

import net.spaceeye.vmod_additions.utils.CommonFluidTank

object PlatformUtilsImpl {
    @JvmStatic
    fun getFluidTank(): CommonFluidTank = ForgeFluidTank()
}