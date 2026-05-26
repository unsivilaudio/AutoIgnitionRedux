package dev.unsivil.ai_redux.gui.pages;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.gui.templates.UIGroup;
import dev.unsivil.ai_redux.gui.templates.UILabel;
import dev.unsivil.ai_redux.gui.templates.UIPage;
import dev.unsivil.ai_redux.gui.templates.enums.LayoutMode;


public class InfoPage extends UIPage {
    public static final AutoIgnitionRedux mod = AutoIgnitionRedux.getInstance();
    private static final Map<String, String> fields = new LinkedHashMap<>();
    
    static {
        PluginManifest manifest = mod.getManifest();
        fields.put(manifest.getName(), manifest.getVersion().toString());
        fields.put("Authors", manifest.getAuthors().stream()
            .map(s -> s.getName())
            .collect(Collectors.joining(", ")));
        fields.put("Description", manifest.getDescription());
        fields.put("Game Version", manifest.getServerVersion().toString());
        fields.put("Last Update", AutoIgnitionRedux.getLastUpdateDate());
    }
    
    public InfoPage(String entryPoint) {
        super(entryPoint);
    }
    
    @Override
    public UICommandBuilder build(@Nonnull UICommandBuilder uiCommandBuilder, @Nullable UIEventBuilder uiEventBuilder) {
        uiCommandBuilder = uiCommandBuilder.clear(entryPoint);
        int idx = 0;
        for (var entry : fields.entrySet()) {
            UILabel labelKey = (UILabel) new UILabel("LabelKey" + idx, entry.getKey())
                .setAnchors(Map.of("Right", "5"))
                .setStyle(Map.of("HorizontalAlignment", "End", "TextColor", "#FFFFFF"))
                .setFlexWeight(1);
            
            UILabel labelValue = (UILabel) new UILabel("LabelValue" + idx, entry.getValue())
                .setAnchors(Map.of("Left", "5"))
                .setStyle(Map.of("TextColor", "#FFFFFF"))
                .setFlexWeight(1);
            
            UIGroup group = new UIGroup("Info" + ++idx);
            group.setMode(LayoutMode.LEFT)
                .appendChild(labelKey.get())
                .appendChild(labelValue.get())
                .setAnchors(Map.of("Height", "25", "Horizontal", "1", "Bottom", "8"))
                .setPadding(Map.of("Vertical", "5"))
                .setFlexWeight(1);
            uiCommandBuilder = uiCommandBuilder.appendInline(entryPoint, group.get());
        }
        return uiCommandBuilder;
    }
}
