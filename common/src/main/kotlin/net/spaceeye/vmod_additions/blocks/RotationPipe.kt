package net.spaceeye.vmod_additions.blocks

import com.simibubi.create.content.kinetics.RotationPropagator
import com.simibubi.create.content.kinetics.base.IRotate
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock
import com.simibubi.create.foundation.block.IBE
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Axis
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.spaceeye.vmod.constraintsManaging.makeManagedConstraint
import net.spaceeye.vmod.constraintsManaging.removeManagedConstraint
import net.spaceeye.vmod.constraintsManaging.types.RopeMConstraint
import net.spaceeye.vmod.utils.Vector3d
import net.spaceeye.vmod.utils.vs.posShipToWorld
import net.spaceeye.vmod_additions.Linkable
import net.spaceeye.vmod_additions.VABlockEntities
import net.spaceeye.vmod_additions.blockentities.RotationPipeBE
import net.spaceeye.vmod_additions.renderers.TubeRenderer
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.shipObjectWorld

class RotationPipe(properties: Properties): RotatedPillarKineticBlock(properties), IBE<RotationPipeBE>, IRotate, Linkable {
    init {
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.AXIS, Axis.Y))
    }

    override fun unlink(level: ServerLevel, pos: BlockPos): Boolean {
        val be = level.getBlockEntity(pos) ?: return false
        if (be !is RotationPipeBE) return false
        if (be.otherPos == null) return false

        level.removeManagedConstraint(be.mID)
        be.mID = -1

        RotationPropagator.handleRemoved(level, pos, be)

        val opos = be.otherPos!!
        be.otherPos = null
        unlink(level, opos)

        RotationPropagator.handleAdded(level, pos, be)

        return true
    }

    override fun link(level: ServerLevel, pos1: BlockPos, pos2: BlockPos): Boolean {
        val be1 = level.getBlockEntity(pos1) ?: return false
        val be2 = level.getBlockEntity(pos2) ?: return false

        if (be1 !is RotationPipeBE || be2 !is RotationPipeBE) {return false}

        val ship1 = level.getShipManagingPos(pos1)
        val ship2 = level.getShipManagingPos(pos2)

        if (ship1 == ship2) {return false}

        val spos1 = Vector3d(pos1) + 0.5
        val spos2 = Vector3d(pos2) + 0.5

        //TODO can't use .let here because run configuration dies for some reason
        val rpos1 = if (ship1 != null) { posShipToWorld(ship1, spos1) } else Vector3d(spos1)
        val rpos2 = if (ship2 != null) { posShipToWorld(ship2, spos2) } else Vector3d(spos2)

        if ((rpos1 - rpos2).dist() >= 7.0) { return false }

        val mID = level.makeManagedConstraint(
            RopeMConstraint(
                ship1?.id ?: level.shipObjectWorld.dimensionToGroundBodyIdImmutable[level.dimensionId]!!,
                ship2?.id ?: level.shipObjectWorld.dimensionToGroundBodyIdImmutable[level.dimensionId]!!,
                1e-20,
                spos1.toJomlVector3d(), spos2.toJomlVector3d(),
                1e+20,
                7.0,
                listOf(pos1, pos2),
                TubeRenderer(
                    spos1, spos2, 1f, ship1 != null, ship2 != null
                )
            )
        ) ?: return false

        unlink(level, pos1)
        unlink(level, pos2)

        be1.mID = mID
        be2.mID = mID

        be1.otherPos = pos2
        be2.otherPos = pos1

        be1.setChanged()
        be2.setChanged()

        RotationPropagator.handleAdded(level, pos1, be1)
        RotationPropagator.handleAdded(level, pos2, be2)

        return true
    }

    override fun onRemove(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        blockState2: BlockState,
        bl: Boolean
    ) {
        if (level !is ServerLevel) {return super.onRemove(blockState, level, blockPos, blockState2, bl)}
        unlink(level, blockPos)
        super.onRemove(blockState, level, blockPos, blockState2, bl)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        return super.getStateForPlacement(ctx)
    }

    override fun hasShaftTowards(world: LevelReader?, pos: BlockPos?, state: BlockState?, face: Direction?) = face!!.axis == state!!.getValue(BlockStateProperties.AXIS)
    override fun getRotationAxis(state: BlockState?): Axis = state!!.getValue(AXIS)
    override fun getBlockEntityClass(): Class<RotationPipeBE> { return RotationPipeBE::class.java }
    override fun getBlockEntityType(): BlockEntityType<out RotationPipeBE> { return VABlockEntities.ROTATION_PIPE!!.get() }
}