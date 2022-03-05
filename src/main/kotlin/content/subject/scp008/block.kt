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
package com.xtex.scpsharp.content.subject.scp008

import com.xtex.scpsharp.content.subject.SCPSubjects
import com.xtex.scpsharp.util.id
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import java.util.*

object SCP008ContainmentBlock : BlockWithEntity(FabricBlockSettings.of(Material.METAL)) {

    val identifier = id("scp008_containment")
    val item = BlockItem(
        this, FabricItemSettings()
            .group(SCPSubjects.itemGroup)
    )
    val entityType: BlockEntityType<SCP008ContainmentBlockEntity> = FabricBlockEntityTypeBuilder.create(
        { pos, state -> SCP008ContainmentBlockEntity(pos, state) }, SCP008ContainmentBlock
    ).build()

    init {
        Registry.register(Registry.BLOCK, identifier, this)
        Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier, entityType)
        Registry.register(Registry.ITEM, identifier, item)
        defaultState = defaultState.with(Properties.OPEN, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(Properties.OPEN)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = SCP008ContainmentBlockEntity(pos, state)

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        world.setBlockState(pos, state.with(Properties.OPEN, !state[Properties.OPEN]))
        return ActionResult.SUCCESS
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        super.randomDisplayTick(state, world, pos, random)
        if (state[Properties.OPEN]) {
            for (i in 0..random.nextInt(10)) {
                world.addParticle(
                    ParticleTypes.CLOUD,
                    pos.x - 0.5 + random.nextFloat(2.0f),
                    pos.y + 0.7 + random.nextFloat(),
                    pos.z - 0.5 + random.nextFloat(2.0f),
                    0.0, 0.0, 0.0
                )
            }
        }
    }

}

class SCP008ContainmentBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(SCP008ContainmentBlock.entityType, pos, state)
