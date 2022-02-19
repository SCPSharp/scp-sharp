package com.xtex.scpsharp.content.scp914

import com.xtex.scpsharp.util.id
import com.xtex.scpsharp.util.logger
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.registry.Registry
import java.lang.invoke.MethodHandles

object SCP914 {

    val logger = logger("SCP-914")

    val modeProperty: EnumProperty<SCP914Mode> = EnumProperty.of("mode", SCP914Mode::class.java)

    val workingSoundEvent = SoundEvent(id("scp914_working"))

    init {
        MethodHandles.lookup().ensureInitialized(SCP914FrameworkBlock::class.java)
        MethodHandles.lookup().ensureInitialized(SCP914ControllerBlock::class.java)
        MethodHandles.lookup().ensureInitialized(SCP914Recipe::class.java)
        Registry.register(Registry.SOUND_EVENT, workingSoundEvent.id, workingSoundEvent)
    }

}

enum class SCP914Mode: StringIdentifiable {

    VERY_BAD, BAD, NORMAL, FINE, VERY_FINE;

    override fun asString() = name.lowercase()

    val next get() = when(this) {
        VERY_BAD -> BAD
        BAD -> NORMAL
        NORMAL -> FINE
        FINE -> VERY_FINE
        VERY_FINE -> VERY_BAD
    }

}
