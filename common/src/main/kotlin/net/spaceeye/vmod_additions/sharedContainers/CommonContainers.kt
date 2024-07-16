package net.spaceeye.vmod_additions.sharedContainers

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack

interface CommonContainer {
    fun withCapacity(capacity: Long): CommonContainer

    fun writeNBT(name: String, tag: CompoundTag)
    fun readNBT(name: String, tag: CompoundTag)

    fun registerOnChange(callback: () -> Unit)
}

interface CommonEnergyTank: CommonContainer
interface CommonFluidTank: CommonContainer

class SingleItemContainer: CommonContainer {
    private var _item = ItemStack.EMPTY
    private var onChange: (() -> Unit)? = null

    var item: ItemStack
        get() = _item
        set(value) {
            if (!_canChange) {return}
            _item = value
            onChange?.invoke()
        }

    private var _canChange = true
    val canChange: Boolean
        get() = _canChange

    override fun withCapacity(capacity: Long): CommonContainer {
        _canChange = capacity > 0L
        return this
    }

    override fun writeNBT(name: String, tag: CompoundTag) {
        tag.put(name, item.save(CompoundTag()))
    }

    override fun readNBT(name: String, tag: CompoundTag) {
        item = ItemStack.of(tag.getCompound(name))
    }

    override fun registerOnChange(callback: () -> Unit) {
        onChange = callback
    }
}