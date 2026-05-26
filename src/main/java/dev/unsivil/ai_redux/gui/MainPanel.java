package dev.unsivil.ai_redux.gui;

import java.awt.Color;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.config.AutoIgnitionReduxConfig;
import dev.unsivil.ai_redux.gui.nav.NavManager;
import dev.unsivil.ai_redux.gui.pages.AboutPage;
import dev.unsivil.ai_redux.gui.pages.ConfigPage;
import dev.unsivil.ai_redux.gui.pages.GlobalPage;
import dev.unsivil.ai_redux.gui.pages.InfoPage;
import dev.unsivil.ai_redux.gui.util.HashBuilder;
import dev.unsivil.ai_redux.gui.util.PlayerNotifier;


public class MainPanel extends InteractiveCustomUIPage<MainPanel.MainPanelData> {
    private static final AutoIgnitionRedux mod = AutoIgnitionRedux.getInstance();
    @SuppressWarnings("unused")
    private static final HytaleLogger logger = mod.getLogger();
    private static final String NAV_ENTRY_POINT = HashBuilder.fullQualifiedPath("#Content", "#NavPane", "#NavBarItems");
    private static final String CONTENT_ENTRY_POINT = HashBuilder.fullQualifiedPath("#Content", "#ContentMain", "#ContentPane");
    
    private final AutoIgnitionReduxConfig config = AutoIgnitionReduxConfig.clone(mod.getConfig());
    private final PlayerNotifier notifier;
    private final NavManager manager;
    private UICommandBuilder uicBuilder;
    private String selectedPane = "Info";
    
    public MainPanel(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, MainPanelData.CODEC);
        manager = new NavManager(NAV_ENTRY_POINT, selectedPane);
        notifier = new PlayerNotifier(playerRef);
    }
    
    @Override
    public void build(
        @Nonnull Ref<EntityStore> ref,
        @Nonnull UICommandBuilder uiCommandBuilder,
        @Nonnull UIEventBuilder eventBuilder,
        @Nonnull Store<EntityStore> store
    ) {
        this.uicBuilder = uiCommandBuilder.append("Pages/Index.ui");
        this.manager.build(ref, this.uicBuilder, eventBuilder, store);
        manager.updateSelect(this.selectedPane, this.uicBuilder);
        setMainContent(eventBuilder);
    }
    
    private void render(String selectedPane) {
        if ((!this.selectedPane.equals(selectedPane)) && this.uicBuilder != null) {
            // logger.atInfo().log("Rebuilding for %s", selectedPane);
            this.selectedPane = selectedPane;
            this.rebuild();
        }
    }
    
    public void setMainContent(UIEventBuilder eventBuilder) {
        String contentHeaderFQHP = HashBuilder.fullQualifiedPath("#Content", "#ContentMain", "#Header", "#HeaderLabel.Text");
        switch (selectedPane) {
            case "Info" -> {
                this.uicBuilder = this.uicBuilder.set(contentHeaderFQHP, "Info");
                InfoPage infoPage = new InfoPage(CONTENT_ENTRY_POINT);
                this.uicBuilder = infoPage.build(this.uicBuilder, null);
            }
            case "Global" -> {
                this.uicBuilder = this.uicBuilder.set(contentHeaderFQHP, "Global");
                GlobalPage globalPage = new GlobalPage(CONTENT_ENTRY_POINT, config);
                this.uicBuilder = globalPage.build(this.uicBuilder, eventBuilder);
            }
            case "Config" -> {
                this.uicBuilder = this.uicBuilder.set(contentHeaderFQHP, "Config");
                ConfigPage configPage = new ConfigPage(CONTENT_ENTRY_POINT, config);
                this.uicBuilder = configPage.build(this.uicBuilder, eventBuilder);
            }
            case "About" -> {
                this.uicBuilder = this.uicBuilder.set(contentHeaderFQHP, "About");
                AboutPage aboutPage = new AboutPage(CONTENT_ENTRY_POINT);
                this.uicBuilder = aboutPage.build(this.uicBuilder, null);
            }
        }
    }
    
    public void updateLocalConfig(String key, boolean value) {
        switch (key.toUpperCase()) {
            case "AUTOSTART" -> config.setAutoStart(!value);
            case "AUTOSTOP" -> config.setAutoStop(!value);
            case "AUTOFILL" -> config.setAutoFill(!value);
            case "AUTOEMPTY" -> config.setAutoEmpty(!value);
            case "AUTOFUEL" -> config.setAutoFuel(!value);
        }
    }
    
    public void updateLocalConfig(String key, long value) {
        switch (key.toUpperCase()) {
            case "SCANFREQUENCY" -> config.setScanInterval(value);
            case "NEIGHBORSCANFREQUENCY" -> config.setScanNeighborInterval(value);
        }
    }
    
    @Override
    public void handleDataEvent(Ref<EntityStore> ref, Store<EntityStore> store, MainPanelData data) {
        super.handleDataEvent(ref, store, data);
        
        try {
            Field[] fields = data.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                if (name.equals("navbar") && field.get(data) != null) {
                    render(data.navbar);
                }
                if (name.startsWith("auto") && field.get(data) != null) {
                    updateLocalConfig(name, Boolean.parseBoolean((String) field.get(data)));
                    this.rebuild();
                }
                if (name.matches("(?i).*scanFrequency") && field.get(data) != null) {
                    updateLocalConfig(name, Long.parseLong((String) field.get(data)));
                }
                if (name.equals("configButton") && field.get(data) != null) {
                    // logger.atInfo().log(config.toJSON());
                    mod.getConfig().persist(config);
                    mod.saveConfig();
                    notifier.sendIconNotification(
                        Message.raw("AutoIgnitionRedux Update").bold(true),
                        Message.raw("Configuration successfully updated.").color(Color.GREEN)
                    );
                    this.close();
                }
                if (name.equals("globalEnable") && field.get(data) != null) {
                    boolean nextEnable = !(Boolean.parseBoolean((String) field.get(data)));
                    mod.getConfig().setGlobalEnable(nextEnable);
                    notifier.sendIconNotification(
                        Message.raw("AutoIgnitionRedux Update").bold(true),
                        Message.join(
                            Message.raw("Plugin is "),
                            Message.raw(nextEnable ? "enabled" : "disabled").color(
                                nextEnable ? Color.GREEN : Color.RED
                            )
                        )
                    );
                    this.close();
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            // Don't care
        } finally {
            this.sendUpdate();
        }
    }
    
    public static class MainPanelData {
        static final String KEY_NAVBAR = "NavBar";
        static final String KEY_CONFIG_BUTTON = "ConfigButton";
        static final String KEY_CONFIG_SCAN_FREQUENCY = "@ScanFrequencyInputField";
        static final String KEY_CONFIG_NEIGHBOR_SCAN_FR = "@NeighborScanFrequencyInputField";
        static final String KEY_CONFIG_AUTO_START = "#AutoStartBox";
        static final String KEY_CONFIG_AUTO_STOP = "#AutoStopBox";
        static final String KEY_CONFIG_AUTO_FILL = "#AutoFillBox";
        static final String KEY_CONFIG_AUTO_EMPTY = "#AutoEmptyBox";
        static final String KEY_CONFIG_AUTO_FUEL = "#AutoFuelBox";
        static final String KEY_GLOBAL_ENABLE_DISABLE = "#GlobalEnableDisable";
        
        public static final BuilderCodec<MainPanelData> CODEC = BuilderCodec.builder(MainPanelData.class, MainPanelData::new)
            .append(new KeyedCodec<>(KEY_NAVBAR, Codec.STRING), (store, m) -> store.navbar = m, store -> store.navbar).add()
            .append(new KeyedCodec<>(KEY_CONFIG_BUTTON, Codec.STRING), (store, m) -> store.configButton = m, store -> store.configButton).add()
            .append(new KeyedCodec<>(KEY_CONFIG_SCAN_FREQUENCY, Codec.STRING), (store, m) -> store.scanFrequency = m, store -> store.scanFrequency)
            .add()
            .append(new KeyedCodec<>(KEY_CONFIG_NEIGHBOR_SCAN_FR, Codec.STRING), (store, m) -> store.neighborScanFrequency = m,
                store -> store.neighborScanFrequency)
            .add()
            .append(new KeyedCodec<>(KEY_CONFIG_AUTO_START, Codec.STRING), (store, m) -> store.autoStart = m, store -> store.autoStart).add()
            .append(new KeyedCodec<>(KEY_CONFIG_AUTO_STOP, Codec.STRING), (store, m) -> store.autoStop = m, store -> store.autoStop).add()
            .append(new KeyedCodec<>(KEY_CONFIG_AUTO_FILL, Codec.STRING), (store, m) -> store.autoFill = m, store -> store.autoFill).add()
            .append(new KeyedCodec<>(KEY_CONFIG_AUTO_EMPTY, Codec.STRING), (store, m) -> store.autoEmpty = m, store -> store.autoEmpty).add()
            .append(new KeyedCodec<>(KEY_CONFIG_AUTO_FUEL, Codec.STRING), (store, m) -> store.autoFuel = m, store -> store.autoFuel).add()
            .append(new KeyedCodec<>(KEY_GLOBAL_ENABLE_DISABLE, Codec.STRING), (store, m) -> store.globalEnable = m, store -> store.globalEnable)
            .add()
            .build();
        
        private String navbar;
        private String configButton;
        private String scanFrequency;
        private String neighborScanFrequency;
        private String autoStart;
        private String autoStop;
        private String autoFill;
        private String autoEmpty;
        private String autoFuel;
        private String globalEnable;
    }
}
