package scpsharp.content.facility

import com.mojang.datafixers.util.Either
import net.minecraft.structure.StructurePiecesCollector
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.Heightmap
import net.minecraft.world.gen.structure.Structure
import java.util.*

abstract class FacilityStructure(config: Config) : Structure(config) {

    companion object {

        @JvmStatic
        protected fun getStructurePosition(
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
            return if (generatorContext.generator(pos, Direction.random(ctx.random))) {
                generatorContext.collect(collector)
                Optional.of(StructurePosition(pos, Either.right(collector)))
            } else {
                Optional.empty()
            }
        }

    }

}