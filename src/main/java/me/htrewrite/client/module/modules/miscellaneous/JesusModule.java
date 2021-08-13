package me.htrewrite.client.module.modules.miscellaneous;

import me.htrewrite.client.clickgui.components.buttons.settings.bettermode.BetterMode;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.exeterimports.mcapi.settings.ModeSetting;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;

public class JesusModule extends Module {
    public static final ModeSetting mode = new ModeSetting("Mode", 0, BetterMode.construct("Vanilla", "NCP", "Trampoline"));
    public static final ValueSetting<Double> trampolineHeight = new ValueSetting<>("JumpHeight", 1.18d, 0d, 50d);

    public static final ValueSetting<Double> offset = new ValueSetting<>("Offset", .18d, 0d, .9d);

    public JesusModule() {
        super("Jesus", "Walk on water.", ModuleType.Miscellaneous, 0);
        endOption();
    }
}