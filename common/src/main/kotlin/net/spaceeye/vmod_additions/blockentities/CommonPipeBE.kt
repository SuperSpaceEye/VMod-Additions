package net.spaceeye.vmod_additions.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.sharedContainers.CommonContainer
import net.spaceeye.vmod_additions.sharedContainers.CommonSharedContainerHandler

//TODO add logic for toolgun schem
abstract class CommonPipeBE<T: BlockEntity, TT: CommonContainer>(
    type: BlockEntityType<T>,
    pos: BlockPos,
    state: BlockState,
    val containerHandler: CommonSharedContainerHandler<TT>,
    val containerConstructor: () -> TT): BlockEntity(type, pos, state) {
    var _id = -1
    var id:Int
        get() = _id
        set(value) {onIdUpdate(); _id = value}

    var mID = -1
    var otherPos = BlockPos(0, 0, 0)
    val container: TT
        get() = containerHandler.getContainer(id)

    // should be overwritten in forge mixin to invalidate caps if needed
    private fun onIdUpdate() {}

    override fun saveAdditional(tag: CompoundTag) {
        tag.putInt("id", id)
        tag.putInt("mID", mID)
        tag.putLong("otherPos", otherPos.asLong())
        container.writeNBT("container", tag)
    }

    override fun load(tag: CompoundTag) {
        id = tag.getInt("id")
        mID = tag.getInt("mID")
        otherPos = BlockPos.of(tag.getLong("otherPos"))
        val tempTank = containerConstructor()
        tempTank.readNBT("container", tag)
        containerHandler.loadContainer(id, tempTank)
    }
}