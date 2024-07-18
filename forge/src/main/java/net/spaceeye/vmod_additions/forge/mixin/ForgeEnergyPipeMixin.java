package net.spaceeye.vmod_additions.forge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.spaceeye.vmod_additions.blockentities.EnergyPipeBE;
import net.spaceeye.vmod_additions.forge.ForgeEnergyTank;
import net.spaceeye.vmod_additions.sharedContainers.CommonEnergyTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EnergyPipeBE.class)
abstract class ForgeEnergyPipeMixin extends BlockEntity {
    public ForgeEnergyPipeMixin(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
    }

    @Shadow
    abstract public CommonEnergyTank _getContainer();

    @Unique
    public LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    @Unique public Capability<IEnergyStorage> energyCap = CapabilityManager.get(new CapabilityToken<>(){});

    /**
     * @author SpaceEye
     * @reason it created to be overwritten on forge
     */
    @Overwrite(remap = false)
    public void onIdUpdate() {
        this.invalidateCaps();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == energyCap) {
            return lazyEnergyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> (ForgeEnergyTank)_getContainer());
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
        lazyEnergyHandler = LazyOptional.of(() -> (ForgeEnergyTank)_getContainer());
    }
}
