package me.htrewrite.client.module.modules.miscellaneous;

import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.exeterimports.mcapi.settings.IntegerSetting;

public class TabExpanderModule extends Module {
    public static final IntegerSetting size = new IntegerSetting("Size", 500d, 1d, 1000d);

    public static TabExpanderModule INSTANCE;
    public TabExpanderModule() {
        super("TabExpander", "Expands tab.", ModuleType.Miscellaneous, 0);
        addOption(size);
        endOption();

        INSTANCE = this;
    }
}