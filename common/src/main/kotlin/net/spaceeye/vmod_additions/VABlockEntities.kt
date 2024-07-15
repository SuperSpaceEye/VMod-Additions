package net.spaceeye.vmod_additions

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod_additions.blockentities.FluidPipeBE
import net.spaceeye.vmod_additions.blockentities.ItemPipeBE

object VABlockEntities {
    private val BLOCKENTITIES = DeferredRegister.create(VModAdditions.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY)

    var ITEM_PIPE = VABlocks.ITEM_PIPE makePair ::ItemPipeBE byName "item_pipe"
    var FLUID_PIPE = VABlocks.FLUID_PIPE makePair ::FluidPipeBE byName "fluid_pipe"

    private infix fun <T: BlockEntity, TT: Block> RegistrySupplier<TT>.makePair(blockEntity: (BlockPos, BlockState) -> T) = Pair(this, { bp: BlockPos, bs: BlockState -> blockEntity(bp, bs)})
    private infix fun <T: BlockEntity, TT: Block> Pair<RegistrySupplier<TT>, (BlockPos, BlockState) -> T>.byName(name: String): RegistrySupplier<BlockEntityType<T>> =
        BLOCKENTITIES.register(name) {
            val type = Util.fetchChoiceType(References.BLOCK_ENTITY, name)

            BlockEntityType.Builder.of(
                this.second,
                this.first.get()
            ).build(type)
        }

    fun register() {
        BLOCKENTITIES.register()
    }
}