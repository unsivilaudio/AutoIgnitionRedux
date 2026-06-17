package dev.unsivil.ai_redux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;

import dev.unsivil.ai_redux.commands.AutoIgnitionCommander;
import dev.unsivil.ai_redux.config.AutoIgnitionReduxConfig;
import dev.unsivil.ai_redux.systems.IgnitionSystem;


public class AutoIgnitionRedux extends JavaPlugin {
    public static final LocalDate UPDATE_DATE = LocalDate.of(2026, 6, 17);
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static AutoIgnitionRedux INSTANCE;
    
    public static final String getLastUpdateDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return UPDATE_DATE.format(dtf);
    }
    
    public static final AutoIgnitionRedux getInstance() { return INSTANCE; }
    
    private final Config<AutoIgnitionReduxConfig> config = this.withConfig(
        "AutoIgnitionRedux", AutoIgnitionReduxConfig.CODEC);
    private final HytaleLogger logger;
    
    public AutoIgnitionRedux(@Nonnull JavaPluginInit init) {
        super(init);
        INSTANCE = this;
        logger = LOGGER;
    }
    
    @Override
    protected void setup() {
        super.setup();
        saveConfig();
        logger.atInfo().log("Starting plugin initialization...");
        
        registerCommands();
        registerSystems();
    }
    
    private void registerCommands() {
        this.getCommandRegistry().registerCommand(new AutoIgnitionCommander("autoignition", "A plugin for furnace automation"));
    }
    
    private void registerSystems() {
        this.getChunkStoreRegistry().registerSystem(new IgnitionSystem(IgnitionSystem.getComponentType));
    }
    
    public AutoIgnitionReduxConfig getConfig() { return config.get(); }
    
    public CompletableFuture<AutoIgnitionReduxConfig> reloadConfig() {
        return config.load();
    }
    
    public CompletableFuture<AutoIgnitionReduxConfig> saveConfig() {
        return config.save().thenApply(res -> getConfig());
    }
}
