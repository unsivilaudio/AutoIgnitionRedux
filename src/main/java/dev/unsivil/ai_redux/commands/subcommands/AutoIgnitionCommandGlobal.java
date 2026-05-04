package dev.unsivil.ai_redux.commands.subcommands;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.Player;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.commands.subcommands.global.GlobalSwitch;
import dev.unsivil.ai_redux.commands.util.PermBuilder;
import dev.unsivil.ai_redux.config.AutoIgnitionReduxConfig;


public class AutoIgnitionCommandGlobal extends AbstractCommand {
    
    public static final HytaleLogger logger = AutoIgnitionRedux.getInstance().getLogger();
    
    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
    
    @Override
    protected final String generatePermissionNode() {
        return PermBuilder.Perm.GLOBAL.toString();
    }
    
    public AutoIgnitionCommandGlobal() {
        super("global", "toggle enablement/disablement of plugin");
        addSubCommand(new GlobalSwitch());
        requirePermission(generatePermissionNode());
    }
    
    @Nonnull
    @Override
    public CompletableFuture<Void> execute(CommandContext context) {
        AutoIgnitionRedux mod = AutoIgnitionRedux.getInstance();
        AutoIgnitionReduxConfig config = mod.getConfig();
        CommandSender sender = context.sender();
        
        Boolean globalEnable = config.isEnabled();
        config.setGlobalEnable(!globalEnable);
        
        if (sender instanceof Player player) {
            player.sendMessage(Message.raw("%s %s".formatted(
                mod.getManifest().getName(), config.isEnabled() ? "enabled" : "disabled")));
        } else {
            logger.atInfo().log("is now %s", config.isEnabled() ? "enabled" : "disabled");
        }
        return CompletableFuture.completedFuture(null);
    }
}
