package dev.unsivil.ai_redux.gui.templates;

import java.util.Map;

import dev.unsivil.ai_redux.gui.templates.UICheckBox.Checked;
import dev.unsivil.ai_redux.gui.templates.enums.LayoutMode;


public class UILabelCheckBox extends UIElement<UILabelCheckBox> {
    private final UILabel label;
    private final UICheckBox checkBox;
    private LayoutMode mode;
    private boolean rtl = false;
    
    public UILabel getLabel() { return label; }
    
    public UICheckBox getCheckBox() { return checkBox; }
    
    public UILabelCheckBox(String id, String label) {
        this(id, label, Checked.UNCHECKED);
    }
    
    public UILabelCheckBox(String id, String label, Checked defaultChecked) {
        this.id = id;
        this.label = new UILabel(id + "Label", label);
        this.checkBox = new UICheckBox(id + "Box", defaultChecked);
    }
    
    public UILabelCheckBox setCheckBoxSize(int checkBoxSize) {
        this.checkBox.setCheckBoxSize(checkBoxSize);
        return this;
    }
    
    public UILabelCheckBox setIsChecked(Checked isChecked) {
        this.checkBox.setChecked(isChecked);
        return this;
    }
    
    public UILabelCheckBox setMode(LayoutMode mode) {
        this.mode = mode;
        return this;
    }
    
    public UILabelCheckBox setRTL(boolean rtl) {
        this.rtl = rtl;
        if (this.rtl) {
            this.checkBox.setAnchors(Map.of("Left", "8"));
        } else {
            this.checkBox.setAnchors(Map.of("Right", "8"));
        }
        return this;
    }
    
    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append("Group #%s {".formatted(id));
        if (mode != null) sb.append("LayoutMode: %s;".formatted(mode.get()));
        if (anchors != null) sb.append("Anchor: %s;".formatted(anchors));
        if (padding != null) sb.append("Padding: %s;".formatted(padding));
        if (style != null) sb.append("Style: %s;".formatted(style));
        if (rtl) {
            sb.append(label.get());
            sb.append(checkBox.get());
        } else {
            sb.append(checkBox.get());
            sb.append(label.get());
        }
        sb.append("}");
        return sb.toString();
    }
}
