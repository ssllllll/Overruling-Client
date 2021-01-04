package me.overruling.client.module.modules.gui;

import me.overruling.client.Overruling;
import me.overruling.client.Wrapper;
import me.overruling.client.clickgui.ClickGuiScreen;
import me.overruling.client.module.Module;
import me.overruling.client.module.ModuleType;
import me.overruling.exeterimports.mcapi.settings.ValueSetting;
import org.lwjgl.input.Keyboard;

public class ClickGuiModule extends Module {
    public ClickGuiModule() {
        super("ClickGUI", "Opens a gui.", ModuleType.Gui, Keyboard.KEY_P);
        endOption();
    }

    @Override
    public void onEnable() {
        if(Wrapper.getPlayer() != null && Wrapper.getMC().world != null && !(Wrapper.getMC().currentScreen instanceof ClickGuiScreen))
            mc.displayGuiScreen(Overruling.INSTANCE.getClickGuiScreen());
        toggle();
    }
}