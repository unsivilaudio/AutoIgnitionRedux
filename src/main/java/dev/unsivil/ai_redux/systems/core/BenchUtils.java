package dev.unsivil.ai_redux.systems.core;

import javax.annotation.Nullable;

import org.joml.Vector3i;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.asset.type.blockhitbox.BlockBoundingBoxes;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.ChunkFlag;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;


public abstract class BenchUtils {
    
    public class Math {
        
        /**
        * Calculates the offset for the secondary part of a multi-block bench.
        * @param rotationIndex The Hytale rotation index (0: South, 1: East, 2: North, 3: West).
        * @return A {@link Vector3i} representing the relative offset, or null if the index is invalid.
        */
        public static Vector3i getSecondBenchBlockOffset(int rotationIndex) {
            return switch (rotationIndex) {
                case 0 -> new Vector3i(-1, 0, 0);
                case 1 -> new Vector3i(0, 0, 1);
                case 2 -> new Vector3i(1, 0, 0);
                case 3 -> new Vector3i(0, 0, -1);
                default -> null;
            };
        }
        
        /**
         * Computes a new world position by adding an offset to a base position.
         * @param base The starting world position (the master block).
         * @param offset The relative displacement to apply.
         * @return A new {@link Vector3i} representing the calculated target position.
         */
        public static Vector3i getRelativePosition(Vector3i base, Vector3i offset) {
            return new Vector3i(
                base.x + offset.x,
                base.y + offset.y,
                base.z + offset.z
            );
        }
    }
    
    public class Chunk {
        
        /**
        * Retrieves a WorldChunk only if it is fully loaded and ready for ticking.
        * @param world The world instance.
        * @param position The block position to check.
        * @return The {@link WorldChunk} instance if safe to use, null otherwise.
        */
        @Nullable
        public static WorldChunk getSafeChunkFromBlock(World world, Vector3i position) {
            ChunkStore chunkStore = world.getChunkStore();
            long index = ChunkUtil.indexChunkFromBlock(position.x, position.z);
            
            Ref<ChunkStore> ref = chunkStore.getChunkReference(index);
            
            if (ref == null || !ref.isValid()) return null;
            
            WorldChunk chunk = ref.getStore().getComponent(ref, WorldChunk.getComponentType());
            
            if (chunk == null || !chunk.is(ChunkFlag.TICKING)) return null;
            
            return chunk;
        }
    }
    
    public class Block {
        
        /**
        * Determines if a bench is a multi-block structure by inspecting its hitboxes.
        * This allows for automatic compatibility with any modded bench without hardcoding IDs.
        * @param blockType The processing block type to check.
        * @return true if the block's hitbox width or depth exceeds a standard 1x1 tile (1.1 threshold).
        */
        // @SuppressWarnings("removal")
        public static boolean isMultiBlock(BlockType blockType) {
            if (blockType == null) return false;
            
            var hitboxAssetMap = BlockBoundingBoxes.getAssetMap();
            var hitboxAsset = hitboxAssetMap.getAsset(blockType.getHitboxTypeIndex());
            
            if (hitboxAsset != null) {
                var box = hitboxAsset.get(0).getBoundingBox();
                return box.width() > 1.1 || box.depth() > 1.1;
            }
            return false;
        }
    }
}
