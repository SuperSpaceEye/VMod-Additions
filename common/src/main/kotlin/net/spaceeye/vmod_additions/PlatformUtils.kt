package net.spaceeye.vmod_additions

import dev.architectury.injectables.annotations.ExpectPlatform
import net.spaceeye.vmod_additions.utils.CommonFluidTank

object PlatformUtils {
    @ExpectPlatform
    @JvmStatic
    fun getFluidTank(): CommonFluidTank = throw AssertionError()
}