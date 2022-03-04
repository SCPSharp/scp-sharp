/*
 * Copyright (C) 2022  xtexChooser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
@file:Environment(EnvType.CLIENT)

package com.xtex.scpsharp.content.scp173

import com.xtex.scpsharp.util.id
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

class SCP173EntityModel(val model: ModelPart) : EntityModel<SCP173Entity>() {

    companion object {

        const val modelScale = 0.5f

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
        model.setPivot(0.0f, 24.0f * (1 / modelScale), 0.0f)
    }

    override fun render(
        matrix: MatrixStack, buffer: VertexConsumer, packedLight: Int, packedOverlay: Int,
        red: Float, green: Float, blue: Float, alpha: Float
    ) {
        matrix.push()
        matrix.scale(modelScale, modelScale, modelScale)
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
    context, SCP173EntityModel(context.getPart(layer)), 0.5f
) {

    companion object {

        val layer = EntityModelLayer(SCP173Entity.identifier, "main")
        val texture = id("textures/entity/scp173.png")

        init {
            EntityRendererRegistry.register(SCP173Entity.type, ::SCP173EntityRenderer)
            EntityModelLayerRegistry.registerModelLayer(layer, SCP173EntityModel.Companion::createTexturedModelData)
        }

    }

    override fun getTexture(entity: SCP173Entity) = SCP173EntityRenderer.texture

}
