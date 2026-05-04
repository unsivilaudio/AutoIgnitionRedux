package dev.unsivil.ai_redux.gui.templates;

import java.util.HashMap;
import java.util.Map;

import dev.unsivil.ai_redux.gui.templates.enums.LayoutMode;


public class UICheckBox extends UIElement<UICheckBox> {
    private static final Map<String, String> symbol = new HashMap<>() {
        {
            put("checked", "Assets/icons/flaticon_check.png");
            put("unchecked", "Assets/icons/flaticon_close.png");
        }
    };
    
    public enum Checked {
        CHECKED("#62e744"), UNCHECKED("#eab4aa");
        
        private final String hexColor;
        
        Checked(String hexColor) {
            this.hexColor = hexColor;
        }
        
        public String getColor() { return this.hexColor; }
    }
    
    private boolean checked;
    private LayoutMode mode = LayoutMode.CENTER_MIDDLE;
    private int checkBoxSize = 20;
    private String child;
    
    public UICheckBox(String id) {
        this(id, Checked.UNCHECKED);
    }
    
    public UICheckBox(String id, Checked defaultChecked) {
        this.id = id;
        this.checked = defaultChecked == Checked.CHECKED;
        this.setBackground(Map.of("Color", defaultChecked.getColor()));
        setCheckBoxSize(checkBoxSize);
    }
    
    public String getValue() { return Boolean.toString(checked); }
    
    public UICheckBox setMode(LayoutMode mode) {
        this.mode = mode;
        return this;
    }
    
    public final UICheckBox setCheckBoxSize(int checkBoxSize) {
        this.checkBoxSize = checkBoxSize;
        Map<String, String> updatedAnchors = new HashMap<>(Map.of(
            "Height", String.valueOf(this.checkBoxSize),
            "Width", String.valueOf(this.checkBoxSize)
        ));
        this.setAnchors(updatedAnchors);
        return this;
    }
    
    public UICheckBox setChecked(Checked isChecked) {
        this.setBackground(Map.of("Color", isChecked.getColor()));
        this.setChecked(isChecked == Checked.CHECKED);
        return this;
    }
    
    public UICheckBox setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }
    
    public UICheckBox appendChild(String child) {
        this.child = child;
        return this;
    }
    
    private void appendSymbol(String maskTexturePath) {
        int fontSize = (int) (this.checkBoxSize * 0.7);
        UIAssetImage icon = (UIAssetImage) new UIAssetImage("checked")
            .setMaskTexturePath(maskTexturePath)
            .setBackground(Map.of("Color", "#282728"))
            .setAnchors(Map.of(
                "Height", String.valueOf(fontSize),
                "Width", String.valueOf(fontSize))
            );
        this.appendChild(icon.get());
    }
    
    @Override
    public UICheckBox setAnchors(Map<String, String> updated) {
        Map<String, String> updatedAnchors = new HashMap<>();
        if (this.anchors != null) {
            Map<String, String> existing = new HashMap<>() {
                {
                    String[] pairs = anchors.replaceAll("[();]", "").split(",");
                    for (String kv : pairs) {
                        String[] pair = kv.split(":");
                        put(pair[0].trim(), pair[1].replaceAll("\"", "").trim());
                    }
                }
            };
            updatedAnchors.putAll(existing);
        }
        updatedAnchors.putAll(updated);
        super.setAnchors(updatedAnchors);
        return this;
    }
    
    @Override
    public String get() {
        appendSymbol(symbol.get((String) (checked ? "checked" : "unchecked")));
        StringBuilder sb = new StringBuilder();
        sb.append("Button #%s {".formatted(id))
            .append("OutlineColor: #CCCCCC(0.35);")
            .append("OutlineSize: 1.0;");
        if (mode != null) sb.append("LayoutMode: %s;".formatted(mode.get()));
        if (background != null) sb.append("Background: %s;".formatted(background));
        if (anchors != null) sb.append("Anchor: %s;".formatted(anchors));
        if (padding != null) sb.append("Padding: %s;".formatted(padding));
        if (style != null) sb.append("Style: %s;".formatted(style));
        if (child != null) sb.append(child);
        sb.append("}");
        return sb.toString();
    }
}
