package net.spaceeye.vmod_additions.forge

import net.minecraft.nbt.CompoundTag
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.templates.FluidTank
import net.spaceeye.vmod_additions.sharedContainers.CommonFluidTank

class ForgeFluidTank(capacity: Int): CommonFluidTank, FluidTank(capacity) {
    constructor(): this(1)

    var onChange: (() -> Unit)? = null

    override fun withCapacity(capacity: Long): CommonFluidTank {
        return ForgeFluidTank(capacity.toInt() * 1000)
    }

    override fun writeNBT(name: String, tag: CompoundTag) {
        val fluidTag = CompoundTag()
        fluid.writeToNBT(fluidTag)
        tag.put(name, fluidTag)
    }

    override fun readNBT(name: String, tag: CompoundTag) {
        val fluidTag = tag.getCompound(name)
        fluid = FluidStack.loadFluidStackFromNBT(fluidTag)
    }

    override fun registerOnChange(callback: () -> Unit) {
        onChange = callback
    }

    override fun onContentsChanged() {
        super.onContentsChanged()
        onChange?.invoke()
    }
}