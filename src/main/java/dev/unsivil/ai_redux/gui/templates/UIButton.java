package dev.unsivil.ai_redux.gui.templates;

import dev.unsivil.ai_redux.gui.templates.enums.LayoutMode;


public class UIButton extends UIElement<UIButton> {
    protected LayoutMode mode;
    protected boolean disabled;
    protected String child;
    
    public UIButton(String id) {
        this.id = id;
    }
    
    public UIButton setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }
    
    public UIButton setMode(LayoutMode mode) {
        this.mode = mode;
        return this;
    }
    
    public UIButton appendChild(String child) {
        this.child = child;
        return this;
    }
    
    public UIButton clearChildren() {
        this.child = null;
        return this;
    }
    
    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append("Button #%s {".formatted(this.id));
        if (mode != null) sb.append("LayoutMode: %s;".formatted(mode.get()));
        if (background != null) sb.append("Background: %s;".formatted(background));
        if (anchors != null) sb.append("Anchor: %s;".formatted(anchors));
        if (padding != null) sb.append("Padding: %s;".formatted(padding));
        if (style != null) sb.append("Style: %s;".formatted(style));
        if (flexWeight != null) sb.append("FlexWeight: %d;".formatted(Integer.valueOf(flexWeight)));
        if (disabled) sb.append("Disabled: %s;".formatted(disabled));
        if (child != null) sb.append(child);
        sb.append("}");
        return sb.toString();
    }
}
