/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.misc.permission.provider

import com.google.common.base.Stopwatch
import com.google.gson.JsonElement
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.world.World
import scpsharp.content.misc.permission.SCPPermission
import scpsharp.content.misc.permission.SCPPermissionCardItem
import scpsharp.util.id
import java.io.InputStream
import java.util.*

class SimplePermissionCardItem(id: Identifier, settings: Settings) : Item(settings), SCPPermissionCardItem {

    val providedPermissions = mutableSetOf<Identifier>()

    init {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(ReloadListener(id))
    }

    override fun getAllProvidedPermissions(world: World, stack: ItemStack) = providedPermissions

    inner class ReloadListener(val id: Identifier) : SimpleSynchronousResourceReloadListener {

        override fun reload(manager: ResourceManager) {
            val timer = Stopwatch.createStarted()
            SCPPermission.logger.info("Reloading permission list for card $id")
            providedPermissions.clear()
            manager.allNamespaces
                .map { Identifier(it, "scpsharp/permission_card/${id.namespace}/${id.path}.json") }
                .filter(manager::containsResource)
                .map(manager::getResource)
                .map(Resource::getInputStream)
                .map(InputStream::bufferedReader)
                .map { it.use(JsonHelper::deserialize) }
                .sortedBy { if(it.has("priority")) it["priority"].asInt else 0 }
                .filter { it.has("rules") }
                .map { it["rules"] }
                .flatMap(JsonElement::getAsJsonArray)
                .map(JsonElement::getAsString)
                .associateBy { it.chars().findFirst() }
                .filterKeys(OptionalInt::isPresent)
                .mapKeys { it.key.asInt }
                .mapKeys { it.key.toChar() }
                .mapValues { it.value.substring(1) }
                .mapKeys {
                    when (it.key) {
                        '+' -> providedPermissions::add
                        '-' -> providedPermissions::remove
                        else -> throw UnsupportedOperationException("Found unsupported operator ${it.key} for card $id")
                    }
                }
                .mapValues { Identifier(it.value) }
                .forEach { (operator, identifier) -> operator(identifier) }
            timer.stop()
            SCPPermission.logger.info("Finished reloading card $id with ${timer.elapsed()}")
            SCPPermission.logger.info("Permission card $id now is able to provide these permissions: $providedPermissions")
        }

        override fun getFabricId() = id("reload_permission_card_" + id.toUnderscoreSeparatedString())

    }

}
