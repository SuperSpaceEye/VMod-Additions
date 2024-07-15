package net.spaceeye.vmod_additions.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.FluidPipeFluidTankHandler
import net.spaceeye.vmod_additions.PlatformUtils
import net.spaceeye.vmod_additions.VABlockEntities
import net.spaceeye.vmod_additions.utils.CommonFluidTank

//TODO add logic for toolgun schem
open class FluidPipeBE(pos: BlockPos, state: BlockState): BlockEntity(VABlockEntities.FLUID_PIPE.get(), pos, state) {
    var _id = -1
    var id:Int
        get() = _id
        set(value) {onIdUpdate(); _id = value}

    var mID = -1
    var otherPos = BlockPos(0, 0, 0)
    val tank: CommonFluidTank
        get() = FluidPipeFluidTankHandler.getTank(id)

    // to invalidate caps on forge
    open fun onIdUpdate() {}

    override fun saveAdditional(tag: CompoundTag) {
        tag.putInt("id", id)
        tag.putInt("mID", mID)
        tag.putLong("otherPos", otherPos.asLong())
        tank.writeNBT("fluid", tag)
    }

    override fun load(tag: CompoundTag) {
        id = tag.getInt("id")
        mID = tag.getInt("mID")
        otherPos = BlockPos.of(tag.getLong("otherPos"))
        val tempTank = PlatformUtils.getFluidTank()
        tempTank.readNBT("fluid", tag)
        FluidPipeFluidTankHandler.loadTank(id, tempTank)
    }
}