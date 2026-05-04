package dev.unsivil.ai_redux.systems.inventory;

import com.hypixel.hytale.builtin.crafting.component.ProcessingBenchBlock;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.CombinedItemContainer;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.block.components.ItemContainerBlock;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.cache.BenchCache;
import dev.unsivil.ai_redux.systems.core.BenchUtils;


/**
 * Handles the physical movement of items between benches and external storage.
 * Manages both refueling logic and output extraction by iterating through
 * discovered nearby containers.
 */
public class ItemMover {
    
    @SuppressWarnings("unused")
    private static final HytaleLogger logger = AutoIgnitionRedux.getInstance().getLogger();
    
    /**
     * Attempts to refill the bench's fuel slot from cached nearby containers.
     * If no fuel is found externally, it attempts to recycle fuel from its own output.
     * @param bench The bench requiring fuel.
     * @param world The world instance.
     * @param cache The bench's cache.
     */
    public static void refillFuel(ProcessingBenchBlock bench, World world, BenchCache cache) {
        CombinedItemContainer combinedItemContainer = bench.getItemContainer();
        
        assert combinedItemContainer != null;
        ItemContainer fuelContainer = combinedItemContainer.getContainer(0);
        ItemContainer outputContainer = combinedItemContainer.getContainer(2);
        
        if (!fuelContainer.isEmpty()) return;
        
        for (Vector3i position : cache.getContainerPositions()) {
            WorldChunk chunk = BenchUtils.Chunk.getSafeChunkFromBlock(world, position);
            if (chunk != null) {
                var component = BlockModule.getComponent(ItemContainerBlock.getComponentType(),
                    world, position.x, position.y, position.z);
                if (component != null && component instanceof ItemContainerBlock containerBlock) {
                    ItemContainer chestContainer = containerBlock.getItemContainer();
                    
                    transferFuel(chestContainer, fuelContainer);
                    
                    if (!fuelContainer.isEmpty()) return;
                }
            }
        }
        
        transferFuel(outputContainer, fuelContainer);
    }
    
    /**
     * Internal logic for moving fuel items from a source to a destination.
     * Validates item quality and checks against the configuration blacklist.
     * @param source The external storage container.
     * @param destination The bench's internal fuel container.
     */
    private static void transferFuel(ItemContainer source, ItemContainer destination) {
        if (source == null || source.isEmpty() || destination == null || destination.getCapacity() == 0) return;
        
        for (short i = 0; i < source.getCapacity(); i++) {
            ItemStack itemStack = source.getItemStack(i);
            
            if (itemStack == null || itemStack.getItem().getFuelQuality() <= 0) continue;
            // if (AutoIgnitionMod.getConfig().getBlacklistedFuelItems().contains(itemStack.getItemId())) continue;
            
            if (destination.canAddItemStack(itemStack)) {
                source.moveItemStackFromSlot(i, destination);
            }
            
            if (!destination.isEmpty()) return;
        }
    }
    
    /**
     * Automatically clears the bench's output slots and transfers contents to nearby storage.
     * @param bench The bench to empty.
     * @param world The world instance.
     * @param cache The bench's cache.
     */
    public static void emptyOutput(ProcessingBenchBlock bench, World world, BenchCache cache) {
        CombinedItemContainer combinedItemContainer = bench.getItemContainer();
        
        assert combinedItemContainer != null;
        ItemContainer outputContainer = combinedItemContainer.getContainer(2);
        
        if (outputContainer.isEmpty()) return;
        
        for (Vector3i position : cache.getContainerPositions()) {
            WorldChunk chunk = BenchUtils.Chunk.getSafeChunkFromBlock(world, position);
            if (chunk != null) {
                var component = BlockModule.getComponent(ItemContainerBlock.getComponentType(),
                    world, position.x, position.y, position.z);
                if (component != null && component instanceof ItemContainerBlock containerBlock) {
                    ItemContainer chestContainer = containerBlock.getItemContainer();
                    
                    transferOutput(outputContainer, chestContainer);
                    
                    if (outputContainer.isEmpty()) break;
                }
            }
        }
    }
    
    /**
     * Internal logic for moving output items to external storage.
     * @param source The bench's internal output container.
     * @param destination The external storage container.
     */
    private static void transferOutput(ItemContainer source, ItemContainer destination) {
        for (short i = 0; i < source.getCapacity(); i++) {
            ItemStack itemStack = source.getItemStack(i);
            
            if (itemStack != null) {
                if (destination.canAddItemStack(itemStack)) {
                    source.moveItemStackFromSlot(i, destination);
                }
            }
        }
    }
    
    /**
     * Automatically fills the bench's input slots from nearby storage.
     * @param bench The bench to fill the input.
     * @param world The world instance.
     * @param cache The bench's cache.
     */
    public static void refillInput(ProcessingBenchBlock bench, World world, BenchCache cache) {
        CombinedItemContainer combinedItemContainer = bench.getItemContainer();
        
        assert combinedItemContainer != null;
        ItemContainer inputContainer = combinedItemContainer.getContainer(1);
        
        for (Vector3i position : cache.getContainerPositions()) {
            WorldChunk chunk = BenchUtils.Chunk.getSafeChunkFromBlock(world, position);
            if (chunk != null) {
                var component = BlockModule.getComponent(ItemContainerBlock.getComponentType(),
                    world, position.x, position.y, position.z);
                if (component != null && component instanceof ItemContainerBlock containerBlock) {
                    ItemContainer chestContainer = containerBlock.getItemContainer();
                    
                    transferInput(chestContainer, inputContainer);
                }
            }
        }
    }
    
    /**
     * Internal logic for moving input items from external storage.
     * @param source The external storage container.
     * @param destination The bench's internal input container.
     */
    private static void transferInput(ItemContainer source, ItemContainer destination) {
        for (short i = 0; i < source.getCapacity(); i++) {
            ItemStack itemStack = source.getItemStack(i);
            if (itemStack != null) {
                // if (AutoIgnitionMod.getConfig().getBlacklistedInputItems().contains(itemStack.getItemId())) continue;
                
                if (destination.canAddItemStack(itemStack)) {
                    source.moveItemStackFromSlot(i, destination);
                }
            }
        }
    }
}
