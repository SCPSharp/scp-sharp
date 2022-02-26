package com.xtex.scpsharp.content.scp008

import com.xtex.scpsharp.util.logger
import java.lang.invoke.MethodHandles

object SCP008 {

    val logger = logger("SCP-008")

    init {
        MethodHandles.lookup().ensureInitialized(SCP008ContainmentBlock::class.java)
        MethodHandles.lookup().ensureInitialized(SCP008StatusEffect::class.java)
    }

}

object SCP008Client {

    init {
        MethodHandles.lookup().ensureInitialized(SCP008OverlayRenderer::class.java)
    }

}
