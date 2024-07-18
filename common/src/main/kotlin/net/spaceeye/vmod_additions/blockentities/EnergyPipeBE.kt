package net.spaceeye.vmod_additions.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.sharedContainers.EnergyPipeEnergyTankHandler
import net.spaceeye.vmod_additions.PlatformUtils
import net.spaceeye.vmod_additions.VABlockEntities
import net.spaceeye.vmod_additions.sharedContainers.CommonEnergyTank

class EnergyPipeBE(pos: BlockPos, state: BlockState): CommonPipeBE<EnergyPipeBE, CommonEnergyTank>(
    VABlockEntities.ENERGY_PIPE.get(), pos, state,
    EnergyPipeEnergyTankHandler,
    {PlatformUtils.getEnergyTank()}) {
    //for forge mixin
    override fun onIdUpdate() { super.onIdUpdate() }
    private fun _getContainer(): CommonEnergyTank = container
}