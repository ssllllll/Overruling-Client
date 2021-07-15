package me.htrewrite.client.clickgui;

public class StaticClickGuiColor {
    public static int newColor(int r, int g, int b, int a) { return (r << 24 | (g << 16) | (b << 8) | a); }
}