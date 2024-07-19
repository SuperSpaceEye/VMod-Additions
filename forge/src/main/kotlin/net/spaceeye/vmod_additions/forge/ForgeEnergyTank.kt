package net.spaceeye.vmod_additions.forge

import net.minecraft.nbt.CompoundTag
import net.minecraftforge.energy.EnergyStorage
import net.spaceeye.vmod_additions.sharedContainers.CommonEnergyTank

class ForgeEnergyTank(capacity: Int): CommonEnergyTank, EnergyStorage(capacity, capacity, capacity) {
    constructor(): this(100000000)

    var callbacks = mutableListOf<(() -> Unit)>()

    override fun withCapacity(capacity: Long): CommonEnergyTank {
        return ForgeEnergyTank(capacity.toInt())
    }

    override fun writeNBT(name: String, tag: CompoundTag) {
        tag.put(name, this.serializeNBT())
    }

    override fun readNBT(name: String, tag: CompoundTag) {
        this.deserializeNBT(tag.get(name))
    }

    override fun registerOnChange(callback: () -> Unit) {
        callbacks.add(callback)
    }


    override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        if (!simulate) callbacks.forEach { it.invoke() }
        return super.receiveEnergy(maxReceive, simulate)
    }

    override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
        if (!simulate) callbacks.forEach { it.invoke() }
        return super.extractEnergy(maxExtract, simulate)
    }
}