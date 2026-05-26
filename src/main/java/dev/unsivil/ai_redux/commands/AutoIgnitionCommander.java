package dev.unsivil.ai_redux.commands;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import dev.unsivil.ai_redux.commands.subcommands.AutoIgnitionCommandConfig;
import dev.unsivil.ai_redux.commands.subcommands.AutoIgnitionCommandGlobal;
import dev.unsivil.ai_redux.commands.subcommands.base.BaseHelp;
import dev.unsivil.ai_redux.commands.subcommands.base.BaseInfo;
import dev.unsivil.ai_redux.commands.util.PermBuilder;
import dev.unsivil.ai_redux.gui.MainPanel;


public class AutoIgnitionCommander extends AbstractCommand {
    
    @Override
    public boolean canGeneratePermission() {
        return false;
    }
    
    @Override
    protected final String generatePermissionNode() {
        return PermBuilder.Perm.COMMANDS.toString();
    }
    
    public AutoIgnitionCommander(String name, String description) {
        super(name, description);
        addAliases(new String[] { "ai", "air" });
        addSubCommand(new BaseInfo());
        addSubCommand(new BaseHelp());
        addSubCommand(new AutoIgnitionCommandGlobal());
        addSubCommand(new AutoIgnitionCommandConfig());
        requirePermission(generatePermissionNode());
    }
    
    @Override
    public CompletableFuture<Void> execute(
        @Nonnull CommandContext context
    ) {
        if (context.isPlayer()) {
            Ref<EntityStore> ref = context.senderAsPlayerRef();
            if (ref != null && ref.isValid()) {
                Store<EntityStore> store = ref.getStore();
                World world = store.getExternalData().getWorld();
                return CompletableFuture.runAsync(() -> {
                    PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
                    Player player = store.getComponent(ref, Player.getComponentType());
                    if (playerRef != null) {
                        MainPanel panel = new MainPanel(playerRef);
                        player.getPageManager().openCustomPage(ref, store, panel);
                    }
                }, world);
            }
        }
        
        return CompletableFuture.completedFuture(null);
    }
}
