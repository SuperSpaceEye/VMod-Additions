package net.spaceeye.vmod_additions.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.ItemPipeStacksHandler
import net.spaceeye.vmod_additions.VABlockEntities

//TODO add logic for toolgun schem
class ItemPipeBE(pos: BlockPos, state: BlockState): BlockEntity(VABlockEntities.ITEM_PIPE.get(), pos, state), WorldlyContainer {
    var id = -1
    var item: ItemStack
        get() = ItemPipeStacksHandler.getStack(id)
        set(value) = ItemPipeStacksHandler.setStack(id, value)
    var mID = -1
    var otherPos = BlockPos(0, 0, 0)

    override fun saveAdditional(tag: CompoundTag) {
        tag.putInt("id", id)
        tag.putInt("mID", mID)
        tag.putLong("otherPos", otherPos.asLong())
        tag.put("item", item.save(CompoundTag()))
    }

    override fun load(tag: CompoundTag) {
        id = tag.getInt("id")
        mID = tag.getInt("mID")
        otherPos = BlockPos.of(tag.getLong("otherPos"))
        ItemPipeStacksHandler.loadStack(id, ItemStack.of(tag.getCompound("item")))
    }

    override fun removeItem(slot: Int, count: Int): ItemStack {
        val result = ContainerHelper.removeItem(listOf(item), slot, count)
        if (!result.isEmpty) { setChanged() }
        return result
    }

    override fun removeItemNoUpdate(slot: Int): ItemStack {
        return ContainerHelper.takeItem(listOf(item), slot)
    }

    override fun setItem(i: Int, stack: ItemStack) {
        item = stack
        if (stack.count > maxStackSize) {
            stack.count = maxStackSize
        }
    }

    override fun clearContent() { item = ItemStack.EMPTY; setChanged() }
    override fun getContainerSize() = 1
    override fun isEmpty() = item.isEmpty
    override fun getItem(i: Int) = item

    override fun stillValid(player: Player) = false
    override fun getSlotsForFace(direction: Direction) = intArrayOf(0)
    override fun canPlaceItemThroughFace(i: Int, itemStack: ItemStack, direction: Direction?) = ItemPipeStacksHandler.canUseStack(id)
    override fun canTakeItemThroughFace(i: Int, itemStack: ItemStack, direction: Direction) = ItemPipeStacksHandler.canUseStack(id)
}