package scpsharp.content.facility

import com.mojang.datafixers.util.Either
import net.minecraft.structure.StructurePiecesCollector
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.Heightmap
import net.minecraft.world.gen.structure.Structure
import scpsharp.util.logger
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

abstract class FacilityStructure(config: Config) : Structure(config) {

    companion object {

        val LOGGER = logger("Facility")

        @OptIn(ExperimentalTime::class)
        @JvmStatic
        protected fun getStructurePosition(
            id: Identifier,
            ctx: Context,
            heightmap: Heightmap.Type = Heightmap.Type.WORLD_SURFACE_WG,
            generator: FacilityGenerator.(pos: BlockPos, direction: Direction) -> Boolean
        ): Optional<StructurePosition> {
            val chunkPos = ctx.chunkPos()
            val y = ctx.chunkGenerator()
                .getHeightInGround(
                    chunkPos.centerX, chunkPos.centerZ, heightmap,
                    ctx.world(), ctx.noiseConfig()
                )
            val pos = BlockPos(chunkPos.centerX, y, chunkPos.centerZ)
            val collector = StructurePiecesCollector()
            val generatorContext = FacilityGenerator(ctx)
            val (success, time) = measureTimedValue {
                generatorContext.generator(
                    pos,
                    Direction.Type.HORIZONTAL.random(ctx.random)
                )
            }
            LOGGER.trace("Resolved $id at ${pos.toShortString()} in $time")
            return if (success) {
                generatorContext.collect(collector)
                Optional.of(StructurePosition(pos, Either.right(collector)))
            } else {
                Optional.empty()
            }
        }

    }

}