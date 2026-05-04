package dev.unsivil.ai_redux.systems.inventory;

import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.StateData;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;

import dev.unsivil.ai_redux.cache.BenchCache;
import dev.unsivil.ai_redux.systems.core.BenchUtils;


/**
 * Provides spatial scanning logic to identify neighboring storage containers.
 */
public class NeighborScanner {
    
    /** Cardinal horizontal offsets for neighbor checking */
    private static final Vector3i[] HORIZONTAL_OFFSETS = {
            new Vector3i(1, 0, 0),
            new Vector3i(-1, 0, 0),
            new Vector3i(0, 0, 1),
            new Vector3i(0, 0, -1)
    };
    
    /**
     * Entry point for scanning containers around a specific bench.
     * It clears the existing cache and performs a fresh scan of all relevant blocks.
     * @param world The world instance.
     * @param position The master block position of the bench.
     * @param cache The cache to store discovered container positions.
     * @param blockType The block type performing the scan.
     */
    public static void scan(World world, Vector3i position, BenchCache cache, BlockType blockType) {
        cache.updateScanTime();
        cache.getContainerPositions().clear();
        
        scanAt(world, position, cache);
        
        if (BenchUtils.Block.isMultiBlock(blockType)) {
            Vector3i offset = BenchUtils.Math.getSecondBenchBlockOffset(blockType.getRotationYawPlacementOffset().ordinal());
            if (offset != null) {
                Vector3i secondPartPosition = BenchUtils.Math.getRelativePosition(position, offset);
                
                WorldChunk chunk = BenchUtils.Chunk.getSafeChunkFromBlock(world, secondPartPosition);
                if (chunk != null && isSameBenchType(blockType, chunk, secondPartPosition)) {
                    scanAt(world, secondPartPosition, cache);
                }
            }
        }
    }
    
    /**
     * Verifies if a block at a given position is part of the same bench instance.
     * @param blockType The original block type to compare with.
     * @param chunk The chunk containing the block to check.
     * @param position The world position of the block to check.
     * @return true if the block shares the same ID.
     */
    private static boolean isSameBenchType(BlockType blockType, WorldChunk chunk, Vector3i position) {
        BlockType secondPartType = chunk.getBlockType(position);
        if (secondPartType == null || blockType == null) return false;
        return secondPartType.getId().equals(blockType.getId());
    }
    
    /**
     * Scans all 4 cardinal directions around a specific position for valid containers.
     * @param world The world instance.
     * @param position The central position to scan around.
     * @param cache The cache to populate.
     */
    private static void scanAt(World world, Vector3i position, BenchCache cache) {
        for (Vector3i offset : HORIZONTAL_OFFSETS) {
            Vector3i neighborPosition = BenchUtils.Math.getRelativePosition(position, offset);
            
            WorldChunk chunk = BenchUtils.Chunk.getSafeChunkFromBlock(world, neighborPosition);
            if (chunk == null) continue;
            
            BlockType neighborType = chunk.getBlockType(neighborPosition);
            if (neighborType != null && neighborType.getId().matches("(?i).*chest.*") &&
                neighborType.getState() instanceof StateData) {
                if (!cache.getContainerPositions().contains(neighborPosition)) {
                    cache.getContainerPositions().add(neighborPosition);
                }
                
                if (neighborType.getId().contains("Large")) {
                    scanSecondChestPart(world, neighborPosition, cache, neighborType.getState());
                }
            }
        }
    }
    
    /**
     * Specifically scans for the second part of a multi-block container
     * based on its shared StateData.
     * @param world The world instance.
     * @param position The position of the first part of the container.
     * @param cache The cache to populate.
     * @param originalState The {@link StateData} of the first part to find its match.
     */
    private static void scanSecondChestPart(World world, Vector3i position, BenchCache cache, StateData originalState) {
        for (Vector3i offset : HORIZONTAL_OFFSETS) {
            Vector3i neighborPosition = BenchUtils.Math.getRelativePosition(position, offset);
            WorldChunk chunk = BenchUtils.Chunk.getSafeChunkFromBlock(world, neighborPosition);
            
            if (chunk != null) {
                BlockType neighborType = chunk.getBlockType(neighborPosition);
                if (neighborType != null && originalState.equals(neighborType.getState())) {
                    if (!cache.getContainerPositions().contains(neighborPosition)) {
                        cache.getContainerPositions().add(neighborPosition);
                    }
                }
            }
        }
    }
}
