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
package com.xtex.scpsharp.content.scp173

import com.google.common.base.Predicates
import com.xtex.scpsharp.content.SCPEntity
import com.xtex.scpsharp.content.SCPIgnoredEntity
import com.xtex.scpsharp.content.scpSubjectItemGroup
import com.xtex.scpsharp.mixin.AttackGoalAccessor
import com.xtex.scpsharp.util.id
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.SpawnEggItem
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class SCP173Entity(entityType: EntityType<out SCP173Entity>, world: World) : SCPEntity(entityType, world) {

    companion object {

        val identifier = id("scp173")
        val type: EntityType<SCP173Entity> = FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::SCP173Entity)
            .dimensions(
                EntityDimensions.fixed(
                    1.4f * SCP173EntityModel.modelScale,
                    5.0f * SCP173EntityModel.modelScale
                )
            )
            .fireImmune()
            .build()
        val eggItemIdentifier = id("scp173_spawn_egg")
        val eggItem = SpawnEggItem(
            type, 0xa87550, 0x825b3f, FabricItemSettings()
                .group(scpSubjectItemGroup)
                .rarity(Rarity.UNCOMMON)
        )

        init {
            Registry.register(Registry.ENTITY_TYPE, identifier, type)
            Registry.register(Registry.ITEM, eggItemIdentifier, eggItem)
            FabricDefaultAttributeRegistry.register(type, createAttributes())
        }

        @JvmStatic
        fun createAttributes(): DefaultAttributeContainer.Builder = createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 150.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 7.0)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 70.0)

    }

    override fun initGoals() {
        super.initGoals()
        goalSelector.add(1, SCP173AttackGoal(this))
        goalSelector.add(2, LookAtEntityGoal(this, PlayerEntity::class.java, 20.0f))
        goalSelector.add(3, SwimGoal(this))
        goalSelector.add(4, WanderAroundGoal(this, 1.0))
        goalSelector.add(4, LookAroundGoal(this))

        targetSelector.add(1, RevengeGoal(this, SCPIgnoredEntity::class.java))
        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, 0, true, false, Predicates.alwaysTrue()))
        targetSelector.add(
            3,
            ActiveTargetGoal(this, LivingEntity::class.java, 0, true, true) { it !is SCPIgnoredEntity })
    }

    override fun canMoveVoluntarily() = super.canMoveVoluntarily() && world.canSCP173MoveNow()

}

class SCP173AttackGoal(private val mob: SCP173Entity) : AttackGoal(mob) {

    override fun tick() {
        super.tick()
        @Suppress("CAST_NEVER_SUCCEEDS") val accessor = this as AttackGoalAccessor
        if (this.mob.squaredDistanceTo(
                accessor.getTarget().x,
                accessor.getTarget().y,
                accessor.getTarget().z
            ) <= 4 * 4
        ) {
            if (accessor.getCoolDown() <= 3) {
                accessor.setCoolDown(10)
                mob.tryAttack(accessor.getTarget())
                @Suppress("CAST_NEVER_SUCCEEDS")
                mob.playSound(SCP173.rotateSound, 1.0f, 0.5f)
            }
        }
    }

}
