package dev.unsivil.ai_redux.gui.templates;

import java.util.HashMap;
import java.util.Map;

import dev.unsivil.ai_redux.gui.templates.enums.UXDesignColor;


public class UILabelButton extends UIButton {
    private final UILabel label;
    
    public UILabelButton(String id, String text) {
        super(id);
        this.label = new UILabel(id + "Label", text);
    }
    
    public UILabel getLabel() { return this.label; }
    
    public UILabelButton setBackground(UXDesignColor color) {
        super.setBackground(Map.of("Color", color.getColor()));
        return this;
    }
    
    public UILabelButton setForeground(UXDesignColor color) {
        Map<String, String> updated = new HashMap<>();
        if (this.label.style != null) {
            Map<String, String> existing = new HashMap<>() {
                {
                    for (String kv : label.style.replaceAll("[()]", "").split(",")) {
                        String[] pair = kv.split(":");
                        put(pair[0], pair[1].trim());
                    }
                }
            };
            if (existing.containsKey("TextColor")) existing.remove("TextColor");
            updated.putAll(existing);
        }
        updated.put("TextColor", color.getColor());
        this.label.setStyle(updated);
        return this;
    }
    
    @Override
    public String get() {
        this.appendChild(this.label.get());
        return super.get();
    }
}
