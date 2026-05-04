package dev.unsivil.ai_redux.gui.templates.enums;

public enum UXDesignColor {
    BRAND("#E1952F"),
    PRIMARY("#141C26"), PRIMARY_ONSURFACE("#6E7DA1"),
    SECONDARY("#0A1119"), SECONDARY_ONSURFACE("#BEBEC9"),
    ENABLED("#43ab13"), DISABLED("#e7572f"),
    CTA_PRIMARY("#527EE7"), CTA_CAUTION("#b5780f"), CTA_DANGER("#DA5333");
    
    private final String hexColor;
    
    private UXDesignColor(String hexColor) {
        this.hexColor = hexColor;
    }
    
    public String getColor() { return this.hexColor; }
}
