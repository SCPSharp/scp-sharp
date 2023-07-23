/*
 * Copyright (C) 2023  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.subject.scp173

import com.google.common.base.Predicates
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.SpawnEggItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Rarity
import net.minecraft.world.World
import scpsharp.subject.SCPSubjects
import scpsharp.util.addItem
import scpsharp.util.id

class SCP173Entity(entityType: EntityType<out SCP173Entity>, world: World) :
    scpsharp.subject.SCPEntity(entityType, world) {

    companion object {

        val IDENTIFIER = id("scp173")
        val TYPE: EntityType<SCP173Entity> = FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::SCP173Entity)
            .dimensions(
                EntityDimensions.fixed(
                    1.4f * SCP173EntityModel.MODEL_SCALE,
                    5.0f * SCP173EntityModel.MODEL_SCALE
                )
            )
            .fireImmune()
            .build()
        val EGG_ITEM_IDENTIFIER = id("scp173_spawn_egg")
        val EGG_ITEM = SpawnEggItem(
            TYPE, 0xa87550, 0x825b3f,
            FabricItemSettings().rarity(Rarity.UNCOMMON)
        )

        init {
            Registry.register(Registries.ENTITY_TYPE, IDENTIFIER, TYPE)
            Registry.register(Registries.ITEM, EGG_ITEM_IDENTIFIER, EGG_ITEM)
            SCPSubjects.ITEM_GROUP_KEY.addItem(EGG_ITEM)
            FabricDefaultAttributeRegistry.register(TYPE, createAttributes())
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

        targetSelector.add(1, RevengeGoal(this))
        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, 0, true, false, Predicates.alwaysTrue()))
        targetSelector.add(
            3,
            ActiveTargetGoal(this, LivingEntity::class.java, true)
        )
    }

    override fun canMoveVoluntarily() = super.canMoveVoluntarily() && world.canSCP173MoveNow()

    override fun setTarget(target: LivingEntity?) {
        if (target != null && target.type.isIn(IGNORE_TAG)) {
            return
        }
        super.setTarget(target)
    }

}

class SCP173AttackGoal(mob: SCP173Entity) : AttackGoal(mob) {

    override fun tick() {
        super.tick()
        if (mob.squaredDistanceTo(target.x, target.y, target.z) <= 4 * 4) {
            if (cooldown <= 3) {
                cooldown = 10
                mob.tryAttack(target)
                mob.playSound(SCP173.ROTATE_SOUND, 1.0f, 0.5f)
            }
        }
    }

}
