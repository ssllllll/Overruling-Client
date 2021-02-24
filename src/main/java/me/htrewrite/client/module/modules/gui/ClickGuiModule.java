package me.htrewrite.client.module.modules.gui;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.Wrapper;
import me.htrewrite.client.clickgui.ClickGuiScreen;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import org.lwjgl.input.Keyboard;

public class ClickGuiModule extends Module {
    public ClickGuiModule() {
        super("ClickGUI", "Opens a gui.", ModuleType.Gui, Keyboard.KEY_P);
        endOption();
    }

    @Override
    public void onEnable() {
        if(Wrapper.getPlayer() != null && Wrapper.getMC().world != null && !(Wrapper.getMC().currentScreen instanceof ClickGuiScreen))
            mc.displayGuiScreen(HTRewrite.INSTANCE.getClickGuiScreen());
        toggle();
    }
}