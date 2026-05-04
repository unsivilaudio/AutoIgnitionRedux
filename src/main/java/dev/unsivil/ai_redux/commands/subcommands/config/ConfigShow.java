package dev.unsivil.ai_redux.commands.subcommands.config;

import java.util.concurrent.CompletableFuture;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.config.AutoIgnitionReduxConfig;


public class ConfigShow extends AbstractCommand {
    
    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
    
    public ConfigShow() {
        super("show", "prints the current configuration");
    }
    
    @Override
    protected CompletableFuture<Void> execute(CommandContext context) {
        AutoIgnitionReduxConfig config = AutoIgnitionRedux.getInstance().getConfig();
        String JSONconfig = config.toJSON();
        CommandSender sender = context.sender();
        sender.sendMessage(Message.raw(JSONconfig));
        
        return CompletableFuture.completedFuture(null);
    }
    
}
