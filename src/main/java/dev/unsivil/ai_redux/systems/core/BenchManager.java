package dev.unsivil.ai_redux.systems.core;

import com.hypixel.hytale.builtin.crafting.component.BenchBlock;
import com.hypixel.hytale.builtin.crafting.component.ProcessingBenchBlock;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.block.BlockModule.BlockStateInfo;
import com.hypixel.hytale.server.core.universe.world.World;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.cache.BenchCache;
import dev.unsivil.ai_redux.config.AutoIgnitionReduxConfig;
import dev.unsivil.ai_redux.systems.inventory.ItemMover;


/**
 *  The head manager for handling all
 *  processing blocks. Instanced for each
 *  ProcessingBenchBlock component.
 */
public class BenchManager {
    
    @SuppressWarnings("unused")
    private static final HytaleLogger loggger = AutoIgnitionRedux.getInstance().getLogger();
    
    private final AutoIgnitionReduxConfig config;
    private final ProcessingBenchBlock processingBenchBlock;
    private final BenchBlock benchBlock;
    private final BlockModule.BlockStateInfo blockStateInfo;
    private final BenchCache cache;
    private final World world;
    
    /** Private BenchManager Constructor */
    private BenchManager(
        ProcessingBenchBlock processingBenchBlock, BenchBlock benchBlock, BlockStateInfo blockStateInfo,
        BenchCache cache, World world
    ) {
        config = AutoIgnitionRedux.getInstance().getConfig();
        this.processingBenchBlock = processingBenchBlock;
        this.benchBlock = benchBlock;
        this.blockStateInfo = blockStateInfo;
        this.cache = cache;
        this.world = world;
    }
    
    /**
     * ### Composition constructor
     * @param processingBenchBlock - bench data block
     * @param benchBlock - required for setting active state
     * @param blockStateInfo - required for setting active state
     * @param cache - {@link BenchCache} cache instance for target
     * @param world - required for {@link ItemMover} helper class
     */
    public static void process(
        ProcessingBenchBlock processingBenchBlock, BenchBlock benchBlock,
        BlockStateInfo blockStateInfo, BenchCache cache, World world
    ) {
        BenchManager bm = new BenchManager(processingBenchBlock, benchBlock, blockStateInfo, cache, world);
        if (!bm.config.isEnabled()) return; // CHECK GLOBAL DISABLE
        if (!bm.init()) return;
        if (!bm.checkFuelReqs()) return;
        boolean isActive = bm.processingBenchBlock.isActive();
        boolean hasNoRecipe = bm.processingBenchBlock.getRecipe() == null;
        if (hasNoRecipe && isActive) {
            if (bm.config.isAutoStop())
                bm.processingBenchBlock.setActive(false, benchBlock, blockStateInfo);
        }
    }
    
    /**
     * Mostly nonsensical initialization block
     * 
     * _still not sure what I want to do here_
     * @return {@link boolean} -- if should continue
     */
    private boolean init() {
        if (!cache.needsRerun(config.getScanInterval())) return false;
        cache.updateScanTime();
        return true;
    }
    
    /**
     * Check the fuel supply to bench and attempt
     * refueling if necessary. Source fuel from
     * nearby containers and bench output
     * @return {@link boolean} -- if should continue
     */
    private boolean checkFuelReqs() {
        if (config.isAutoFill())
            ItemMover.refillInput(processingBenchBlock, world, cache);
        if (config.isAutoFuel())
            ItemMover.refillFuel(processingBenchBlock, world, cache);
        if (config.isAutoEmpty())
            ItemMover.emptyOutput(processingBenchBlock, world, cache);
        
        return attemptStart();
    }
    
    /**
     * Attempt starting of bench instance
     * @return {@link boolean} -- if should continue
     */
    private boolean attemptStart() {
        if (!processingBenchBlock.isActive() && processingBenchBlock.getRecipe() != null) {
            return processingBenchBlock.setActive(true, benchBlock, blockStateInfo);
        }
        return true;
    }
}
