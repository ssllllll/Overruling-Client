package me.htrewrite.client.module.modules.render;

import me.htrewrite.client.clickgui.components.buttons.settings.bettermode.BetterMode;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.ChatColor;
import me.htrewrite.exeterimports.mcapi.settings.ModeSetting;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;
import me.htrewrite.salimports.util.ESPUtil;

public class HoleESPModule extends Module {
    public static final ModeSetting mode = new ModeSetting("Mode", 0, BetterMode.construct(ChatColor.enumToStringArray(ESPUtil.HoleModes.values())));
    public static final ValueSetting<Double> radius = new ValueSetting<>("Radius", 8d, 0d, 32d);
    public static final ToggleableSetting ignoreOwnHole = new ToggleableSetting("IgnoreOwnHole", false);

    public static final ModeSetting color_setting = new ModeSetting("ColorSetting", 0, BetterMode.construct("OBSIDIAN", "BEDROCK"));
    /* OBSIDIAN */
    public static final ToggleableSetting O_Rainbow = new ToggleableSetting("O_Rainbow", false);
    public static final ValueSetting<Double> O_Red = new ValueSetting<>("O_Red", 1d, 0d, 1d);
    public static final ValueSetting<Double> O_Green = new ValueSetting<>("O_Green", 0d, 0d , 1d);
    public static final ValueSetting<Double> O_Blue = new ValueSetting<>("O_Blue", 0d, 0d, 1d);
    public static final ValueSetting<Double> O_Alpha = new ValueSetting<>("O_Alpha", .5d, 0d, 1d);
    /* BEDROCK */
    public static final ToggleableSetting B_Rainbow = new ToggleableSetting("B_Rainbow", false);
    public static final ValueSetting<Double> B_Red = new ValueSetting<>("B_Red", 0d, 0d, 1d);
    public static final ValueSetting<Double> B_Green = new ValueSetting<>("B_Green", 1d, 0d, 1d);
    public static final ValueSetting<Double> B_Blue = new ValueSetting<>("B_Blue", 0d, 0d, 1d);
    public static final ValueSetting<Double> B_Alpha = new ValueSetting<>("B_Alpha", .5d, 0d, 1d);

    public HoleESPModule() {
        super("HoleESP", "Highlights holes.", ModuleType.Render, 0);
        addOption(mode);
        addOption(radius);
        addOption(ignoreOwnHole);
        addOption(color_setting);
        addOption(O_Rainbow.setVisibility(v -> color_setting.getValue().contentEquals("OBSIDIAN")));
        addOption(O_Red.setVisibility(v -> color_setting.getValue().contentEquals("OBSIDIAN") && !O_Rainbow.isEnabled()));
        addOption(O_Green.setVisibility(v -> color_setting.getValue().contentEquals("OBSIDIAN") && !O_Rainbow.isEnabled()));
        addOption(O_Blue.setVisibility(v -> color_setting.getValue().contentEquals("OBSIDIAN") && !O_Rainbow.isEnabled()));
        addOption(O_Alpha.setVisibility(v -> color_setting.getValue().contentEquals("OBSIDIAN") && !O_Rainbow.isEnabled()));
        addOption(B_Rainbow.setVisibility(v -> color_setting.getValue().contentEquals("BEDROCK")));
        addOption(B_Red.setVisibility(v -> color_setting.getValue().contentEquals("BEDROCK") && !B_Rainbow.isEnabled()));
        addOption(B_Green.setVisibility(v -> color_setting.getValue().contentEquals("BEDROCK") && !B_Rainbow.isEnabled()));
        addOption(B_Blue.setVisibility(v -> color_setting.getValue().contentEquals("BEDROCK") && !B_Rainbow.isEnabled()));
        addOption(B_Alpha.setVisibility(v -> color_setting.getValue().contentEquals("BEDROCK") && !B_Rainbow.isEnabled()));
        endOption();
    }
}