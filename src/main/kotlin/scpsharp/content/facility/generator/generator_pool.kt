/*
 * Copyright (C) 2022  SCPSharp Team
 *
 * This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License. See LICENSE file for more.
 */
package scpsharp.content.facility.generator

import com.google.common.collect.Queues
import net.minecraft.world.gen.feature.util.FeatureContext
import scpsharp.util.logger
import java.util.*

object FacilityGeneratorPool {

    const val extendedStackSize = 1024L * 1024L * 16
    const val generationTimeout = 1000L * 60L * 2

    val queueNotifier = Object()
    private val queue: Queue<Request> = Queues.synchronizedQueue(Queues.newConcurrentLinkedQueue<Request>())
    val threadGroup = ThreadGroup("SCP# Pooled FacGen")
    val threads: Set<Thread>
    val logger = logger("PooledFacGen")

    init {
        logger.info("Launching pooled generators")
        val mutableThreads = mutableListOf<Thread>()
        for (i in 1..4) {
            mutableThreads.add(Thread(threadGroup, {
                while (true) {
                    while (true) {
                        val request = queue.poll()
                        if (request == null) {
                            break
                        } else {
                            try {
                                request.result = FacilityGenerator(request.context).tryRandomGenerate(request.factory)
                            } catch (e: Throwable) {
                                throw RuntimeException("Error handling $request", e)
                            }
                            synchronized(request.notifier) {
                                request.notifier.notifyAll()
                            }
                        }
                    }
                    synchronized(queueNotifier) {
                        queueNotifier.wait()
                    }
                }
            }, "SCP# Polled Facility Generator #$i", extendedStackSize))
        }
        threads = mutableThreads.toSet()
        threads.forEach {
            it.isDaemon = true
            it.start()
        }
    }

    fun request(context: FeatureContext<*>, factory: ComponentFactory<*>): Boolean {
        val request = Request(context, factory)
        queue.offer(request)
        synchronized(queueNotifier) {
            queueNotifier.notify()
        }
        synchronized(request.notifier) {
            request.notifier.wait(generationTimeout)
        }
        return request.result
    }

    private data class Request(
        val context: FeatureContext<*>,
        val factory: ComponentFactory<*>,
        var result: Boolean = false,
        @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN") val notifier: Object = Object()
    )

}