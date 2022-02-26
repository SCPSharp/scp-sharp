package com.xtex.scpsharp.content.scp500

import com.xtex.scpsharp.util.logger
import java.lang.invoke.MethodHandles

object SCP500 {

    val logger = logger("SCP-500")

    init {
        MethodHandles.lookup().ensureInitialized(SCP5001Item::class.java)
        MethodHandles.lookup().ensureInitialized(SCP500JarItem::class.java)
    }

}
