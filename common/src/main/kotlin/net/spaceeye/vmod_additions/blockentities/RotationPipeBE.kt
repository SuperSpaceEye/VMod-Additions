package net.spaceeye.vmod_additions.blockentities

import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.VABlockEntities

class RotationPipeBE(pos: BlockPos, state: BlockState): KineticBlockEntity(VABlockEntities.ROTATION_PIPE!!.get(), pos, state) {
    var mID = -1
    var otherPos: BlockPos? = null

    override fun writeSafe(tag: CompoundTag) {
        super.writeSafe(tag)
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
}