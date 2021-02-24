package me.htrewrite.client.module.modules.miscellaneous;

import me.htrewrite.client.clickgui.components.buttons.settings.bettermode.BetterMode;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.exeterimports.mcapi.settings.ModeSetting;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;

public class AutoHappyMealModule extends Module {
    public static final ModeSetting mode = new ModeSetting("Mode", null, 0, BetterMode.construct("MODE", "*arab*"));
    public static final ToggleableSetting setting = new ToggleableSetting("Setting", null, false);

    public AutoHappyMealModule() {
        super("AutoHappyMeal", "*as*", ModuleType.Miscellaneous, 0);
        addOption(mode);
        addOption(setting); setting.setVisibility(asd -> mode.getI() == 1);
        endOption();
    }
}