package dev.unsivil.ai_redux.systems;

import javax.annotation.Nonnull;

import com.hypixel.hytale.builtin.crafting.CraftingPlugin;
import com.hypixel.hytale.builtin.crafting.component.BenchBlock;
import com.hypixel.hytale.builtin.crafting.component.ProcessingBenchBlock;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.bench.ProcessingBench;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.block.BlockModule.BlockStateInfo;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.section.BlockSection;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.cache.BenchCacheManager;
import dev.unsivil.ai_redux.systems.core.BenchManager;


public class IgnitionSystem extends EntityTickingSystem<ChunkStore> {
    
    public static final HytaleLogger logger = AutoIgnitionRedux.getInstance().getLogger();
    public static final ComponentType<ChunkStore, ProcessingBenchBlock> getComponentType = ProcessingBenchBlock.getComponentType();
    private static final ComponentType<ChunkStore, BlockModule.BlockStateInfo> blockStateInfoComponentType = BlockStateInfo.getComponentType();
    
    private final ComponentType<ChunkStore, BenchBlock> benchBlockComponentType;
    private final ComponentType<ChunkStore, ProcessingBenchBlock> benchComponentType;
    
    public IgnitionSystem(ComponentType<ChunkStore, ProcessingBenchBlock> benchComponentType) {
        this.benchComponentType = benchComponentType;
        this.benchBlockComponentType = CraftingPlugin.get()
            .getBenchBlockComponentType();
    }
    
    @Override
    public void tick(
        float dt, int eid,
        @Nonnull ArchetypeChunk<ChunkStore> archeType,
        @Nonnull Store<ChunkStore> store,
        CommandBuffer<ChunkStore> command
    ) {
        ProcessingBenchBlock pBenchBlock = archeType.getComponent(eid, benchComponentType);
        BenchBlock benchBlock = archeType.getComponent(eid, benchBlockComponentType);
        BlockModule.BlockStateInfo bStateInfo = archeType.getComponent(eid, blockStateInfoComponentType);
        
        if (pBenchBlock != null && benchBlock != null && bStateInfo != null) {
            Ref<ChunkStore> chunkRef = bStateInfo.getChunkRef();
            if (chunkRef.isValid()) {
                BlockChunk blockChunk = store.getComponent(chunkRef, BlockChunk.getComponentType());
                if (blockChunk != null) {
                    int blockIndex = bStateInfo.getIndex();
                    int localX = ChunkUtil.xFromBlockInColumn(blockIndex);
                    int localY = ChunkUtil.yFromBlockInColumn(blockIndex);
                    int localZ = ChunkUtil.zFromBlockInColumn(blockIndex);
                    int blockX = ChunkUtil.worldCoordFromLocalCoord(blockChunk.getX(), localX);
                    int blockZ = ChunkUtil.worldCoordFromLocalCoord(blockChunk.getZ(), localZ);
                    
                    @SuppressWarnings("deprecation")
                    BlockSection blockSection = blockChunk.getSectionAtBlockY(localY);
                    int blockId = blockSection.get(localX, localY, localZ);
                    BlockType blockType = BlockType.getAssetMap().getAsset(blockId);
                    if (blockSection.loaded && blockType != null) {
                        var world = store.getExternalData().getWorld();
                        var cache = BenchCacheManager.getOrCreate(
                            new Vector3i(blockX, localY, blockZ), world, blockType
                        );
                        ProcessingBench processingBench = pBenchBlock.getProcessingBench();
                        if (processingBench != null) {
                            BenchManager.process(
                                pBenchBlock,
                                benchBlock,
                                bStateInfo,
                                cache,
                                world
                            );
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public Query<ChunkStore> getQuery() { return Query.and(this.benchComponentType); }
}
