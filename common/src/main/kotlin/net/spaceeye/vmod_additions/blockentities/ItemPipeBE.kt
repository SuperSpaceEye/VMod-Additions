package net.spaceeye.vmod_additions.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.sharedContainers.ItemPipeStacksHandler
import net.spaceeye.vmod_additions.VABlockEntities
import net.spaceeye.vmod_additions.sharedContainers.SingleItemContainer

class ItemPipeBE(pos: BlockPos, state: BlockState): CommonPipeBE<ItemPipeBE, SingleItemContainer>(
    VABlockEntities.ITEM_PIPE.get(), pos, state,
    ItemPipeStacksHandler,
    {SingleItemContainer()}), WorldlyContainer {
    override fun removeItem(slot: Int, count: Int): ItemStack {
        val result = ContainerHelper.removeItem(listOf(container.item), slot, count)
        if (!result.isEmpty) { setChanged() }
        return result
    }

    override fun removeItemNoUpdate(slot: Int): ItemStack {
        return ContainerHelper.takeItem(listOf(container.item), slot)
    }

    override fun setItem(i: Int, stack: ItemStack) {
        container.item = stack
        if (stack.count > maxStackSize) {
            stack.count = maxStackSize
        }
    }

    override fun clearContent() { container.item = ItemStack.EMPTY; setChanged() }
    override fun getContainerSize() = if (container.canChange) 1 else 0
    override fun isEmpty() = container.item.isEmpty
    override fun getItem(i: Int) = container.item

    override fun stillValid(player: Player) = false
    override fun getSlotsForFace(direction: Direction) = if (container.canChange) intArrayOf(0) else intArrayOf()
    override fun canPlaceItemThroughFace(i: Int, itemStack: ItemStack, direction: Direction?) = true
    override fun canTakeItemThroughFace(i: Int, itemStack: ItemStack, direction: Direction) = true
}