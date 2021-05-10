package me.htrewrite.client.clickgui;

import me.htrewrite.client.clickgui.components.Colors;

public class StaticClickGuiColor {
    public static int newColor(int r, int g, int b, int a) { return (r << 24 | (g << 16) | (b << 8) | a); }
}