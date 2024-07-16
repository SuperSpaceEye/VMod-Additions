package net.spaceeye.vmod_additions.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.sharedContainers.FluidPipeFluidTankHandler
import net.spaceeye.vmod_additions.VAConfig
import net.spaceeye.vmod_additions.blockentities.FluidPipeBE
import net.spaceeye.vmod_additions.sharedContainers.CommonFluidTank
import net.spaceeye.vmod_additions.sharedContainers.CommonSharedContainerHandler

class FluidPipe(properties: Properties): CommonPipe<CommonFluidTank>(properties) {
    override val handler: CommonSharedContainerHandler<CommonFluidTank> get() = FluidPipeFluidTankHandler
    override val maxConnectionDist: Double get() = VAConfig.SERVER.PIPES.MAX_DIST.FLUID_PIPE
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity? { return FluidPipeBE(blockPos, blockState) }
}