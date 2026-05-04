package dev.unsivil.ai_redux.gui.pages;

import java.util.Map;

import javax.annotation.Nonnull;

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;

import dev.unsivil.ai_redux.config.AutoIgnitionReduxConfig;
import dev.unsivil.ai_redux.gui.templates.UIGroup;
import dev.unsivil.ai_redux.gui.templates.UILabel;
import dev.unsivil.ai_redux.gui.templates.UILabelButton;
import dev.unsivil.ai_redux.gui.templates.UIPage;
import dev.unsivil.ai_redux.gui.templates.enums.LayoutMode;
import dev.unsivil.ai_redux.gui.templates.enums.UXDesignColor;
import dev.unsivil.ai_redux.gui.util.HashBuilder;


public class GlobalPage extends UIPage {
    private final AutoIgnitionReduxConfig config;
    
    public GlobalPage(String entryPoint, AutoIgnitionReduxConfig config) {
        super(entryPoint);
        this.config = config;
    }
    
    @Override
    public UICommandBuilder build(@Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder) {
        UIGroup configSettings = (UIGroup) new UIGroup("ConfigSettings")
            .setMode(LayoutMode.TOP)
            .setAnchors(Map.of("Bottom", "15"))
            .setPadding(Map.of("Vertical", "10"));
        
        UIGroup configSettingsHeader = (UIGroup) new UIGroup("ConfigSettingsHeader")
            .setMode(LayoutMode.TOP)
            .appendChild(new UILabel("HeaderText", "Configuration")
                .setStyle(Map.of(
                    "FontSize", "18",
                    "RenderBold", "true",
                    "TextColor", "#FFFFFF",
                    "HorizontalAlignment", "Center")
                ).get())
            .setAnchors(Map.of("Bottom", "15"));
        
        Map<String, String> scanLabelStyle = Map.of(
            "HorizontalAlignment", "Center",
            "VerticalAlignment", "Center",
            "TextColor", "#FFFFFF");
        UILabel scanFrequencyLabel = (UILabel) new UILabel("ScanFrequencyLabel",
            "Scan Frequency: %d(ms)".formatted(config.getScanInterval()))
                .setPadding(Map.of("Full", "8"))
                .setStyle(scanLabelStyle);
        UILabel neighborScanFrequencyLabel = (UILabel) new UILabel("NeighborScanFrequencyLabel",
            "Container Scan Frequency: %d(ms)".formatted(config.getScanNeighborInterval()))
                .setPadding(Map.of("Full", "8"))
                .setStyle(scanLabelStyle);
        UIGroup configSettingsGroup = (UIGroup) new UIGroup("ConfigSettingsGroup")
            .appendChild(scanFrequencyLabel.get()).appendChild(neighborScanFrequencyLabel.get())
            .setMode(LayoutMode.CENTER_MIDDLE)
            .setAnchors(Map.of("Bottom", "15", "Horizontal", "1"));
        
        UIGroup configBooleanGroup = (UIGroup) new UIGroup("ConfigBooleanGroup")
            .appendChild(getAutoBooleanLabel("AutoStart", config.isAutoStart()).get())
            .appendChild(getAutoBooleanLabel("AutoStop", config.isAutoStop()).get())
            .appendChild(getAutoBooleanLabel("AutoFill", config.isAutoFill()).get())
            .setMode(LayoutMode.CENTER_MIDDLE)
            .setAnchors(Map.of("Bottom", "15", "Horizontal", "1"));
        
        UIGroup configBooleanGroup2 = (UIGroup) new UIGroup("ConfigBooleanGroup2")
            .appendChild(getAutoBooleanLabel("AutoEmpty", config.isAutoEmpty()).get())
            .appendChild(getAutoBooleanLabel("AutoFuel", config.isAutoFuel()).get())
            .setMode(LayoutMode.CENTER_MIDDLE)
            .setAnchors(Map.of("Bottom", "15", "Horizontal", "1"));
        
        configSettings
            .appendChild(configSettingsHeader.get())
            .appendChild(configSettingsGroup.get())
            .appendChild(configBooleanGroup.get())
            .appendChild(configBooleanGroup2.get());
        
        UILabel globalStatusLabel = (UILabel) new UILabel("GlobalStatusLabel", "This plugin is currently:")
            .setAnchors(Map.of("Left", "40"))
            .setFlexWeight(1)
            .setStyle(Map.of("TextColor", "#FFFFFF", "HorizontalAlignment", "End"));
        UILabel globalStatusValue = (UILabel) new UILabel("GlobalStatusValue", config.isEnabled() ? "enabled" : "disabled")
            .setFlexWeight(1)
            .setAnchors(Map.of("Left", "10"))
            .setStyle(Map.of(
                "TextColor", config.isEnabled() ? UXDesignColor.ENABLED.getColor() : UXDesignColor.DISABLED.getColor(),
                "RenderBold", "true", "RenderUppercase", "true"));
        UIGroup globalStatusRow = (UIGroup) new UIGroup("GlobalStatusRow")
            .appendChild(globalStatusLabel.get())
            .appendChild(globalStatusValue.get())
            .setMode(LayoutMode.CENTER_MIDDLE)
            .setAnchors(Map.of("Horizontal", "1"));
        
        UIGroup globalStatusRow2 = (UIGroup) new UIGroup("GlobalStatusRow2")
            .appendChild(
                new UILabel("GlobalStatusHelperText", "Click the button below this text to change the global activation status.")
                    .setAnchors(Map.of("Right", "8"))
                    .setStyle(Map.of(
                        "TextColor", "#C0C0C0",
                        "FontSize", "10",
                        "RenderBold", "true",
                        "RenderUppercase", "true"))
                    .setFlexWeight(1)
                    .get()
            )
            .setMode(LayoutMode.CENTER_MIDDLE)
            .setAnchors(Map.of("Horizontal", "1"));
        
        UIGroup globalStatusGroup = (UIGroup) new UIGroup("GlobalStatusGroup")
            .appendChild(globalStatusRow.get())
            .appendChild(globalStatusRow2.get())
            .setMode(LayoutMode.MIDDLE_CENTER)
            .setAnchors(Map.of("Bottom", "15", "Full", "1"))
            .setPadding(Map.of("Vertical", "8"))
            .setFlexWeight(1);
        
        UILabelButton globalEnableDisable = (UILabelButton) new UILabelButton("GlobalEnableDisable", config.isEnabled() ? "Disable" : "Enable")
            .setMode(LayoutMode.CENTER)
            .setBackground(Map.of("Color", config.isEnabled() ? UXDesignColor.CTA_DANGER.getColor() : UXDesignColor.CTA_PRIMARY.getColor()))
            .setAnchors(Map.of("Width", "125", "Height", "30"))
            .setPadding(Map.of("Horizontal", "12", "Vertical", "5"));
        globalEnableDisable.getLabel()
            .setStyle(Map.of("TextColor", "#FFFFFF", "RenderUppercase", "true", "RenderBold", "true"));
        UIGroup globalActionsRow = (UIGroup) new UIGroup("GlobalActionsRow")
            .appendChild(globalEnableDisable.get())
            .setMode(LayoutMode.CENTER_MIDDLE)
            .setPadding(Map.of("Vertical", "10"));
        UIGroup globalActionsGroup = (UIGroup) new UIGroup("GlobalActionsGroup")
            .appendChild(globalActionsRow.get())
            .setMode(LayoutMode.TOP);
        
        String globalEnableDisableFQHP = HashBuilder.fullQualifiedPath(
            entryPoint,
            globalActionsGroup.getId(),
            globalActionsRow.getId(),
            globalEnableDisable.getId()
        );
        
        uiCommandBuilder = uiCommandBuilder
            .appendInline(entryPoint, configSettings.get())
            .appendInline(entryPoint, globalStatusGroup.get())
            .appendInline(entryPoint, globalActionsGroup.get());
        
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, globalEnableDisableFQHP,
            EventData.of(globalEnableDisable.getId(), String.valueOf(config.isEnabled())));
        
        return uiCommandBuilder;
    }
    
    private UILabel getAutoBooleanLabel(String name, boolean enabled) {
        UILabel booleanLabel = (UILabel) new UILabel(name + "Label", name)
            .setBackground(Map.of("Color",
                enabled ? UXDesignColor.ENABLED.getColor() : UXDesignColor.DISABLED.getColor()))
            .setAnchors(Map.of("Width", "125", "Left", "5", "Right", "5"))
            .setPadding(Map.of("Horizontal", "15"))
            .setStyle(Map.of(
                "RenderBold", "true",
                "HorizontalAlignment", "Center",
                "VerticalAlignment", "Center",
                "Alignment", "Center",
                "TextColor", "#FFFFFF"));
        
        return booleanLabel;
    }
}
