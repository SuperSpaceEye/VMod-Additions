package net.spaceeye.vmod_additions.fabric

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.minecraft.nbt.CompoundTag
import net.spaceeye.vmod_additions.sharedContainers.CommonFluidTank

class FabricFluidTank: CommonFluidTank, SingleVariantStorage<FluidVariant>() {
    private var _capacity = FluidConstants.BUCKET
    var onChange: (() -> Unit)? = null
    override fun writeNBT(name: String, tag: CompoundTag) {
        tag.put("${name}fluidVariant", variant.toNbt())
        tag.putLong("${name}amount", amount)
        tag.putLong("${name}capacity", _capacity)
    }

    override fun readNBT(name: String, tag: CompoundTag) {
        variant = FluidVariant.fromNbt(tag.getCompound("${name}fluidVariant"))
        amount = tag.getLong("${name}amount")
        _capacity = tag.getLong("${name}capacity")
    }

    override fun withCapacity(capacity: Long): FabricFluidTank {
        this._capacity = FluidConstants.BUCKET * capacity
        return this
    }

    override fun registerOnChange(callback: () -> Unit) {
        onChange = callback
    }

    override fun getCapacity(variant: FluidVariant?): Long {
        return _capacity
    }

    override fun getBlankVariant(): FluidVariant {
        return FluidVariant.blank()
    }

    override fun onFinalCommit() {
        super.onFinalCommit()
        onChange?.invoke()
    }
}