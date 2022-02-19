package com.xtex.scpsharp

import com.xtex.scpsharp.content.scp914.SCP914
import com.xtex.scpsharp.content.scp914.SCP914ControllerBlock
import com.xtex.scpsharp.content.scp914.SCP914FrameworkBlock
import com.xtex.scpsharp.content.scp914.SCP914Recipe
import java.lang.invoke.MethodHandles

fun initMain() {
    MethodHandles.lookup().ensureInitialized(SCP914::class.java)
}

fun initClient() {

}
