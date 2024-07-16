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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.spaceeye.vmod_additions.blockentities.FluidPipeBE;
import net.spaceeye.vmod_additions.forge.ForgeFluidTank;
import net.spaceeye.vmod_additions.sharedContainers.CommonFluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FluidPipeBE.class)
abstract class ForgeFluidPipeMixin extends BlockEntity {
    @Shadow
    abstract public CommonFluidTank getTank();

    @Unique public LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
    @Unique public Capability<IFluidHandler> fluidCap = CapabilityManager.get(new CapabilityToken<>(){});

    public ForgeFluidPipeMixin(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
    }

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
        if (cap == fluidCap) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> (ForgeFluidTank)getTank());
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
        lazyFluidHandler = LazyOptional.of(() -> (ForgeFluidTank)getTank());
    }
}
