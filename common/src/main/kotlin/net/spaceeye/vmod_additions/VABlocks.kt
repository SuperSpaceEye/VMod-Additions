package net.spaceeye.vmod_additions

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.spaceeye.vmod_additions.blocks.EnergyPipe
import net.spaceeye.vmod_additions.blocks.FluidPipe
import net.spaceeye.vmod_additions.blocks.ItemPipe

object VABlocks {
    private val BLOCKS = DeferredRegister.create(VModAdditions.MOD_ID, Registry.BLOCK_REGISTRY)

    var ITEM_PIPE = BLOCKS.register("item_pipe") { ItemPipe(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)) }
    var FLUID_PIPE = BLOCKS.register("fluid_pipe") { FluidPipe(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)) }
    var ENERGY_PIPE = BLOCKS.register("energy_pipe") { EnergyPipe(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)) }

    fun register() {BLOCKS.register()}
    fun registerItems(items: DeferredRegister<Item?>) {
        for (block in BLOCKS) {
            items.register(block.id) { BlockItem(block.get(), Item.Properties().tab(VAItems.TAB)) }
        }
    }
}