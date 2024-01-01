/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
@file:Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")

package scpsharp.misc.permission.provider

import com.mojang.serialization.MapCodec
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtString
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import scpsharp.misc.SCPMisc
import scpsharp.misc.permission.SCPPermission
import scpsharp.misc.permission.SCPPermissionCardItem
import scpsharp.misc.permission.SCPPermissionEmitterBlock
import scpsharp.util.addItem
import scpsharp.util.id

class CardReaderBlock(settings: Settings) : BlockWithEntity(settings), SCPPermissionEmitterBlock {

    companion object {
        val IDENTIFIER = id("card_reader")
        val BLOCK = CardReaderBlock(
            FabricBlockSettings.create()
                .mapColor(MapColor.LIGHT_GRAY)
                .strength(2f)
        )
        val ITEM = BlockItem(BLOCK, FabricItemSettings())
        val ENTITY_TYPE: BlockEntityType<CardReaderBlockEntity> =
            FabricBlockEntityTypeBuilder.create(::CardReaderBlockEntity, BLOCK).build()
        val CODEC: MapCodec<CardReaderBlock> = createCodec(::CardReaderBlock)

        init {
            Registry.register(Registries.BLOCK, IDENTIFIER, BLOCK)
            Registry.register(Registries.BLOCK_ENTITY_TYPE, IDENTIFIER, ENTITY_TYPE)
            Registry.register(Registries.ITEM, IDENTIFIER, ITEM)
            SCPMisc.ITEM_GROUP_KEY.addItem(ITEM)
        }

        private fun createVoxelShape(state: BlockState): VoxelShape =
            when (state[Properties.HORIZONTAL_FACING]) {
                Direction.WEST -> VoxelShapes.combineAndSimplify(
                    createCuboidShape(13.0, 6.0, 9.0, 14.0, 10.0, 10.0),
                    createCuboidShape(14.0, 5.0, 6.0, 16.0, 11.0, 10.0),
                    BooleanBiFunction.OR
                )

                Direction.SOUTH -> VoxelShapes.combineAndSimplify(
                    createCuboidShape(9.0, 6.0, 2.0, 10.0, 10.0, 3.0),
                    createCuboidShape(6.0, 5.0, 0.0, 10.0, 11.0, 2.0),
                    BooleanBiFunction.OR
                )

                Direction.EAST -> VoxelShapes.combineAndSimplify(
                    createCuboidShape(2.0, 6.0, 6.0, 3.0, 10.0, 7.0),
                    createCuboidShape(0.0, 5.0, 6.0, 2.0, 11.0, 10.0),
                    BooleanBiFunction.OR
                )

                Direction.NORTH -> VoxelShapes.combineAndSimplify(
                    createCuboidShape(6.0, 6.0, 13.0, 7.0, 10.0, 14.0),
                    createCuboidShape(6.0, 5.0, 14.0, 10.0, 11.0, 16.0),
                    BooleanBiFunction.OR
                )

                else -> throw IllegalStateException(state[Properties.HORIZONTAL_FACING].name)
            }
    }

    private val shapes: Map<BlockState, VoxelShape> = getShapesForStates(CardReaderBlock::createVoxelShape)

    init {
        this.defaultState = defaultState.with(Properties.HORIZONTAL_FACING, Direction.WEST)
    }

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) =
        shapes[state]

    override fun getCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) =
        shapes[state]

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(Properties.HORIZONTAL_FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        if (ctx.side.axis == Direction.Axis.Y) return null
        if (ctx.world.getBlockState(ctx.blockPos.offset(ctx.side.opposite)).isAir) return null
        return super.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.side)
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack) {
        super.onPlaced(world, pos, state, placer, itemStack)
        neighborUpdate(state, world, pos, this, pos, false)
    }

    override fun getCodec() = CODEC

    @Deprecated("Deprecated in Java")
    override fun neighborUpdate(
        state: BlockState,
        world: World,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        notify: Boolean
    ) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify)
        if (world.getBlockState(pos.offset(state[Properties.HORIZONTAL_FACING].opposite)).isAir)
            world.breakBlock(pos, true)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = CardReaderBlockEntity(pos, state)

    @Deprecated("Deprecated in Java")
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
                SCPPermission.updateDoubleNeighbors(world, pos)
                world.scheduleBlockTick(pos, this, 20 * 2)
                return ActionResult.SUCCESS
            }
        }
        return super.onUse(state, world, pos, player, hand, hit)
    }

    @Deprecated("Deprecated in Java")
    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super.scheduledTick(state, world, pos, random)
        val blockEntity = world.getBlockEntity(pos) as CardReaderBlockEntity
        blockEntity.permissions.clear()
        blockEntity.markDirty()
        SCPPermission.updateDoubleNeighbors(world, pos)
    }

    override fun getAllProvidedPermissions(world: World, pos: BlockPos, state: BlockState) =
        (world.getBlockEntity(pos) as CardReaderBlockEntity).permissions

}

class CardReaderBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(CardReaderBlock.ENTITY_TYPE, pos, state) {

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
