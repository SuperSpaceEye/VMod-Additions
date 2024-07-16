package net.spaceeye.vmod_additions.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.sharedContainers.FluidPipeFluidTankHandler
import net.spaceeye.vmod_additions.PlatformUtils
import net.spaceeye.vmod_additions.VABlockEntities
import net.spaceeye.vmod_additions.sharedContainers.CommonFluidTank

class FluidPipeBE(pos: BlockPos, state: BlockState): CommonPipeBE<FluidPipeBE, CommonFluidTank>(
    VABlockEntities.FLUID_PIPE.get(), pos, state,
    FluidPipeFluidTankHandler,
    {PlatformUtils.getFluidTank()})