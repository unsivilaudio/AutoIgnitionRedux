package dev.unsivil.ai_redux.gui.templates;

import java.util.Map;
import java.util.stream.Collectors;


public abstract class UIElement<T extends UIElement<T>> {
    protected String id;
    protected String anchors;
    protected String padding;
    protected String style;
    protected String background;
    protected String flexWeight;
    protected String tooltipText;
    
    public String getId() { return "#" + id; }
    
    public UIElement<T> setAnchors(Map<String, String> anchors) {
        this.anchors = anchors.entrySet().stream()
            .map((e) -> "%s: %s".formatted(e.getKey(), e.getValue()))
            .collect(Collectors.joining(",", "(", ")"));
        return this;
    }
    
    public UIElement<T> setPadding(Map<String, String> padding) {
        this.padding = padding.entrySet().stream()
            .map((e) -> "%s: %s".formatted(e.getKey(), e.getValue()))
            .collect(Collectors.joining(",", "(", ")"));
        return this;
    }
    
    public UIElement<T> setStyle(Map<String, String> styles) {
        this.style = styles.entrySet().stream()
            .map((e) -> "%s: %s".formatted(e.getKey(), e.getValue()))
            .collect(Collectors.joining(",", "(", ")"));
        return this;
    }
    
    public UIElement<T> setBackground(Map<String, String> background) {
        this.background = background.entrySet().stream()
            .map((e) -> "%s: %s".formatted(e.getKey(), e.getValue()))
            .collect(Collectors.joining(",", "(", ")"));
        return this;
    }
    
    public UIElement<T> setBackground(String background) {
        this.background = background;
        return this;
    }
    
    public UIElement<T> setFlexWeight(int flexWeight) {
        this.flexWeight = String.valueOf(flexWeight);
        return this;
    }
    
    public UIElement<T> setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
        return this;
    }
    
    public abstract String get();
}
