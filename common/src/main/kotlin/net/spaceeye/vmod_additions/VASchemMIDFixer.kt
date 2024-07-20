package net.spaceeye.vmod_additions

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.spaceeye.vmod.schematic.ShipSchematic
import net.spaceeye.vmod_additions.blockentities.MIDContainer

object VASchemMIDFixer {
    private val updateRequests = mutableMapOf<Int, MutableList<Pair<ServerLevel, BlockPos>>>()

    fun addRequest(oldId: Int, level: ServerLevel, pos: BlockPos) {
        updateRequests.getOrPut(oldId) { mutableListOf() }.add(Pair(level, pos))
    }

    init {
        ShipSchematic.registerOrderedCopyPasteEvents(
            "VMod Additions mID Fixer", "VMod Constraint Manager",
            {_, _, _, _ -> null},
            {_, _, _, _, _, ->
                val globalMap = ShipSchematic.getGlobalMap("VMod Constraint Manager") ?: return@registerOrderedCopyPasteEvents
                val change = globalMap["changedIDs"] as Map<Int, Int>

                change.forEach { (k, v) ->
                    val requests = updateRequests[k]
                    updateRequests.remove(k)
                    if (requests == null) { return@forEach }
                    requests.forEach { (level, pos) ->
                        val be = level.getBlockEntity(pos) ?: return@forEach
                        if (be !is MIDContainer) { return@forEach }

                        be.mID = v
                        be.setChanged()
                    }
                }
            }
        )
    }
}