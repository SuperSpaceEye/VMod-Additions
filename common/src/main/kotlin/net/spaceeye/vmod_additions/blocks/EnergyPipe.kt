package net.spaceeye.vmod_additions.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.sharedContainers.EnergyPipeEnergyTankHandler
import net.spaceeye.vmod_additions.VAConfig
import net.spaceeye.vmod_additions.blockentities.EnergyPipeBE
import net.spaceeye.vmod_additions.sharedContainers.CommonEnergyTank
import net.spaceeye.vmod_additions.sharedContainers.CommonSharedContainerHandler

class EnergyPipe(properties: Properties): CommonPipe<CommonEnergyTank>(properties) {
    override val handler: CommonSharedContainerHandler<CommonEnergyTank> get() = EnergyPipeEnergyTankHandler
    override val maxConnectionDist: Double get() = VAConfig.SERVER.PIPES.MAX_DIST.ENERGY_PIPE
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity? { return EnergyPipeBE(blockPos, blockState) }
}