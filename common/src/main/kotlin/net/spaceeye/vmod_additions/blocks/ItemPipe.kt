package net.spaceeye.vmod_additions.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.sharedContainers.ItemPipeStacksHandler
import net.spaceeye.vmod_additions.VAConfig
import net.spaceeye.vmod_additions.blockentities.ItemPipeBE
import net.spaceeye.vmod_additions.sharedContainers.CommonSharedContainerHandler
import net.spaceeye.vmod_additions.sharedContainers.SingleItemContainer

class ItemPipe(properties: Properties): CommonPipe<SingleItemContainer>(properties) {
    override val handler: CommonSharedContainerHandler<SingleItemContainer> get() = ItemPipeStacksHandler
    override val maxConnectionDist: Double get() = VAConfig.SERVER.PIPES.ITEM_PIPE_MAX_DIST
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity? { return ItemPipeBE(blockPos, blockState) }
}