package net.spaceeye.vmod_additions.mixin;

import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.spaceeye.vmod_additions.blockentities.RotationPipeBE;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(RotationPropagator.class)
public class RotationPropagatorMixin {
    @Inject(
            method = "getPotentialNeighbourLocations",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;", ordinal = 0),
            remap = false,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void vmod$injectRotationPipeLogic(KineticBlockEntity be, CallbackInfoReturnable<List<BlockPos>> cir, List neighbours, BlockPos blockPos, Level level, BlockState blockState) {
        if (!(be instanceof RotationPipeBE)) {return;}
        var otherPos = ((RotationPipeBE) be).getOtherPos();
        if (otherPos == null) {return;}
        neighbours.add(otherPos);
    }
}
