package net.spaceeye.vmod_additions

import net.spaceeye.vmod.rendering.RenderingTypes
import net.spaceeye.vmod_additions.renderers.TubeRenderer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

fun ILOG(s: String) = VModAdditions.logger.info(s)
fun WLOG(s: String) = VModAdditions.logger.warn(s)
fun DLOG(s: String) = VModAdditions.logger.debug(s)
fun ELOG(s: String) = VModAdditions.logger.error(s)

object VModAdditions {
    const val MOD_ID: String = "vmod_additions"

    val logger: Logger = LogManager.getLogger(MOD_ID)!!

    @JvmStatic
    fun init() {
        VABlocks.register()
        VABlockEntities.register()
        VAItems.register()

        RenderingTypes.register(::TubeRenderer)
    }
}
