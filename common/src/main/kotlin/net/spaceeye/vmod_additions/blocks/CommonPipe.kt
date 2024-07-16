package net.spaceeye.vmod_additions.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod.constraintsManaging.makeManagedConstraint
import net.spaceeye.vmod.constraintsManaging.removeManagedConstraint
import net.spaceeye.vmod.constraintsManaging.types.RopeMConstraint
import net.spaceeye.vmod.utils.Vector3d
import net.spaceeye.vmod.utils.vs.posShipToWorld
import net.spaceeye.vmod_additions.Linkable
import net.spaceeye.vmod_additions.blockentities.CommonPipeBE
import net.spaceeye.vmod_additions.renderers.TubeRenderer
import net.spaceeye.vmod_additions.sharedContainers.CommonContainer
import net.spaceeye.vmod_additions.sharedContainers.CommonSharedContainerHandler
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.shipObjectWorld

abstract class CommonPipe<T: CommonContainer>(properties: Properties): BaseEntityBlock(properties), Linkable {
    abstract val handler: CommonSharedContainerHandler<T>
    abstract val maxConnectionDist: Double

    override fun unlink(level: ServerLevel, pos: BlockPos): Boolean {
        val be = level.getBlockEntity(pos) ?: return false
        if (be !is CommonPipeBE<*, *>) return false
        if (be.id == -1) return false

        handler.removeContainer(be.id)
        level.removeManagedConstraint(be.mID)

        be.id = -1
        be.mID = -1

        unlink(level, be.otherPos)
        be.otherPos = BlockPos(0, 0, 0)
        return true
    }

    override fun link(level: ServerLevel, pos1: BlockPos, pos2: BlockPos): Boolean {
        val be1 = level.getBlockEntity(pos1) ?: return false
        val be2 = level.getBlockEntity(pos2) ?: return false

        if (be1 !is CommonPipeBE<*, *> || be2 !is CommonPipeBE<*, *>) {return false}

        val ship1 = level.getShipManagingPos(pos1)
        val ship2 = level.getShipManagingPos(pos2)

        if (ship1 == null && ship2 == null) {return false}

        val spos1 = Vector3d(pos1) + 0.5
        val spos2 = Vector3d(pos2) + 0.5

        //TODO can't use .let here because run configuration dies for some reason
        val rpos1 = if (ship1 != null) { posShipToWorld(ship1, spos1) } else Vector3d(spos1)
        val rpos2 = if (ship2 != null) { posShipToWorld(ship2, spos2) } else Vector3d(spos2)

        if ((rpos1 - rpos2).dist() >= maxConnectionDist) { return false }

        val mID = level.makeManagedConstraint(
            RopeMConstraint(
                ship1?.id ?: level.shipObjectWorld.dimensionToGroundBodyIdImmutable[level.dimensionId]!!,
                ship2?.id ?: level.shipObjectWorld.dimensionToGroundBodyIdImmutable[level.dimensionId]!!,
                1e-20,
                spos1.toJomlVector3d(), spos2.toJomlVector3d(),
                1e+20,
                maxConnectionDist,
                listOf(pos1, pos2),
                TubeRenderer(
                    spos1, spos2, 1f, ship1 != null, ship2 != null
                )
            )
        ) ?: return false

        unlink(level, pos1)
        unlink(level, pos2)

        val id = handler.createContainer {
            val be1 = level.getBlockEntity(pos1)
            if (be1 is CommonPipeBE<*, *>) { be1.setChanged() }

            val be2 = level.getBlockEntity(pos2)
            if (be2 is CommonPipeBE<*, *>) { be2.setChanged() }

            (be1 is CommonPipeBE<*, *>) && (be2 is CommonPipeBE<*, *>)
        }

        be1.id = id
        be2.id = id

        be1.mID = mID
        be2.mID = mID

        be1.otherPos = pos2
        be2.otherPos = pos1

        be1.setChanged()
        be2.setChanged()

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

    override fun getRenderShape(state: BlockState) = RenderShape.MODEL
}
