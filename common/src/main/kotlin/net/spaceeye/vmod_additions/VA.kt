package net.spaceeye.vmod_additions

import dev.architectury.platform.Platform
import net.spaceeye.vmod.rendering.RenderingTypes
import net.spaceeye.vmod_additions.renderers.TubeRenderer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

fun ILOG(s: String) = VA.logger.info(s)
fun WLOG(s: String) = VA.logger.warn(s)
fun DLOG(s: String) = VA.logger.debug(s)
fun ELOG(s: String) = VA.logger.error(s)

object VA {
    const val MOD_ID: String = "vmod_additions"

    val logger: Logger = LogManager.getLogger(MOD_ID)!!

    var createExists = false

    @JvmStatic
    fun init() {
        createExists = Platform.isModLoaded("create")
        VABlocks.register()
        VABlockEntities.register()
        VAItems.register()

        RenderingTypes.register(::TubeRenderer)
    }
}
