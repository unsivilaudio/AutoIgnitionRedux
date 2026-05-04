package dev.unsivil.ai_redux.commands.subcommands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

import dev.unsivil.ai_redux.commands.subcommands.config.ConfigReload;
import dev.unsivil.ai_redux.commands.subcommands.config.ConfigShow;
import dev.unsivil.ai_redux.commands.util.PermBuilder;


public class AutoIgnitionCommandConfig extends AbstractCommandCollection {
    
    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
    
    @Override
    protected final String generatePermissionNode() {
        return PermBuilder.Perm.CONFIG.toString();
    }
    
    public AutoIgnitionCommandConfig() {
        super("config", "configuration commands");
        addSubCommand(new ConfigShow());
        addSubCommand(new ConfigReload());
        requirePermission(generatePermissionNode());
    }
    
}
