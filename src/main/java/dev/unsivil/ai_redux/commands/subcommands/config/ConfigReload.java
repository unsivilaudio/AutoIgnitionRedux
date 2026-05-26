package dev.unsivil.ai_redux.commands.subcommands.config;

import java.util.concurrent.CompletableFuture;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.commands.util.PermBuilder;


public class ConfigReload extends AbstractCommand {
    
    public static final HytaleLogger logger = AutoIgnitionRedux.getInstance().getLogger();
    
    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
    
    @Override
    protected final String generatePermissionNode() {
        return PermBuilder.Perm.CONFIG.extend("reload");
    }
    
    public ConfigReload() {
        super("reload", "reload the configuration from file");
        requirePermission(generatePermissionNode());
    }
    
    @Override
    public CompletableFuture<Void> execute(CommandContext context) {
        AutoIgnitionRedux mod = AutoIgnitionRedux.getInstance();
        CommandSender sender = context.sender();
        
        var futureLoaded = mod.reloadConfig();
        
        futureLoaded.whenComplete((result, ex) -> {
            if (ex != null) {
                if (context.isPlayer()) {
                    sender.sendMessage(Message.raw("Something went wrong loading the config."));
                    return;
                }
                logger.atSevere().log("Something went wrong loading the config. " + ex.getMessage());
                return;
            }
            
            if (context.isPlayer()) {
                sender.sendMessage(Message.raw("AutoIgnitionRedux configuration reloaded."));
            } else {
                logger.atInfo().log("Configuration reloaded.");
            }
        });
        
        return futureLoaded.thenRun(() -> {});
    }
}
