package moe.aira.enums;

public enum ColorType {
    SPARKLE(0xff2861), FLASH(0xffd700), BRILLIANT(0x2cb7ff), GLITTER(0x9ad114), ALL(0);
    public final int rgb;

    ColorType(int rgb) {
        this.rgb = rgb;
    }
}
