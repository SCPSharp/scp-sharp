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
package com.xtex.scpsharp.content.scp914

import com.xtex.scpsharp.content.scpSubjectItemGroup
import com.xtex.scpsharp.util.id
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraft.world.WorldView
import java.util.*
import kotlin.math.abs

object SCP914FrameworkBlock : Block(
    FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY)
        .strength(-1f)
) {

    val identifier = id("scp914_framework")

    init {
        Registry.register(Registry.BLOCK, identifier, SCP914FrameworkBlock)
    }

}

object SCP914ControllerBlock : Block(
    FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY)
        .strength(8f, 10f)
) {

    val identifier = id("scp914_controller")
    val item = BlockItem(
        SCP914ControllerBlock, FabricItemSettings()
            .group(scpSubjectItemGroup)
            .rarity(Rarity.UNCOMMON)
    )

    init {
        Registry.register(Registry.BLOCK, identifier, SCP914ControllerBlock)
        Registry.register(Registry.ITEM, identifier, item)
        defaultState = defaultState.with(SCP914.modeProperty, SCP914Mode.NORMAL)
            .with(Properties.POWERED, false)
            .with(Properties.AGE_3, 0)
    }

    fun getStructureOffsetRange(facing: Direction) = if (facing.opposite.axis == Direction.Axis.X) {
        val xWidth = facing.opposite.offsetX * 3
        val zWidth = abs(facing.opposite.offsetX * 4)
        (if (xWidth > 0) 0..xWidth else xWidth..0) to -zWidth..zWidth
    } else {
        val xWidth = abs(facing.opposite.offsetZ * 4)
        val zWidth = facing.opposite.offsetZ * 3
        -xWidth..xWidth to (if (zWidth > 0) 0..zWidth else zWidth..0)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(Properties.HORIZONTAL_FACING)
            .add(SCP914.modeProperty)
            .add(Properties.POWERED)
            .add(Properties.AGE_3)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return super.getDefaultState()
            .with(Properties.HORIZONTAL_FACING, ctx.playerFacing.opposite)
    }

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        super.onStateReplaced(state, world, pos, newState, moved)
        if (!newState.isOf(this)) {
            // Destroy
            val (xRange, zRange) = getStructureOffsetRange(state[Properties.HORIZONTAL_FACING])
            for (y in -2..2) {
                for (x in xRange) {
                    for (z in zRange) {
                        world.breakBlock(pos.add(x, y, z), false)
                    }
                }
            }
        } else {
            when (newState[Properties.AGE_3]) {
                1 -> world.createAndScheduleBlockTick(pos, this, 30) // Closing door
                2 -> world.createAndScheduleBlockTick(pos, this, 10) // Activating
                3 -> world.createAndScheduleBlockTick(pos, this, 30) // Opening door
            }
        }
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val (xRange, zRange) = getStructureOffsetRange(state[Properties.HORIZONTAL_FACING])
        for (y in -2..2) {
            for (x in xRange) {
                for (z in zRange) {
                    val offsetPos = pos.add(x, y, z)
                    val block = world.getBlockState(offsetPos)
                    val hardness = block.getHardness(world, offsetPos)
                    if (!block.isAir && (y >= 0 || !(hardness >= 0f && hardness < 5f))) {
                        return false
                    }
                }
            }
        }
        return super.canPlaceAt(state, world, pos)
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super.onBlockAdded(state, world, pos, oldState, notify)
        if (!oldState.isOf(this)) {
            val facing = state[Properties.HORIZONTAL_FACING]
            val (xRange, zRange) = getStructureOffsetRange(facing)
            for (y in -2..2) {
                for (x in xRange) {
                    for (z in zRange) {
                        if (x == 0 && y == 0 && z == 0) {
                            continue
                        }
                        val offsetPos = pos.add(x, y, z)
                        if (!world.getBlockState(offsetPos).isAir) {
                            world.breakBlock(offsetPos, true)
                        }
                        if ((x == xRange.first || x == xRange.last
                                    || z == zRange.first || z == zRange.last
                                    || y == -2 || y == 2
                                    || x == 0 || z == 0)
                            && ((facing.axis == Direction.Axis.X
                                    && !(abs(z) <= 2 && z != 0 && x == 0 && (y != 2 && y != -2)))
                                    || (facing.axis == Direction.Axis.Z
                                    && !(abs(x) <= 2 && x != 0 && z == 0 && (y != 2 && y != -2))))
                        ) {
                            world.setBlockState(offsetPos, SCP914FrameworkBlock.defaultState)
                        }
                    }
                }
            }
        }
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (world.isClient) {
            return ActionResult.SUCCESS
        }
        world.setBlockState(pos, state.with(SCP914.modeProperty, state[SCP914.modeProperty].next))
        player.sendMessage(
            TranslatableText(
                "scpsharp.scp914.switched",
                TranslatableText("scpsharp.scp914.mode.${state[SCP914.modeProperty].next.asString()}")
            ), true
        )
        return ActionResult.SUCCESS
    }

    override fun neighborUpdate(
        state: BlockState,
        world: World,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        notify: Boolean
    ) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify)
        val isReceivingRedstonePower = world.isReceivingRedstonePower(pos)
        if (state[Properties.POWERED]) {
            if (!isReceivingRedstonePower) {
                world.setBlockState(pos, state.with(Properties.POWERED, false))
            }
        } else {
            if (state[Properties.AGE_3] == 0 && isReceivingRedstonePower) {
                // Start activating
                world.setBlockState(
                    pos, state.with(Properties.AGE_3, 1)
                        .with(Properties.POWERED, true)
                )
            }
        }
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super.scheduledTick(state, world, pos, random)
        when (state[Properties.AGE_3]) {
            1 -> {
                closeDoor(state, world, pos)
                SCP914.logger.info("Door of $pos closed")
                world.setBlockState(pos, state.with(Properties.AGE_3, 2))
            }
            2 -> {
                world.playSound(null, pos, SCP914.workingSoundEvent, SoundCategory.MASTER, 1.0f, 1.0f)
                activate(state, world, pos)
                world.setBlockState(pos, state.with(Properties.AGE_3, 3))
            }
            3 -> {
                openDoor(state, world, pos)
                SCP914.logger.info("Door of $pos open")
                world.setBlockState(pos, state.with(Properties.AGE_3, 0))
            }
        }
    }

    fun activate(state: BlockState, world: World, pos: BlockPos) {
        val mode = state[SCP914.modeProperty]
        SCP914.logger.info("Activated at $world $pos with $mode")
        val facing = state[Properties.HORIZONTAL_FACING].opposite
        val rVec = BlockPos(facing.rotateCounterclockwise(Direction.Axis.Y).vector).multiply(-1)
        val lVec = BlockPos(facing.rotateCounterclockwise(Direction.Axis.Y).vector).multiply(4)
        val fVec = BlockPos(facing.vector)
        val bVec = BlockPos(facing.vector).multiply(3)
        val p1 = Vec3d(pos.x + lVec.x + bVec.x.toDouble(), pos.y - 3.0, pos.z + lVec.z + bVec.z.toDouble())
        val p2 = Vec3d(pos.x + rVec.x + fVec.x.toDouble(), pos.y + 3.0, pos.z + rVec.z + fVec.z.toDouble())
        world.getEntitiesByClass(Entity::class.java, Box(p1, p2)) {
            it is ItemEntity || it is PlayerEntity
        }.forEach {
            if (it is ItemEntity) {
                it.stack = processItem(mode, it.stack, world)
            } else if (it is PlayerEntity) {
                val slot = it.inventory.selectedSlot
                it.inventory.setStack(slot, processItem(mode, it.inventory.getStack(slot), world))
            }
            val targetPos = it.pos.add(Vec3d.of(facing.rotateClockwise(Direction.Axis.Y).vector.multiply(4)))
            it.teleport(targetPos.x, targetPos.y, targetPos.z)
        }
    }

    fun openDoor(state: BlockState, world: World, pos: BlockPos) =
        replaceDoor(state, world, pos, Blocks.AIR.defaultState)

    fun closeDoor(state: BlockState, world: World, pos: BlockPos) =
        replaceDoor(state, world, pos, SCP914FrameworkBlock.defaultState)

    fun replaceDoor(state: BlockState, world: World, pos: BlockPos, doorState: BlockState) {
        val facing = state[Properties.HORIZONTAL_FACING]
        val (xRange, zRange) = if (facing.axis == Direction.Axis.X) {
            0..0 to -2..2
        } else {
            -2..2 to 0..0
        }
        for (y in -1..1) {
            for (x in xRange) {
                for (z in zRange) {
                    if ((facing.axis == Direction.Axis.X || x != 0) && (facing.axis == Direction.Axis.Z || z != 0)) {
                        world.setBlockState(pos.add(x, y, z), doorState)
                    }
                }
            }
        }
    }

    fun processItem(mode: SCP914Mode, item: ItemStack, world: World): ItemStack {
        SCP914.logger.info("Processing $item with $mode")
        val inventory = SimpleInventory(
            when (mode) {
                SCP914Mode.VERY_BAD -> processItem(SCP914Mode.BAD, item, world)
                SCP914Mode.FINE -> processItem(SCP914Mode.NORMAL, item, world)
                SCP914Mode.VERY_FINE -> processItem(SCP914Mode.FINE, item, world)
                else -> item
            }
        )
        val recipe = world.recipeManager.getAllMatches(SCP914Recipe.type, inventory, world)
            .filter { (mode == SCP914Mode.VERY_BAD || mode == SCP914Mode.BAD) || it.source.test(inventory.getStack(0)) }
            .firstOrNull {
                (mode != SCP914Mode.VERY_BAD && mode != SCP914Mode.BAD) || it.target.test(
                    inventory.getStack(
                        0
                    )
                )
            }
        if (recipe == null) {
            SCP914.logger.info("No recipe found for ${inventory.getStack(0)}")
        }
        return recipe?.craft(inventory) ?: inventory.getStack(0).copy()
    }

}
