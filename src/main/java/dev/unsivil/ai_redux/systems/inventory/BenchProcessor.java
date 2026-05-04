package dev.unsivil.ai_redux.systems.inventory;

// import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
// import com.hypixel.hytale.math.vector.Vector3i;
// import com.hypixel.hytale.server.core.inventory.container.CombinedItemContainer;
// import com.hypixel.hytale.server.core.universe.world.World;
// import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
// import net.autoignition.AutoIgnitionMod;
// import net.autoignition.cache.BenchCache;
// import net.autoignition.cache.BenchCacheManager;
// import net.autoignition.config.AutoIgnitionConfig;

/**
 * Core logic controller for processing benches. Orchestrates timing, inventory logistics, and
 * automatic ignition states for individual blocks in the world.
 */
public class BenchProcessor {
    
    /**
     * Primary entry point for bench logic. Validates timing thresholds before executing resource
     * management and ignition state updates.
     * 
     * @param bench The state of the bench being processed.
     */
    // @SuppressWarnings("removal")
    // public static void handle(ProcessingBenchState bench) {
    // WorldChunk chunk = bench.getChunk();
    // if (chunk == null) return;
    
    // Vector3i position = bench.getBlockPosition();
    //     World world = chunk.getWorld();
    
    //     BenchCache cache = BenchCacheManager.getOrCreate(position, world, bench);
    
    //     AutoIgnitionConfig config = AutoIgnitionMod.getConfig();
    
    //     if (!cache.needsRerun(config.getUpdateIntervalMs())) return;
    //     cache.updateRunTime();
    
    //     handleLogistics(bench, world, cache);
    
    //     boolean hasNoRecipe = (bench.getRecipe() == null);
    //     if (hasNoRecipe) {
    //         if (config.isEnableAutoFuelStop()) bench.setActive(false);
    //     } else if (config.isEnableAutoFuelStart()) {
    //         attemptIgnition(bench);
    //     }
    // }
    
    /**
     * Handles the movement of items between the bench and nearby containers.
     * @param bench The current bench state.
     * @param world The world instance.
     * @param cache The cached container positions for this bench.
     */
    // private static void handleLogistics(ProcessingBenchState bench, World world, BenchCache cache) {
    //     AutoIgnitionConfig config = AutoIgnitionMod.getConfig();
    
    //     if (config.isEnableAutoRefuel()) ItemMover.refillFuel(bench, world, cache);
    //     if (config.isEnableInputTransfer()) ItemMover.refillInput(bench, world, cache);
    //     if (config.isEnableOutputTransfer()) ItemMover.emptyOutput(bench, world, cache);
    // }
    
    /**
     * Analyzes the bench's internal containers to decide if it should be activated.
     * @param bench The bench state to potentially ignite.
     */
    // private static void attemptIgnition(ProcessingBenchState bench) {
    //     if (bench.isActive() || bench.getRecipe() == null) return;
    
    //     CombinedItemContainer container = bench.getItemContainer();
    //     if (!container.getContainer(0).isEmpty() && !container.getContainer(1).isEmpty()) {
    //         bench.setActive(true);
    //     }
    // }
}
