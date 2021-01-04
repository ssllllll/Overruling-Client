package me.overruling.client.clickgui;

import me.overruling.client.Overruling;
import me.overruling.client.clickgui.components.panels.PanelComponent;
import me.overruling.exeterimports.keybind.Keybind;
import me.overruling.exeterimports.mcapi.manager.SetManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public final class GuiManager extends SetManager<PanelComponent> {
    private final ClickGuiScreen clickGuiScreen;

    public GuiManager() {
        clickGuiScreen = new ClickGuiScreen();

        Overruling.INSTANCE.getKeybindManager().register(new Keybind("click_gui_keybind", Keyboard.KEY_GRAVE) {
            @Override
            public void onPressed() {
                Minecraft.getMinecraft().displayGuiScreen(clickGuiScreen);
            }
        });

        /*new File(nivea, "gui_config.json") {
            @Override
            public void load() {//TODO this
                try {
                    if (!getConfig().exists()) {
                        getConfig().createNewFile();
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();;
                }
            }
            @Override
            public void save() {
                try {
                    if (!getConfig().exists()) {
                        getConfig().createNewFile();
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();;
                }
            }
        };*/
    }

    public ClickGuiScreen getClickGuiScreen() {
        return clickGuiScreen;
    }
}