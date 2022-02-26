/*
 * Copyright (C) 2022  xtexChooser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.xtex.scpsharp.content.scp008

import com.xtex.scpsharp.content.scpSubjectItemGroup
import com.xtex.scpsharp.util.id
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

object SCP008ContainmentBlock : BlockWithEntity(FabricBlockSettings.of(Material.METAL)) {

    val identifier = id("scp008_containment")
    val item = BlockItem(
        this, FabricItemSettings()
            .group(scpSubjectItemGroup)
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

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = SCP008ContainmentBlockEntity(pos, state)

}

class SCP008ContainmentBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(SCP008ContainmentBlock.entityType, pos, state)
