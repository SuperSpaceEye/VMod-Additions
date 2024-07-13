package net.spaceeye.vmod_additions

import dev.architectury.registry.CreativeTabRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.spaceeye.vmod.VM
import net.spaceeye.vmod_additions.items.Linker

object VAItems {
    val ITEMS = DeferredRegister.create(VModAdditions.MOD_ID, Registry.ITEM_REGISTRY)

    val TAB: CreativeModeTab = CreativeTabRegistry.create(
        ResourceLocation(
            VM.MOD_ID,
            "vmod_additions_tab"
        )
    ) { ItemStack(LOGO.get()) }

    var LOGO: RegistrySupplier<Item> = ITEMS.register("vmod_additions_logo") { Item(Item.Properties()) }

    var LINKER = ITEMS.register("linker") { Linker(Item.Properties().stacksTo(1).tab(TAB)) }

    fun register() {
        VABlocks.registerItems(ITEMS)
        ITEMS.register()
    }
}