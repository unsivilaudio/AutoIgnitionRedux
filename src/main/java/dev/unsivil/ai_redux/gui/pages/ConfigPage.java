package dev.unsivil.ai_redux.gui.pages;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.config.AutoIgnitionReduxConfig;
import dev.unsivil.ai_redux.gui.templates.*;
import dev.unsivil.ai_redux.gui.templates.enums.LayoutMode;
import dev.unsivil.ai_redux.gui.templates.enums.UXDesignColor;
import dev.unsivil.ai_redux.gui.util.HashBuilder;


public class ConfigPage extends UIPage {
    public static final HytaleLogger logger = AutoIgnitionRedux.getInstance().getLogger();
    private final AutoIgnitionReduxConfig config;
    
    public ConfigPage(String entryPoint, AutoIgnitionReduxConfig config) {
        super(entryPoint);
        this.config = config;
    }
    
    @Override
    public UICommandBuilder build(@Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder) {
        uiCommandBuilder = uiCommandBuilder.clear(entryPoint);
        
        // BUILDING STRING INPUT CONTROLS
        UIGroup scanFrGroup = (UIGroup) buildInputRow(
            "ScanFrequency", "Scan Frequency",
            String.valueOf((long) config.getScanInterval()),
            uiEventBuilder
        )
            .setPadding(Map.of("Left", "20"));
        UIGroup neighborFrGroup = (UIGroup) buildInputRow(
            "NeighborScanFrequency", "Container Scan Frequency",
            String.valueOf((long) config.getScanNeighborInterval()),
            uiEventBuilder
        )
            .setPadding(Map.of("Left", "20"));
        
        uiCommandBuilder = uiCommandBuilder
            .appendInline(entryPoint, scanFrGroup.get())
            .appendInline(entryPoint, neighborFrGroup.get());
        
        // BUILDING CHECKABLE BOX CONTROLS
        UIGroup startStopFill = (UIGroup) buildCheckBoxRow("CBGroupStartStopFill", new LinkedHashMap<>() {
            {
                put("AutoStart", Map.of("AutoStart", config.isAutoStart()));
                put("AutoStop", Map.of("AutoStop", config.isAutoStop()));
                put("AutoFill", Map.of("AutoFill", config.isAutoFill()));
            }
        }, uiEventBuilder)
            .setMode(LayoutMode.CENTER)
            .setPadding(Map.of("Left", "20"))
            .setAnchors(Map.of("Horizontal", "1", "Bottom", "15", "Top", "25"));
        
        UIGroup emptyFuel = (UIGroup) buildCheckBoxRow("CBGroupEmptyFuel", new LinkedHashMap<>() {
            {
                put("AutoEmpty", Map.of("AutoEmpty", config.isAutoEmpty()));
                put("AutoFuel", Map.of("AutoFuel", config.isAutoFuel()));
            }
        }, uiEventBuilder)
            .setMode(LayoutMode.CENTER)
            .setPadding(Map.of("Left", "20"))
            .setAnchors(Map.of("Horizontal", "1", "Bottom", "15"));
        
        uiCommandBuilder = uiCommandBuilder
            .appendInline(entryPoint, startStopFill.get())
            .appendInline(entryPoint, emptyFuel.get());
        
        // BUILDING FORM ACTIONS
        UILabelButton saveButton = (UILabelButton) new UILabelButton("ConfigButtonSave", "Save")
            .setForeground(UXDesignColor.SECONDARY_ONSURFACE)
            .setBackground(UXDesignColor.CTA_PRIMARY)
            .setMode(LayoutMode.CENTER)
            .setAnchors(Map.of("Width", "125", "Height", "30"))
            .setPadding(Map.of("Horizontal", "12", "Vertical", "5"));
        saveButton.getLabel()
            .setStyle(Map.of("FontSize", "15", "RenderUppercase", "true", "RenderBold", "true"))
            .setAnchors(Map.of("Vertical", "1", "Horizontal", "1"));
        UIGroup actionRow = (UIGroup) new UIGroup("ConfigActionRow")
            .setMode(LayoutMode.RIGHT)
            .setAnchors(Map.of("Height", "30"))
            .setPadding(Map.of("Horizontal", "35"));
        UIGroup actionGroup = (UIGroup) new UIGroup("ConfigActions")
            .setMode(LayoutMode.BOTTOM)
            .appendChild(actionRow.appendChild(saveButton.get()).get())
            .setAnchors(Map.of("Full", "1", "Height", "100"))
            .setPadding(Map.of("Horizontal", "30"))
            .setFlexWeight(1);
        
        uiCommandBuilder = uiCommandBuilder.appendInline(entryPoint, actionGroup.get());
        
        String saveButtonFQHP = HashBuilder.fullQualifiedPath(
            entryPoint, actionGroup.getId(), actionRow.getId(), saveButton.getId()
        );
        
        uiEventBuilder.addEventBinding(
            CustomUIEventBindingType.Activating,
            saveButtonFQHP,
            EventData.of("ConfigButton", "Save"));
        
        return uiCommandBuilder;
    }
    
    private UIGroup buildInputRow(String rowPrefix, String labelText, String defaultValue, UIEventBuilder uiEventBuilder) {
        
        UILabel rowLabel = (UILabel) new UILabel(rowPrefix + "Label", labelText)
            .setStyle(Map.of(
                "FontSize", "15",
                "RenderBold", "true",
                "TextColor", "#E1952F",
                "VerticalAlignment", "Center",
                "HorizontalAlignment", "End"))
            .setAnchors(Map.of("Width", "250", "Vertical", "1"));
        UITextField rowInputField = (UITextField) new UITextField(rowPrefix + "InputField")
            .setValue(defaultValue)
            .setPlaceholderText(defaultValue)
            .setBackground(Map.of("Color", "#E4E3FA"))
            .setStyle(Map.of("TextColor", "#3D4548"))
            .setAnchors(Map.of("Width", "80", "Vertical", "1"))
            .setPadding(Map.of("Horizontal", "12", "Vertical", "5"));
        UILabel rowInputSuffix = (UILabel) new UILabel(rowPrefix + "InputSuffix", "ms")
            .setStyle(Map.of(
                "TextColor", "#E1952F",
                "RenderBold", "true",
                "FontSize", "10"))
            .setAnchors(Map.of("Left", "8"));
        UIGroup rowInputGroup = (UIGroup) new UIGroup(rowPrefix + "InputGroup")
            .setMode(LayoutMode.LEFT)
            .appendChild(rowInputField.get())
            .appendChild(rowInputSuffix.get())
            .setFlexWeight(1)
            .setAnchors(Map.of("Vertical", "1", "Left", "15"));
        
        UIGroup rowGroup = (UIGroup) new UIGroup(rowPrefix + "Group")
            .setMode(LayoutMode.LEFT)
            .appendChild(rowLabel.get())
            .appendChild(rowInputGroup.get())
            .setAnchors(Map.of("Height", "30", "Bottom", "15"));
        
        String rowInputFQHP = HashBuilder.fullQualifiedPath(
            entryPoint,
            rowGroup.getId(),
            rowInputGroup.getId(),
            rowInputField.getId()
        );
        uiEventBuilder.addEventBinding(
            CustomUIEventBindingType.ValueChanged,
            rowInputFQHP,
            EventData.of(
                '@' + rowInputField.getId().substring(1),
                rowInputField.getId().concat(".Value"))
        );
        
        return rowGroup;
    }
    
    public UIGroup buildCheckBoxRow(String id, Map<String, Map<String, Boolean>> fields, UIEventBuilder uiEventBuilder) {
        UIGroup rowGroup = new UIGroup(id);
        fields.entrySet().stream()
            .forEach((e) -> {
                var fieldId = e.getKey();
                e.getValue().entrySet().stream()
                    .forEach((fe) -> {
                        UILabelCheckBox rowCheckBox =
                            new UILabelCheckBox(fieldId, fe.getKey(),
                                fe.getValue() == true
                                    ? UICheckBox.Checked.CHECKED
                                    : UICheckBox.Checked.UNCHECKED);
                        rowCheckBox
                            .setRTL(true)
                            .setMode(LayoutMode.LEFT)
                            .setAnchors(Map.of("Width", "150"))
                            .setPadding(Map.of("Full", "8"));
                        rowCheckBox.getCheckBox()
                            .setMode(LayoutMode.CENTER);
                        rowCheckBox.getLabel()
                            .setStyle(Map.of(
                                "TextColor", "#E1952F",
                                "RenderBold", "true",
                                "FontSize", "15"));
                        rowGroup.appendChild(rowCheckBox.get());
                        String rowCheckBoxFQHP = HashBuilder.fullQualifiedPath(
                            entryPoint, rowGroup.getId(), rowCheckBox.getId(), rowCheckBox.getCheckBox().getId()
                        );
                        
                        uiEventBuilder.addEventBinding(
                            CustomUIEventBindingType.Activating,
                            rowCheckBoxFQHP,
                            EventData.of(
                                rowCheckBox.getCheckBox().getId(),
                                rowCheckBox.getCheckBox().getValue()));
                    });
            });
        return rowGroup;
    }
}
