package net.spaceeye.vmod_additions

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel

interface Linkable {
    fun link(level: ServerLevel, pos1: BlockPos, pos2: BlockPos): Boolean
    fun unlink(level: ServerLevel, pos: BlockPos): Boolean
}