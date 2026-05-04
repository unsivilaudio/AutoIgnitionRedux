package dev.unsivil.ai_redux.gui.templates;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;


public abstract class UIPage {
    protected final String entryPoint;
    
    public UIPage(String entryPoint) {
        this.entryPoint = entryPoint;
    }
    
    public abstract UICommandBuilder build(UICommandBuilder uiCommandBuilder, UIEventBuilder uiEventBuilder);
}
