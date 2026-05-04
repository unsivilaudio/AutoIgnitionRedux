package dev.unsivil.ai_redux.commands.subcommands.base;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;

import dev.unsivil.ai_redux.AutoIgnitionRedux;


public class BaseInfo extends AbstractCommand {
    
    public static final HytaleLogger logger = AutoIgnitionRedux.getInstance().getLogger();
    
    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
    
    public BaseInfo() {
        super("info", "General information about the plugin");
    }
    
    @Override
    public CompletableFuture<Void> execute(CommandContext context) {
        CommandSender sender = context.sender();
        AutoIgnitionRedux mod = AutoIgnitionRedux.getInstance();
        PluginManifest manifest = mod.getManifest();
        String sep = "=".repeat(45);
        String sep2 = "-".repeat(45);
        
        String authors = manifest.getAuthors().stream()
            .map(s -> s.getName())
            .collect(Collectors.joining(", "));
        String description = manifest.getDescription();
        String header = "  %s (%s) ".formatted(manifest.getName(), manifest.getVersion());
        
        sender.sendMessage(Message.raw(sep).color(Color.GREEN));
        sender.sendMessage(Message.raw(header).color(Color.GREEN).bold(true));
        sender.sendMessage(Message.raw(sep).color(Color.GREEN));
        sender.sendMessage(Message.join(
            Message.raw("Authors: "),
            Message.raw(authors).color(Color.YELLOW)
        ));
        if (description != null) {
            sender.sendMessage(Message.join(
                Message.raw("Description: "),
                Message.raw(description).italic(true)
            ));
        }
        sender.sendMessage(Message.empty());
        sender.sendMessage(Message.join(
            Message.raw("/autoignition help").color(Color.ORANGE),
            Message.raw(" to see more")
        ));
        sender.sendMessage(Message.raw(sep2).color(Color.GREEN));
        
        return CompletableFuture.completedFuture(null);
    }
}
