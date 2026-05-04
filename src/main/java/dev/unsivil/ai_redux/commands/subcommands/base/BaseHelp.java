package dev.unsivil.ai_redux.commands.subcommands.base;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;


public class BaseHelp extends AbstractCommand {
    
    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
    
    public BaseHelp() {
        super("help", "show the help menu");
    }
    
    public static final Map<String, Map<String, String>> menuOptions = new LinkedHashMap<>();
    
    static {
        // @formatter:off
        menuOptions.put("autoignitionredux.commands", new LinkedHashMap<>(){{
            put("info", "Show information about plugin");
            put("help", "Prints this dialog");
        }});
        menuOptions.put("autoignitionredux.commands.global", new LinkedHashMap<>(){{
            put("global", "Toggle enable/disable of plugin");
            put("global set [enable|disable]", "Disable or enable plugin");
        }});
        menuOptions.put("autoignitionredux.commands.config", new LinkedHashMap<>(){{
            put("config show", "Prints the currently loaded configuration");
        }});
        menuOptions.put("autoignition.commands.config.reload", new LinkedHashMap<>(){{
            put("config reload", "Reload the configuration from file");
        }});
        // @formatter:on
    }
    
    @Override
    protected CompletableFuture<Void> execute(CommandContext context) {
        CommandSender sender = context.sender();
        String header = "  AutoIgnitionRedux (alias: /ai /air)";
        String sep = "=".repeat(45);
        
        sender.sendMessage(Message.raw(sep).color(Color.GREEN));
        sender.sendMessage(Message.raw(header).color(Color.GREEN).bold(true));
        sender.sendMessage(Message.raw(sep).color(Color.GREEN));
        menuBuilder(context).entrySet().stream()
            .map(e -> Message.join(
                e.getKey(),
                Message.raw(" - "),
                Message.raw(e.getValue()))
            )
            .forEach(sender::sendMessage);
        sender.sendMessage(Message.raw(sep).color(Color.GREEN));
        
        return CompletableFuture.completedFuture(null);
    }
    
    private Map<Message, String> menuBuilder(CommandContext context) {
        Map<Message, String> msgs = new LinkedHashMap<>();
        CommandSender sender = context.sender();
        
        menuOptions.keySet().stream()
            .forEach(perm -> {
                if ((context.isPlayer() && sender.hasPermission(perm)) || !context.isPlayer()) {
                    var cmds = menuOptions.get(perm);
                    for (String cmdArg : cmds.keySet()) {
                        Message key = Message.raw(prependPrefix(cmdArg)).color(Color.ORANGE);
                        String val = cmds.get(cmdArg);
                        msgs.put(key, val);
                    }
                }
            });
        
        return msgs;
    }
    
    private static String prependPrefix(String cmd) {
        return "/autoignition " + cmd;
    }
}
