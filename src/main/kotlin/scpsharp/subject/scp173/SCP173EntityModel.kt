/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
@file:Environment(EnvType.CLIENT)

package scpsharp.subject.scp173

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.model.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.util.math.MatrixStack
import scpsharp.util.id

class SCP173EntityModel(val model: ModelPart) : EntityModel<SCP173Entity>() {

    companion object {

        const val MODEL_SCALE = 0.5f

        fun createTexturedModelData(dilation: Dilation = Dilation.NONE): TexturedModelData =
            TexturedModelData.of(ModelData().apply {
                root
                    .addChild("head", ModelPartBuilder.create().apply {
                        uv(0, 0).cuboid("head", -12.0f, -79.0f, -9.0f, 24.0f, 21.0f, 16.0f, dilation)
                        uv(50, 37).cuboid("neck", -10.0f, -58.0f, -7.0f, 20.0f, 1.0f, 12.0f, dilation)
                    }, ModelTransform.NONE)
                    .addChild("body", ModelPartBuilder.create().apply {
                        uv(0, 37).cuboid("body", -8.0f, -57.0f, -5.0f, 16.0f, 41.0f, 9.0f, dilation)
                    }, ModelTransform.NONE)
                    .addChild("leg", ModelPartBuilder.create().apply {
                        uv(50, 74).cuboid("left_leg", 2.0f, -16.0f, -3.0f, 4.0f, 16.0f, 4.0f, dilation)
                        uv(66, 74).cuboid("right_leg", -6.0f, -16.0f, -3.0f, 4.0f, 16.0f, 4.0f, dilation)
                    }, ModelTransform.NONE)
                    .addChild("hand", ModelPartBuilder.create().apply {
                        uv(50, 50).cuboid("left_hand", -12.0f, -40.0f, -25.0f, 4.0f, 20.0f, 4.0f, dilation)
                        uv(66, 50).cuboid("right_hand", 8.0f, -40.0f, -25.0f, 4.0f, 20.0f, 4.0f, dilation)
                    }, ModelTransform.rotation(-0.5236f, 0.0f, 0.0f))
            }, 128, 128)

    }

    init {
        model.setPivot(0.0f, 24.0f * (1 / MODEL_SCALE), 0.0f)
    }

    override fun render(
        matrix: MatrixStack, buffer: VertexConsumer, packedLight: Int, packedOverlay: Int,
        red: Float, green: Float, blue: Float, alpha: Float
    ) {
        matrix.push()
        matrix.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE)
        model.render(matrix, buffer, packedLight, packedOverlay)
        matrix.pop()
    }

    override fun setAngles(
        entity: SCP173Entity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
    }

}

class SCP173EntityRenderer(context: EntityRendererFactory.Context) : MobEntityRenderer<SCP173Entity, SCP173EntityModel>(
    context, SCP173EntityModel(context.getPart(LAYER)), 0.5f
) {

    companion object {

        val LAYER = EntityModelLayer(SCP173Entity.IDENTIFIER, "main")
        val TEXTURE = id("textures/entity/scp173.png")

        init {
            EntityRendererRegistry.register(SCP173Entity.TYPE, ::SCP173EntityRenderer)
            EntityModelLayerRegistry.registerModelLayer(LAYER, SCP173EntityModel.Companion::createTexturedModelData)
        }

    }

    override fun getTexture(entity: SCP173Entity) = TEXTURE

}
