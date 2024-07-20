package net.spaceeye.vmod_additions.blockentities

import com.simibubi.create.content.kinetics.RotationPropagator
import com.simibubi.create.content.kinetics.base.IRotate
import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod.events.AVSEvents
import net.spaceeye.vmod.events.RandomEvents
import net.spaceeye.vmod_additions.VABlockEntities

class RotationPipeBE(pos: BlockPos, state: BlockState): KineticBlockEntity(VABlockEntities.ROTATION_PIPE!!.get(), pos, state), MIDContainer {
    override var mID = -1
    var otherPos: BlockPos? = null

    override fun write(tag: CompoundTag?, clientPacket: Boolean) {
        if (tag == null || clientPacket) {return super.write(tag, clientPacket)}

        tag.putInt("mID", mID)
        tag.putBoolean("isConnected", otherPos != null)
        if (otherPos == null) {return}
        tag.putLong("otherPos", otherPos!!.asLong())
    }

    override fun read(tag: CompoundTag, clientPacket: Boolean) {
        if (clientPacket) {return}
        mID = tag.getInt("mID")
        val isConnected = tag.getBoolean("isConnected")
        if (!isConnected) {return}
        otherPos = BlockPos.of(tag.getLong("otherPos"))

        //for some reason blockentity doesn't have a server level when read is called, so i need to do this shit
        RandomEvents.serverOnTick.on { (it), unregister ->
            val level = level
            if (level !is ServerLevel) {return@on}

            val otherBE = level.getBlockEntity(otherPos!!) ?: return@on unregister.unregister()
            if (otherBE !is RotationPipeBE) return@on unregister.unregister()
            if (otherBE.otherPos != blockPos) return@on unregister.unregister()
            RotationPropagator.handleRemoved(level, blockPos, this)
            RotationPropagator.handleRemoved(level, otherPos, otherBE)

            RotationPropagator.handleAdded(level, blockPos, this)
            RotationPropagator.handleAdded(level, otherPos, otherBE)

            unregister.unregister()
        }
    }

    override fun isCustomConnection(other: KineticBlockEntity?, state: BlockState?, otherState: BlockState?): Boolean {
        return true
    }

    override fun propagateRotationTo(
        target: KineticBlockEntity?,
        stateFrom: BlockState?,
        stateTo: BlockState?,
        diff: BlockPos?,
        connectedViaAxes: Boolean,
        connectedViaCogs: Boolean
    ): Float {
        if (target !is RotationPipeBE) {return super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs)}
        return 1f
    }

    override fun addPropagationLocations(
        block: IRotate?,
        state: BlockState?,
        neighbours: MutableList<BlockPos>?
    ): MutableList<BlockPos> {
        otherPos?.let { neighbours!!.add(it) }
        return super.addPropagationLocations(block, state, neighbours)
    }
}