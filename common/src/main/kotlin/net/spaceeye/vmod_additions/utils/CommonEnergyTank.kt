package net.spaceeye.vmod_additions.utils

import net.minecraft.nbt.CompoundTag

interface CommonEnergyTank {
    fun withCapacity(capacity: Long): CommonEnergyTank

    fun writeNBT(name: String, tag: CompoundTag)
    fun readNBT(name: String, tag: CompoundTag)

    fun registerOnChange(callback: () -> Unit)
}