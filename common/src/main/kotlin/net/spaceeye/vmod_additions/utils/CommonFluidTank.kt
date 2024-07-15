package net.spaceeye.vmod_additions.utils

import net.minecraft.nbt.CompoundTag

interface CommonFluidTank {
    fun withCapacity(capacity: Long): CommonFluidTank

    fun writeNBT(name: String, tag: CompoundTag)
    fun readNBT(name: String, tag: CompoundTag)

    fun registerOnChange(callback: () -> Unit)
}