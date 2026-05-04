package dev.unsivil.ai_redux.gui.templates.enums;

public enum LayoutMode {
    TOP("Top"), TOP_SCROLLING("TopScrolling"), LEFT("Left"), RIGHT("Right"),
    FULL("Full"), MIDDLE("Middle"), MIDDLE_CENTER("MiddleCenter"),
    CENTER_MIDDLE("CenterMiddle"), LEFT_CENTER_WRAP("LeftCenterWrap"), CENTER("Center"),
    BOTTOM("Bottom"), BOTTOM_SCROLLING("BottomScrolling");
    
    String property;
    
    LayoutMode(String property) {
        this.property = property;
    }
    
    public String get() {
        return this.property;
    }
}
