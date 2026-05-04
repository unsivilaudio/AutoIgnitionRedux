package dev.unsivil.ai_redux.gui.templates;

public class UITextField extends UIElement<UITextField> {
    private String value;
    private String placeholderText;
    
    public UITextField(String id) {
        this.id = id;
    }
    
    public UITextField setValue(String value) {
        this.value = value;
        return this;
    }
    
    public UITextField setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
        return this;
    }
    
    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append("TextField #%s {".formatted(id));
        if (value != null) sb.append("Value: \"%s\";".formatted(value));
        if (placeholderText != null) sb.append("PlaceholderText: \"%s\";".formatted(placeholderText));
        if (background != null) sb.append("Background: %s;".formatted(background));
        if (anchors != null) sb.append("Anchor: %s;".formatted(anchors));
        if (padding != null) sb.append("Padding: %s;".formatted(padding));
        if (style != null) sb.append("Style: %s;".formatted(style));
        sb.append("}");
        return sb.toString();
    }
}
