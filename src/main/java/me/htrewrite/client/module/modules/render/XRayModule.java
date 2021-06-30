package me.htrewrite.client.module.modules.render;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.manager.XRayManager;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;

public class XRayModule extends Module {
    public static final ValueSetting<Double> distance = new ValueSetting<>("Distance", null, 50d, 5d, 100d);
    public static final ValueSetting<Double> delay = new ValueSetting<>("TickDelay", null, 2d, 0d, 20d);

    private XRayManager xRayManager;

    public XRayModule() {
        super("XRay", "See blocks through walls.", ModuleType.Render, 0);
        addOption(distance);
        addOption(delay);
        endOption();

        xRayManager = HTRewrite.INSTANCE.getxRayManager();
    }
}