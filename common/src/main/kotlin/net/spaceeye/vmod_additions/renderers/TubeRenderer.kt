package net.spaceeye.vmod_additions.renderers

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.Blocks
import net.spaceeye.vmod.rendering.types.BaseRenderer
import net.spaceeye.vmod.rendering.types.BlockRenderer
import net.spaceeye.vmod.utils.Vector3d
import net.spaceeye.vmod.utils.getQuatFromDir
import net.spaceeye.vmod.utils.readVector3d
import net.spaceeye.vmod.utils.vs.posShipToWorldRender
import net.spaceeye.vmod.utils.writeVector3d
import org.joml.Quaterniond
import org.lwjgl.opengl.GL11
import org.valkyrienskies.core.api.ships.ClientShip
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.util.toMinecraft

object TubeState {
    var state = Blocks.OAK_FENCE.defaultBlockState()
}

class TubeRenderer(): BlockRenderer {
    var point1 = Vector3d()
    var point2 = Vector3d()

    var scale: Float = 1.0f

    var ship1isShip: Boolean = false
    var ship2isShip: Boolean = false

    constructor(
        pos1: Vector3d, pos2: Vector3d, scale: Float, ship1isShip: Boolean, ship2isShip: Boolean
    ): this() {
        this.point1 = pos1
        this.point2 = pos2
        this.scale = scale
        this.ship1isShip = ship1isShip
        this.ship2isShip = ship2isShip
    }

    override val typeName = "VMATubeRenderer"
    override fun copy(nShip1: Ship?, nShip2: Ship?, spoint1: Vector3d, spoint2: Vector3d): BaseRenderer {
        return TubeRenderer(spoint1, spoint2, scale, ship1isShip, ship2isShip)
    }

    override fun renderBlockData(poseStack: PoseStack, camera: Camera, buffer: MultiBufferSource) {
        val level = Minecraft.getInstance().level!!

        RenderSystem.enableDepthTest()
        RenderSystem.depthFunc(GL11.GL_LEQUAL)
        RenderSystem.depthMask(true)
        RenderSystem.setShader(GameRenderer::getPositionShader)
        RenderSystem.enableBlend()

        val ship1 = if (ship1isShip) level.getShipManagingPos(point1.toBlockPos()) else null
        val ship2 = if (ship2isShip) level.getShipManagingPos(point2.toBlockPos()) else null

        val aShip = (ship1 ?: ship2 ?: return) as ClientShip

        val pos1 = if (ship1isShip) posShipToWorldRender((ship1 ?: return) as ClientShip, point1) else point1
        val pos2 = if (ship2isShip) posShipToWorldRender((ship2 ?: return) as ClientShip, point2) else point2

        val shipScale = aShip.renderTransform.shipToWorldScaling.x()
        val dir = pos2 - pos1
        val middlePoint = pos1 + dir / 2

        poseStack.pushPose()

        poseStack.translate(-camera.position.x, -camera.position.y, -camera.position.z)
        poseStack.translate(middlePoint.x, middlePoint.y, middlePoint.z)

        poseStack.mulPose(
            Quaterniond()
                .mul(getQuatFromDir(dir.normalize()))
                .toMinecraft()
        )

        val yScale = (dir).dist()

        poseStack.scale(scale, yScale.toFloat(), scale)
        poseStack.translate(-0.5, -0.5 / scale * shipScale, -0.5)

        val combinedLightIn = LightTexture.pack(0, level.getBrightness(LightLayer.SKY, middlePoint.toBlockPos()))
        val combinedOverlayIn = OverlayTexture.NO_OVERLAY

        Minecraft.getInstance().blockRenderer
            .renderSingleBlock(TubeState.state, poseStack, buffer, combinedLightIn, combinedOverlayIn)

        poseStack.popPose()
    }

    override fun deserialize(buf: FriendlyByteBuf) {
        point1 = buf.readVector3d()
        point2 = buf.readVector3d()

        scale = buf.readFloat()

        ship1isShip = buf.readBoolean()
        ship2isShip = buf.readBoolean()
    }

    override fun serialize(): FriendlyByteBuf {
        val buf = getBuffer()

        buf.writeVector3d(point1)
        buf.writeVector3d(point2)

        buf.writeFloat(scale)

        buf.writeBoolean(ship1isShip)
        buf.writeBoolean(ship2isShip)

        return buf
    }
}