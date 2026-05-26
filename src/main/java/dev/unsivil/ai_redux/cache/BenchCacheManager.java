package dev.unsivil.ai_redux.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Vector3i;

import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.world.World;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.config.AutoIgnitionReduxConfig;
import dev.unsivil.ai_redux.systems.inventory.NeighborScanner;


/**
 * Global manager for bench caches.
 * Handles lifecycle, retrieval, and periodic cleanup of {@link BenchCache} instances
 * to ensure high performance and prevent memory leaks.
 */
public class BenchCacheManager {
    
    public static final int DEFAULT_RATE_LIMIT = 500;
    
    /** Thread-safe storage mapping block positions to their respective caches. */
    private static final Map<Vector3i, BenchCache> CACHE_MAP = new ConcurrentHashMap<>();
    
    /**
     * Retrieves an existing cache for a bench or creates a new one if it doesn't exist.
     * If the cache exists but is outdated based on the scanner interval, it triggers a re-scan.
     * @param position The world coordinates of the bench.
     * @param world The world instance required for neighborhood scanning.
     * @param blockType The processing bench state instance.
     * @return The up-to-date {@link BenchCache} for the given position.
     */
    public static BenchCache getOrCreate(Vector3i position, World world, BlockType blockType) {
        BenchCache cache = CACHE_MAP.computeIfAbsent(position, k -> {
            BenchCache newCache = new BenchCache();
            NeighborScanner.scan(world, k, newCache, blockType);
            return newCache;
        });
        
        AutoIgnitionReduxConfig config = AutoIgnitionRedux.getInstance().getConfig();
        
        if (cache.needsRescan(config.getScanNeighborInterval())) {
            NeighborScanner.scan(world, position, cache, blockType);
        }
        
        return cache;
    }
    
    /**
     * Removes inactive caches from memory to prevent leaks.
     * A cache is considered "old" if it hasn't been scanned/accessed for more than 10 minutes.
     */
    public static void cleanOldCaches() {
        long now = System.currentTimeMillis();
        long threshold = 10 * 60 * 1000;
        
        CACHE_MAP.entrySet().removeIf(entry -> (now - entry.getValue().getLastScanTime()) > threshold);
    }
}
