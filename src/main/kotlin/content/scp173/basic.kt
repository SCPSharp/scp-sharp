package com.xtex.scpsharp.content.scp173

import java.lang.invoke.MethodHandles

object SCP173 {

    init {
        MethodHandles.lookup().ensureInitialized(SCP173Entity::class.java)
    }

}

object SCP173Client {

    init {
        MethodHandles.lookup().ensureInitialized(SCP173EntityRenderer::class.java)
    }

}
