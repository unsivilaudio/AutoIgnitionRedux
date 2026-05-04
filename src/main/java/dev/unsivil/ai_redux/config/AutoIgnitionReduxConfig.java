package dev.unsivil.ai_redux.config;

import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;


public class AutoIgnitionReduxConfig {
    
    private static final int CONFIG_VERSION = 1;
    private static final Map<String, ?> DEFAULT_CONFIG = Map.of(
        "scanInterval", 500L,
        "scanNeighborInterval", 1000L,
        "autoStart", true,
        "autoStop", true,
        "autoFuel", true,
        "autoEmpty", true,
        "autoFill", true
    );
    private final AtomicBoolean globalEnable = new AtomicBoolean(true);
    private long scanInterval;
    private long scanNeighborInterval;
    private boolean autoStart;
    private boolean autoStop;
    private boolean autoFuel;
    private boolean autoEmpty;
    private boolean autoFill;
    
    public AutoIgnitionReduxConfig() {
        this.scanInterval = (Long) DEFAULT_CONFIG.get("scanInterval");
        this.scanNeighborInterval = (Long) DEFAULT_CONFIG.get("scanNeighborInterval");
        this.autoStart = (boolean) DEFAULT_CONFIG.get("autoStart");
        this.autoFuel = (boolean) DEFAULT_CONFIG.get("autoFuel");
        this.autoStop = (boolean) DEFAULT_CONFIG.get("autoStop");
        this.autoEmpty = (boolean) DEFAULT_CONFIG.get("autoEmpty");
        this.autoFill = (boolean) DEFAULT_CONFIG.get("autoFill");
    }
    
    public AutoIgnitionReduxConfig(AutoIgnitionReduxConfig config) {
        this.globalEnable.set(config.globalEnable.get());
        this.scanInterval = config.scanInterval;
        this.scanNeighborInterval = config.scanNeighborInterval;
        this.autoStart = config.autoStart;
        this.autoFuel = config.autoFuel;
        this.autoStop = config.autoStop;
        this.autoEmpty = config.autoEmpty;
        this.autoFill = config.autoFill;
    }
    
    public static final AutoIgnitionReduxConfig clone(AutoIgnitionReduxConfig config) {
        return new AutoIgnitionReduxConfig(config);
    }
    
    public AutoIgnitionReduxConfig persist(AutoIgnitionReduxConfig config) {
        this.scanInterval = config.scanInterval;
        this.scanNeighborInterval = config.scanNeighborInterval;
        this.autoStart = config.autoStart;
        this.autoFuel = config.autoFuel;
        this.autoStop = config.autoStop;
        this.autoEmpty = config.autoEmpty;
        this.autoFill = config.autoFill;
        return clone(this);
    }
    
    public boolean isEnabled() { return globalEnable.get(); }
    
    public void setGlobalEnable(boolean enabled) {
        globalEnable.set(enabled);
    }
    
    public long getScanInterval() { return scanInterval; }
    
    public void setScanInterval(long scanInterval) { this.scanInterval = Math.max((long) DEFAULT_CONFIG.get("scanInterval"), scanInterval); }
    
    public long getScanNeighborInterval() { return scanNeighborInterval; }
    
    public void setScanNeighborInterval(long scanNeighborInterval) {
        this.scanNeighborInterval = Math.max((long) DEFAULT_CONFIG.get("scanNeighborInterval"), scanNeighborInterval);
    }
    
    public boolean isAutoStart() { return autoStart; }
    
    public void setAutoStart(boolean autoStart) { this.autoStart = autoStart; }
    
    public boolean isAutoFuel() { return autoFuel; }
    
    public void setAutoFuel(boolean autoFuel) { this.autoFuel = autoFuel; }
    
    public boolean isAutoStop() { return autoStop; }
    
    public void setAutoStop(boolean autoStop) { this.autoStop = autoStop; }
    
    public boolean isAutoEmpty() { return autoEmpty; }
    
    public void setAutoEmpty(boolean autoEmpty) { this.autoEmpty = autoEmpty; }
    
    public boolean isAutoFill() { return autoFill; }
    
    public void setAutoFill(boolean autoFill) { this.autoFill = autoFill; }
    
    private String keyJSON(String key, boolean value) {
        return "\"%s\": %s".formatted(key, value);
    }
    
    private String keyJSON(String key, long value) {
        return "\"%s\": %d".formatted(key, value);
    }
    
    public String toJSON() {
        var output = new StringJoiner(",", "{", "}");
        output.add(keyJSON("GlobalEnable", globalEnable.get()))
            .add(keyJSON("ScanInterval", scanInterval))
            .add(keyJSON("ScanNeighborInterval", scanNeighborInterval))
            .add(keyJSON("AutoStart", autoStart))
            .add(keyJSON("AutoStop", autoStop))
            .add(keyJSON("AutoFuel", autoFuel))
            .add(keyJSON("AutoEmpty", autoEmpty))
            .add(keyJSON("AutoFill", autoFill));
        return output.toString();
    }
    
    public static final BuilderCodec<AutoIgnitionReduxConfig> CODEC =
        BuilderCodec.builder(AutoIgnitionReduxConfig.class, AutoIgnitionReduxConfig::new)
            .append(new KeyedCodec<>("ConfigVersion", Codec.INTEGER), (_, _) -> {}, (_) -> AutoIgnitionReduxConfig.CONFIG_VERSION).add()
            .append(new KeyedCodec<>("ScanInterval", Codec.LONG), (c, v) -> c.setScanInterval(v), (c) -> c.scanInterval).add()
            .append(new KeyedCodec<>("ScanNeighborInterval", Codec.LONG), (c, v) -> c.setScanNeighborInterval(v), (c) -> c.scanNeighborInterval).add()
            .append(new KeyedCodec<>("AutoStart", Codec.BOOLEAN), (c, v) -> c.autoStart = v, (c) -> c.autoStart).add()
            .append(new KeyedCodec<>("AutoStop", Codec.BOOLEAN), (c, v) -> c.autoStop = v, (c) -> c.autoStop).add()
            .append(new KeyedCodec<>("AutoFuel", Codec.BOOLEAN), (c, v) -> c.autoFuel = v, (c) -> c.autoFuel).add()
            .append(new KeyedCodec<>("AutoEmpty", Codec.BOOLEAN), (c, v) -> c.autoEmpty = v, (c) -> c.autoEmpty).add()
            .append(new KeyedCodec<>("AutoFill", Codec.BOOLEAN), (c, v) -> c.autoFill = v, (c) -> c.autoFill).add()
            .build();
}
