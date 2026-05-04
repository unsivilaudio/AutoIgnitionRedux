package dev.unsivil.ai_redux.commands.subcommands.global;

import java.util.concurrent.CompletableFuture;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.entity.entities.Player;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.config.AutoIgnitionReduxConfig;


public class GlobalSwitch extends AbstractCommand {
    
    public static final HytaleLogger logger = AutoIgnitionRedux.getInstance().getLogger();
    
    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
    
    private final RequiredArg<String> enableDisable;
    
    public GlobalSwitch() {
        super("set", "Declaratively switch the plugin enabled/disabled");
        enableDisable = withRequiredArg("[enable|disable]", "the mode to change global operations", ArgTypes.STRING);
    }
    
    @Override
    public CompletableFuture<Void> execute(CommandContext context) {
        CommandSender sender = context.sender();
        AutoIgnitionRedux mod = AutoIgnitionRedux.getInstance();
        AutoIgnitionReduxConfig config = mod.getConfig();
        String enableString = enableDisable.get(context);
        switch (enableString) {
            case "enable", "enabled" -> config.setGlobalEnable(true);
            case "disable", "disabled" -> config.setGlobalEnable(false);
            default -> {
                // invalid value passed, do nothing
            }
        }
        
        if (sender instanceof Player player) {
            player.sendMessage(Message.raw("%s %s".formatted(
                mod.getManifest().getName(), config.isEnabled() ? "enabled" : "disabled")));
        } else {
            logger.atInfo().log("is now %s", config.isEnabled() ? "enabled" : "disabled");
        }
        return CompletableFuture.completedFuture(null);
    }
}
