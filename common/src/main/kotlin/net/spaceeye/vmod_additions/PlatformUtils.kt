package net.spaceeye.vmod_additions

import dev.architectury.injectables.annotations.ExpectPlatform
import net.spaceeye.vmod_additions.sharedContainers.CommonEnergyTank
import net.spaceeye.vmod_additions.sharedContainers.CommonFluidTank

object PlatformUtils {
    @ExpectPlatform
    @JvmStatic
    fun getFluidTank(): CommonFluidTank = throw AssertionError()

    @ExpectPlatform
    @JvmStatic
    fun getEnergyTank(): CommonEnergyTank = throw AssertionError()
}