package net.spaceeye.vmod_additions.items

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.spaceeye.vmod_additions.Linkable

class Linker(properties: Properties): Item(properties) {
    var pos1: BlockPos? = null

    override fun useOn(useOnContext: UseOnContext): InteractionResult {
        val level = useOnContext.level
        if (level !is ServerLevel) { return super.useOn(useOnContext) }
        val player = useOnContext.player ?: return super.useOn(useOnContext)

        val pos = useOnContext.clickedPos
        val state = level.getBlockState(pos)
        val block = state.block

        if (player.isShiftKeyDown) {
            pos1 = null
            if (!state.isAir && block is Linkable) { block.unlink(level, pos) }
            return super.useOn(useOnContext)
        }

        if (state.isAir || block !is Linkable) {return super.useOn(useOnContext)}

        if (pos1 == null) {
            pos1 = pos
            return super.useOn(useOnContext)
        }

        if (pos1 == pos) {
            pos1 = null
            return super.useOn(useOnContext)
        }

        block.link(level, pos1!!, pos)

        pos1 = null

        return super.useOn(useOnContext)
    }
}