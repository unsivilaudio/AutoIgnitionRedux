package dev.unsivil.ai_redux.gui.nav;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.ui.PatchStyle;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;


public class NavManager {
    private static final Map<String, String> navOptions = new LinkedHashMap<>() {
        {
            put("Info", "autoignitionredux.commands");
            put("Global", "autoignitionredux.commands.global");
            put("Config", "autoignitionredux.commands.config");
            put("About", "autoignitionredux.commands");
        }
    };
    
    private final String entryPoint;
    private String selectedPane;
    
    public NavManager(@Nonnull String entryPoint, @Nonnull String selectedPane) {
        this.entryPoint = entryPoint;
        this.selectedPane = selectedPane;
    }
    
    public UICommandBuilder build(
        @Nonnull Ref<EntityStore> ref,
        @Nonnull UICommandBuilder uiCommandBuilder,
        @Nonnull UIEventBuilder uiEventBuilder,
        @Nonnull Store<EntityStore> store
    ) {
        Player player = store.getComponent(ref, Player.getComponentType());
        uiCommandBuilder = uiCommandBuilder.clear(entryPoint);
        UICommandBuilder uic = uiCommandBuilder;
        navOptions.entrySet().stream().forEach(entry -> {
            if (player.hasPermission(entry.getValue())) {
                String iconPath = switch (entry.getKey()) {
                    case "Info" -> "Assets/icons/flaticon_info.png";
                    case "Global" -> "Assets/icons/flaticon_world.png";
                    case "Config" -> "Assets/icons/flaticon_setting.png";
                    case "About" -> "Assets/icons/flaticon_google-docs.png";
                    default -> throw new RuntimeException("Missing %s icon asset for NavBar".formatted(entry.getKey()));
                };
                NavBarItem navButton = new NavBarItem("Nav" + entry.getKey(), entry.getKey(), iconPath)
                    .setIconSize(25);
                if (entry.getKey().equals(selectedPane)) {
                    navButton.setSelected(true);
                }
                uic.appendInline(entryPoint, navButton.get());
                uiEventBuilder.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    entryPoint.concat(" " + navButton.getID()),
                    EventData.of("NavBar", entry.getKey())
                );
            }
        });
        uiCommandBuilder = uic;
        return uiCommandBuilder;
    }
    
    /* This is probably redundant logic now -- EDIT it somehow isn't */
    private UICommandBuilder manageSelected(@Nonnull UICommandBuilder uiCommandBuilder, @Nonnull String previousSelect, @Nonnull String nextSelect) {
        String prevButtonEntry = entryPoint.concat(" #Nav" + previousSelect + "Button");
        String prevButtonIconEntry = entryPoint.concat(" #Nav%1$sGroup #Nav%1$sIcon".formatted(previousSelect));
        String prevButtonLabelEntry = entryPoint.concat(" #Nav%1$sGroup #Nav%1$sLabel".formatted(previousSelect));
        uiCommandBuilder = uiCommandBuilder
            .setNull(prevButtonEntry.concat(".Background"))
            .setObject(prevButtonIconEntry.concat(".Background"),
                new PatchStyle().setColor(Value.of("#6E7DA1")))
            .set(prevButtonLabelEntry.concat(".Style"),
                Value.ref("Components/nav/items/NavItems.ui", "StandardStyle")
            );
        String nextButtonEntry = entryPoint.concat(" #Nav" + nextSelect + "Button");
        String nextButtonIconEntry = entryPoint.concat(" #Nav%1$sGroup #Nav%1$sIcon".formatted(nextSelect));
        String nextButtonLabelEntry = entryPoint.concat(" #Nav%1$sGroup #Nav%1$sLabel".formatted(nextSelect));
        uiCommandBuilder = uiCommandBuilder
            .set(nextButtonEntry.concat(".Background"),
                Value.ref("Components/nav/NavBar.ui", "SelectedBackground"))
            .setObject(nextButtonIconEntry.concat(".Background"),
                new PatchStyle().setColor(Value.of("#BAC9EC")))
            .set(nextButtonLabelEntry.concat(".Style"),
                Value.ref("Components/nav/items/NavItems.ui", "SelectedStyle")
            );
        return uiCommandBuilder;
    }
    
    public UICommandBuilder updateSelect(@Nonnull String selectedPane, @Nonnull UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder = manageSelected(uiCommandBuilder, this.selectedPane, selectedPane);
        this.selectedPane = selectedPane;
        return uiCommandBuilder;
    }
}
