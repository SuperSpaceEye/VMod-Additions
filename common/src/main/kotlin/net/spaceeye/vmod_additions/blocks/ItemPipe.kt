package net.spaceeye.vmod_additions.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.vmod.constraintsManaging.makeManagedConstraint
import net.spaceeye.vmod.constraintsManaging.removeManagedConstraint
import net.spaceeye.vmod.constraintsManaging.types.RopeMConstraint
import net.spaceeye.vmod.rendering.types.A2BRenderer
import net.spaceeye.vmod.utils.Vector3d
import net.spaceeye.vmod.utils.vs.posShipToWorld
import net.spaceeye.vmod_additions.ItemPipeStacksHandler
import net.spaceeye.vmod_additions.Linkable
import net.spaceeye.vmod_additions.VAConfig
import net.spaceeye.vmod_additions.blockentities.ItemPipeBE
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.shipObjectWorld
import java.awt.Color

class ItemPipe(properties: Properties): BaseEntityBlock(properties), Linkable {
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity? {
        return ItemPipeBE(blockPos, blockState)
    }

    override fun onRemove(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        blockState2: BlockState,
        bl: Boolean
    ) {
        if (level !is ServerLevel) {return super.onRemove(blockState, level, blockPos, blockState2, bl)}
        val be = level.getBlockEntity(blockPos) as ItemPipeBE
        level.removeManagedConstraint(be.mID)
        super.onRemove(blockState, level, blockPos, blockState2, bl)
    }

    //TODO add more checks for linking
    override fun link(level: ServerLevel, pos1: BlockPos, pos2: BlockPos): Boolean {
        Vector3d(0, 10, 0)
        val be1 = level.getBlockEntity(pos1) ?: return false
        val be2 = level.getBlockEntity(pos2) ?: return false

        if (be1 !is ItemPipeBE || be2 !is ItemPipeBE) {return false}

        val ship1 = level.getShipManagingPos(pos1)
        val ship2 = level.getShipManagingPos(pos2)

        val spos1 = Vector3d(pos1) + 0.5
        val spos2 = Vector3d(pos2) + 0.5

        //TODO can't use .let here because reasons
        val rpos1 = if (ship1 != null) { posShipToWorld(ship1, spos1) } else Vector3d(spos1)
        val rpos2 = if (ship2 != null) { posShipToWorld(ship2, spos2) } else Vector3d(spos2)

        if ((rpos1 - rpos2).dist() >= VAConfig.SERVER.PIPES.ITEM_PIPE_MAX_DIST) {
            return false
        }

        val mID = level.makeManagedConstraint(RopeMConstraint(
            ship1?.id ?: level.shipObjectWorld.dimensionToGroundBodyIdImmutable[level.dimensionId]!!,
            ship2?.id ?: level.shipObjectWorld.dimensionToGroundBodyIdImmutable[level.dimensionId]!!,
            1e-20,
            spos1.toJomlVector3d(), spos2.toJomlVector3d(),
            1e+20,
            VAConfig.SERVER.PIPES.ITEM_PIPE_MAX_DIST,
            listOf(pos1, pos2),
            A2BRenderer(
                ship1 != null, ship2 != null,
                spos1, spos2, Color.CYAN, 0.2
            )
        )) ?: return false

        val itemStack = be1.item
        val id = ItemPipeStacksHandler.createStack(
            {
                if (!level.isLoaded(pos1) || !level.isLoaded(pos2)) {return@createStack false}
                val be1 = level.getBlockEntity(pos1)
                if (be1 !is ItemPipeBE) {return@createStack false}
                val be2 = level.getBlockEntity(pos2)
                if (be2 !is ItemPipeBE) {return@createStack false}

                be1.id != -1 && be2.id != -1 && be1.id == be2.id
            }
        ) {
            val be1 = level.getBlockEntity(pos1)
            if (be1 is ItemPipeBE) { be1.setChanged() }

            val be2 = level.getBlockEntity(pos2)
            if (be2 is ItemPipeBE) { be2.setChanged() }

            (be1 is ItemPipeBE) && (be2 is ItemPipeBE)
        }
        ItemPipeStacksHandler.setStack(id, itemStack)

        be1.id = id
        be2.id = id

        be1.mID = mID
        be2.mID = mID

        be1.setChanged()
        be2.setChanged()

        return true
    }

    override fun getRenderShape(state: BlockState) = RenderShape.MODEL
}