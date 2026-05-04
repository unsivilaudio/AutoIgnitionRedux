package dev.unsivil.ai_redux.gui.pages;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;

import dev.unsivil.ai_redux.AutoIgnitionRedux;
import dev.unsivil.ai_redux.gui.templates.UIAssetImage;
import dev.unsivil.ai_redux.gui.templates.UIGroup;
import dev.unsivil.ai_redux.gui.templates.UILabel;
import dev.unsivil.ai_redux.gui.templates.UIPage;
import dev.unsivil.ai_redux.gui.templates.enums.LayoutMode;


public class AboutPage extends UIPage {
    private final UIAssetImage heartIcon = new UIAssetImage("IconHeart", "Assets/icons/flaticon_heart.png");
    private final UIAssetImage coffeeIcon = new UIAssetImage("IconCoffeeCup", "Assets/icons/flaticon_coffee-cup.png");
    private final String blurb = """
        AutoIgnitionRedux is a utility for automation of furnace workflows,
        based on the original works of Lutia (https://github.com/lukkoedm/AutoIgnition).
        """;
    
    public AboutPage(String entryPoint) {
        super(entryPoint);
    }
    
    @Override
    public UICommandBuilder build(@Nonnull UICommandBuilder uiCommandBuiler, @Nullable UIEventBuilder uiEventBuilder) {
        uiCommandBuiler = uiCommandBuiler.clear(entryPoint);
        AutoIgnitionRedux mod = AutoIgnitionRedux.getInstance();
        var manifest = mod.getManifest();
        
        UIAssetImage iconImage = (UIAssetImage) new UIAssetImage("AppIcon", "Assets/icon-256.png")
            .setAnchors(Map.of("Width", "150", "Height", "150"));
        UIGroup iconGroup = (UIGroup) new UIGroup("AppIconGroup")
            .setMode(LayoutMode.MIDDLE_CENTER)
            .appendChild(iconImage.get())
            .setAnchors(Map.of("Width", "180", "Height", "180", "Horizontal", "1"))
            .setPadding(Map.of("Top", "16", "Bottom", "16"));
        
        UILabel nameLabel = (UILabel) new UILabel("AppNameLabel", "AutoIgnitionRedux v%s".formatted(manifest.getVersion()))
            .setStyle(Map.of(
                "TextColor", "#996C04",
                "FontSize", "18",
                "VerticalAlignment", "Center"
            ));
        UIGroup nameGroup = (UIGroup) new UIGroup("AppNameGroup")
            .setMode(LayoutMode.MIDDLE_CENTER)
            .appendChild(nameLabel.get());
        
        UILabel updateLabel = (UILabel) new UILabel("AppUpdateLabel", "Last Update: %s".formatted(
            AutoIgnitionRedux.getLastUpdateDate()
        ))
            .setStyle(Map.of(
                "TextColor", "#8ECAFB",
                "FontSize", "10",
                "RenderBold", "true",
                "RenderUppercase", "true",
                "VerticalAlignment", "Center"
            ));
        
        UIGroup updateGroup = (UIGroup) new UIGroup("AppUpdateGroup")
            .setMode(LayoutMode.MIDDLE_CENTER)
            .appendChild(updateLabel.get())
            .setAnchors(Map.of("Bottom", "15"));
        
        Map<String, String> iconAnchors = Map.of("Height", "15", "Width", "15", "Left", "5", "Right", "5");
        UIGroup blurbIcons = (UIGroup) new UIGroup("AppBlurbIcons")
            .setMode(LayoutMode.CENTER_MIDDLE)
            .appendChild(new UILabel("BlurbIconLabel", "A plugin made with").setStyle(Map.of("FontSize", "12")).get())
            .appendChild(coffeeIcon.setAnchors(iconAnchors).get())
            .appendChild(new UILabel("BlurbIconLabel2", "and").setStyle(Map.of("FontSize", "12")).get())
            .appendChild(heartIcon.setAnchors(iconAnchors).get())
            .appendChild(new UILabel("BlubTextLabel3", "by Unsivil").setStyle(Map.of("FontSize", "12")).get())
            .setAnchors(Map.of("Bottom", "15"));
        
        UILabel blurbDesc = (UILabel) new UILabel("AppBlurbDesc", blurb)
            .setStyle(Map.of(
                "FontSize", "12",
                "TextColor", "#FFFFFF",
                "Alignment", "Center",
                "Wrap", "true"))
            .setAnchors(Map.of("Full", "1"))
            .setPadding(Map.of("Full", "15"));
        UIGroup blurbGroup = new UIGroup("AppBlurbGroup")
            .setMode(LayoutMode.MIDDLE_CENTER)
            .appendChild(blurbIcons.get())
            .appendChild(blurbDesc.get());
        
        return uiCommandBuiler
            .appendInline(entryPoint, iconGroup.get())
            .appendInline(entryPoint, nameGroup.get())
            .appendInline(entryPoint, updateGroup.get())
            .appendInline(entryPoint, blurbGroup.get());
    }
}
