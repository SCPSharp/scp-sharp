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
