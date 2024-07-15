package net.spaceeye.vmod_additions

import net.minecraft.world.item.ItemStack
import net.spaceeye.vmod.utils.ServerClosable
import kotlin.math.max

object ItemPipeStacksHandler: ServerClosable() {
    private val data = mutableMapOf<Int, ItemStack>()
    private val canMove = mutableMapOf<Int, () -> Boolean>()
    private val updates = mutableMapOf<Int, () -> Boolean>()

    override fun close() {
        data.clear()
        updates.clear()
    }

    var counter = -1

    fun canUseStack(id: Int) = id != -1 && canMove[id]?.invoke() ?: false

    fun createStack(canMoveItems: () -> Boolean, update: () -> Boolean): Int {
        counter++

        data.getOrPut(counter) {ItemStack.EMPTY}
        canMove.getOrPut(counter) {canMoveItems}
        updates.getOrPut(counter) {update}

        return counter
    }

    fun loadStack(id: Int, stack: ItemStack) {
        data.getOrPut(id) {stack}
        updates[id]?.invoke()
        counter = max(counter, id)
    }

    fun setStack(id: Int, stack: ItemStack) {
        if (!canUseStack(id)) {return}
        data[id] = stack
        updates[id]?.invoke()
    }

    fun getStack(id: Int) = data[id] ?: ItemStack.EMPTY

    fun removeStack(id: Int) {
        data.remove(id)
        canMove.remove(id)
        updates.remove(id)
    }
}