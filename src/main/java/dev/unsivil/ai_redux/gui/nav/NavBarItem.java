package dev.unsivil.ai_redux.gui.nav;

import java.util.HashMap;
import java.util.Map;

import dev.unsivil.ai_redux.gui.templates.UIAssetImage;
import dev.unsivil.ai_redux.gui.templates.UIButton;
import dev.unsivil.ai_redux.gui.templates.UIGroup;
import dev.unsivil.ai_redux.gui.templates.UILabel;
import dev.unsivil.ai_redux.gui.templates.enums.LayoutMode;


public class NavBarItem {
    private static final Map<String, Map<String, String>> LABEL_STYLE = new HashMap<>();
    
    static {
        LABEL_STYLE.put("selected", Map.of("RenderBold", "true", "RenderUppercase", "true", "TextColor", "#BAC9EC"));
        LABEL_STYLE.put("standard", Map.of("RenderUppercase", "true", "TextColor", "#6E7DA1"));
    }
    
    private final UIGroup iconGroup;
    private final UILabel label;
    private final UIButton button;
    private final String id;
    private int iconSize = 15;
    private String iconPath;
    private boolean isSelected;
    private boolean rtl;
    
    public NavBarItem(String id, String text, String iconPath) {
        this(id, text, iconPath, false);
    }
    
    public NavBarItem(String id, String text, String iconPath, boolean defaultSelected) {
        this.id = id;
        this.iconPath = iconPath;
        this.isSelected = defaultSelected;
        this.button = new UIButton(id + "Button");
        this.label = (UILabel) new UILabel(id + "Label", text)
            .setPadding(Map.of("Vertical", "5", "Left", "10"));
        this.iconGroup = new UIGroup(id + "Group");
    }
    
    public UIButton getButton() { return this.button; }
    
    public UILabel getLabel() { return this.label; }
    
    public String getID() { return button.getId(); }
    
    public NavBarItem setIconSize(int size) {
        this.iconSize = size;
        return this;
    }
    
    public NavBarItem setRTL(boolean rtl) {
        this.rtl = rtl;
        return this;
    }
    
    public NavBarItem setSelected(boolean selected) {
        this.isSelected = selected;
        return this;
    }
    
    public UIAssetImage getIcon() { // 
        return (UIAssetImage) new UIAssetImage(id + "Icon")
            .setMaskTexturePath(iconPath)
            .setBackground(Map.of("Color", "#6E7DA1"))
            .setAnchors(
                Map.of(
                    "Height", String.valueOf(iconSize),
                    "Width", String.valueOf(iconSize),
                    (rtl ? "Left" : "Right"), "8"
                ));
    }
    
    public String get() {
        button.setMode(LayoutMode.MIDDLE)
            .setAnchors(Map.of("Bottom", "5", "Horizontal", "1"));
        
        label.setStyle(LABEL_STYLE.get("standard"));
        
        var icon = getIcon();
        if (isSelected) {
            icon.setBackground(Map.of("Color", "#BAC9EC"));
            label.setStyle(LABEL_STYLE.get("selected"));
            button.setBackground(Map.of("Color", "#6E7DA1(0.28)"));
        }
        if (rtl) {
            iconGroup.setMode(LayoutMode.RIGHT)
                .appendChild(label.get())
                .appendChild(icon.get());
            button.setPadding(Map.of("Right", "20", "Vertical", "6"));
        } else {
            iconGroup.setMode(LayoutMode.LEFT)
                .appendChild(icon.get())
                .appendChild(label.get());
            button.setPadding(Map.of("Left", "20", "Vertical", "6"));
        }
        return button.appendChild(iconGroup.get()).get();
    }
}
