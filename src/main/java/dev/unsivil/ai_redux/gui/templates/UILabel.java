package dev.unsivil.ai_redux.gui.templates;

public class UILabel extends UIElement<UILabel> {
    private String text;
    
    public UILabel(String id, String text) {
        this.id = id;
        this.text = text;
    }
    
    public UILabel setText(String text) {
        this.text = text;
        return this;
    }
    
    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append("Label #%s {".formatted(this.id));
        sb.append("Text: \"%s\";".formatted(text));
        if (background != null) sb.append("Background: %s;".formatted(background));
        if (anchors != null) sb.append("Anchor: %s;".formatted(anchors));
        if (padding != null) sb.append("Padding: %s;".formatted(padding));
        if (style != null) sb.append("Style: %s;".formatted(style));
        if (flexWeight != null) sb.append("FlexWeight: %d;".formatted(Integer.valueOf(flexWeight)));
        sb.append("}");
        return sb.toString();
    }
}
