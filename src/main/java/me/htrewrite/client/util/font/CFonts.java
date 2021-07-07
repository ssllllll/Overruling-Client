package me.htrewrite.client.util.font;

import java.awt.*;

public class CFonts {
    public static CFontRenderer roboto15, roboto18, roboto22;

    public static void load() {
        roboto15 = new CFontRenderer(new Font("Roboto", Font.PLAIN, 15), true, 8);
        roboto18 = new CFontRenderer(new Font("Roboto", Font.PLAIN, 18), true, 8);
        roboto22 = new CFontRenderer(new Font("Roboto", Font.PLAIN, 22), true, 8);
    }
}