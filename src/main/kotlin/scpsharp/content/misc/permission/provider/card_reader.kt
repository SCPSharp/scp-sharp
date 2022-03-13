/*
 * Copyright (C) 2022  SCPSharp Team
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
@file:Suppress("DEPRECATION")

package scpsharp.content.misc.permission.provider

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtString
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import scpsharp.content.misc.SCPMisc
import scpsharp.content.misc.permission.SCPPermission
import scpsharp.content.misc.permission.SCPPermissionCardItem
import scpsharp.content.misc.permission.SCPPermissionEmitterBlock
import scpsharp.util.id
import java.util.*

object CardReaderBlock : BlockWithEntity(
    FabricBlockSettings.of(Material.METAL)
        .strength(2f)
), SCPPermissionEmitterBlock {

    val identifier = id("card_reader")
    val item = BlockItem(
        this,
        FabricItemSettings()
            .group(SCPMisc.itemGroup)
    )
    val entityType: BlockEntityType<CardReaderBlockEntity> =
        FabricBlockEntityTypeBuilder.create(::CardReaderBlockEntity, this).build()

    init {
        Registry.register(Registry.BLOCK, identifier, this)
        Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier, entityType)
        Registry.register(Registry.ITEM, identifier, item)
    }

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = CardReaderBlockEntity(pos, state)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (!world.isClient) {
            val item = player.getStackInHand(hand)
            if (item.item is SCPPermissionCardItem) {
                val blockEntity = world.getBlockEntity(pos) as CardReaderBlockEntity
                blockEntity.permissions.addAll(
                    (item.item as SCPPermissionCardItem).getAllProvidedPermissions(world, item)
                )
                blockEntity.markDirty()
                SCPPermission.logger.info("$player applied $item to card reader at $world $pos")
                SCPPermission.updateDoubleNeighbors(world, pos)
                world.createAndScheduleBlockTick(pos, this, 20 * 2)
                return ActionResult.SUCCESS
            }
        }
        return super.onUse(state, world, pos, player, hand, hit)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super.scheduledTick(state, world, pos, random)
        val blockEntity = world.getBlockEntity(pos) as CardReaderBlockEntity
        blockEntity.permissions.clear()
        blockEntity.markDirty()
        SCPPermission.logger.info("Card reader at $world $pos closed")
        SCPPermission.updateDoubleNeighbors(world, pos)
    }

    override fun getAllProvidedPermissions(world: World, pos: BlockPos, state: BlockState) =
        (world.getBlockEntity(pos) as CardReaderBlockEntity).permissions

}

class CardReaderBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(CardReaderBlock.entityType, pos, state) {

    val permissions = mutableSetOf<Identifier>()

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        if (nbt.contains("Permissions")) {
            permissions.clear()
            permissions.addAll(
                nbt.getList("Permissions", NbtElement.STRING_TYPE.toInt())
                    .map(NbtElement::asString)
                    .map(::Identifier)
            )
        }
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        val list = nbt.getList("Permissions", NbtElement.STRING_TYPE.toInt())
        list.clear()
        list.addAll(
            permissions.map(Identifier::toString)
                .map(NbtString::of)
        )
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = createNbt()

}
