package dev.unsivil.ai_redux.gui.templates;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.unsivil.ai_redux.gui.templates.enums.LayoutMode;


public class UIGroup extends UIElement<UIGroup> {
    private LayoutMode mode;
    private final List<String> children = new LinkedList<>();
    
    public UIGroup(String id) {
        this(id, null, null);
    }
    
    public UIGroup(String id, Map<String, String> anchors, Map<String, String> padding) {
        this.id = id;
        if (anchors != null) {
            this.anchors = anchors.entrySet().stream()
                .map((e) -> "%s: %s".formatted(e.getKey(), e.getValue()))
                .collect(Collectors.joining(",", "(", ")"));
        }
        if (padding != null) {
            this.padding = padding.entrySet().stream()
                .map((e) -> "%s: %s".formatted(e.getKey(), e.getValue()))
                .collect(Collectors.joining(",", "(", ")"));
        }
    }
    
    private UIGroup(String id, UIGroup group) {
        this.id = id;
        mode = group.mode;
        anchors = group.anchors;
        padding = group.padding;
        background = group.background;
    }
    
    public UIGroup setMode(LayoutMode mode) {
        this.mode = mode;
        return this;
    }
    
    public UIGroup appendChild(String child) {
        children.add(child);
        return this;
    }
    
    public UIGroup clearChildren() {
        children.clear();
        return this;
    }
    
    public UIGroup clone(String id) {
        return new UIGroup(id, this);
    }
    
    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append("Group #%s {".formatted(this.id));
        if (mode != null) sb.append("LayoutMode: %s;".formatted(mode.get()));
        if (background != null) sb.append("Background: %s;".formatted(background));
        if (anchors != null) sb.append("Anchor: %s;".formatted(anchors));
        if (padding != null) sb.append("Padding: %s;".formatted(padding));
        if (style != null) sb.append("Style: %s;".formatted(style));
        if (flexWeight != null) sb.append("FlexWeight: %d;".formatted(Integer.valueOf(flexWeight)));
        for (String child : children) {
            sb.append(child);
        }
        sb.append("}");
        return sb.toString();
    }
}
