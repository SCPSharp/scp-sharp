package com.xtex.scpsharp.content.subject

import com.xtex.scpsharp.content.scp005.SCP005
import com.xtex.scpsharp.content.scp008.SCP008
import com.xtex.scpsharp.content.scp008.SCP008Client
import com.xtex.scpsharp.content.scp173.SCP173
import com.xtex.scpsharp.content.scp173.SCP173Client
import com.xtex.scpsharp.content.scp427.SCP427
import com.xtex.scpsharp.content.scp500.SCP500
import com.xtex.scpsharp.content.scp714.SCP714
import com.xtex.scpsharp.content.scp914.SCP914

object SCPSubjects {

    init {
        SCP005
        SCP008
        SCP173
        SCP427
        SCP500
        SCP714
        SCP914
    }

}

object SCPSubjectsClient {

    init {
        SCP008Client
        SCP173Client
    }

}
