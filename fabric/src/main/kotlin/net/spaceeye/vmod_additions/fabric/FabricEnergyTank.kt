package net.spaceeye.vmod_additions.fabric

import net.minecraft.nbt.CompoundTag
import net.spaceeye.vmod_additions.utils.CommonEnergyTank
import team.reborn.energy.api.base.SimpleEnergyStorage

class FabricEnergyTank(capacity: Long): CommonEnergyTank, SimpleEnergyStorage(capacity, capacity, capacity) {
    var onChange: (() -> Unit)? = null

    constructor(): this(100000000L) {}

    override fun withCapacity(capacity: Long): CommonEnergyTank {
        return FabricEnergyTank(capacity)
    }

    override fun writeNBT(name: String, tag: CompoundTag) {
        tag.putLong("${name}amount", amount)
    }

    override fun readNBT(name: String, tag: CompoundTag) {
        amount = tag.getLong("${name}amount")
    }

    override fun registerOnChange(callback: () -> Unit) {
        onChange = callback
    }



    override fun onFinalCommit() {
        super.onFinalCommit()
        onChange?.invoke()
    }
}