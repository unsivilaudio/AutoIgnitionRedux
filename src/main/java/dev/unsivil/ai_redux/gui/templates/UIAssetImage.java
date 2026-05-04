package dev.unsivil.ai_redux.gui.templates;

public class UIAssetImage extends UIElement<UIAssetImage> {
    private String assetPath;
    private String maskTexturePath;
    
    public UIAssetImage(String id) {
        this(id, null);
    }
    
    public UIAssetImage(String id, String assetPath) {
        this.id = id;
        if (assetPath != null) {
            this.assetPath = "UI/Custom/".concat(assetPath);
        }
    }
    
    public UIAssetImage setMaskTexturePath(String maskPath) {
        this.maskTexturePath = maskPath;
        return this;
    }
    
    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append("AssetImage #%s {".formatted(id));
        if (assetPath != null) sb.append("AssetPath: \"%s\";".formatted(assetPath));
        if (background != null) sb.append("Background: %s;".formatted(background));
        if (maskTexturePath != null) sb.append("MaskTexturePath: \"%s\";".formatted(maskTexturePath));
        if (anchors != null) sb.append("Anchor: %s;".formatted(anchors));
        if (padding != null) sb.append("Padding: %s;".formatted(padding));
        sb.append("}");
        return sb.toString();
    }
}
