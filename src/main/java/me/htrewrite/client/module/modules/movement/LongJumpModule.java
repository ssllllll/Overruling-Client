package me.htrewrite.client.module.modules.movement;

import me.htrewrite.client.clickgui.components.buttons.settings.bettermode.BetterMode;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.exeterimports.mcapi.settings.ModeSetting;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;

public class LongJumpModule extends Module {
    public static final ModeSetting mode = new ModeSetting("Mode", 1, BetterMode.construct("VIRTUE", "DIRECT", "TICK"));
    public static final ValueSetting<Double> timeout = new ValueSetting<>("TimeOut", 2d, 0d, 5d);
    public static final ValueSetting<Double> boost = new ValueSetting<>("Boost", 4.48d, 1d, 20d);
    public static ToggleableSetting lagOff = new ToggleableSetting("LagOff", false);

    public LongJumpModule() {
        super("LongJump", "Jump longer.", ModuleType.Movement, 0);
        endOption();
    }
}