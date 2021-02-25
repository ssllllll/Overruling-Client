package me.htrewrite.client.clickgui;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.clickgui.components.buttons.ModComponent;
import me.htrewrite.client.clickgui.components.panels.PanelComponent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ClickGuiScreen extends GuiScreen {
    private final List<PanelComponent> panels = new CopyOnWriteArrayList<>();

    public ClickGuiScreen() {
        int positionX = 30;
        for (ModuleType modType : ModuleType.values()) {
            panels.add(new PanelComponent(modType.name(), positionX, 20, 85, 20) {
                @Override
                public void registerComponents() {
                    for (Module mod : HTRewrite.INSTANCE.getModuleManager().getModules()) {
                        if (mod.getCategory().equals(modType)) {
                            getComponents().add(new ModComponent(mod, mod.getLabel(), getPositionX(), getPositionY(), 85, 20));
                        }
                    }
                }
            });
            positionX += 87;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        panels.forEach(panelComponent -> panelComponent.drawComponent(mouseX, mouseY));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_UP)
            StaticScrollOffset.offset-=10;
        if(keyCode == Keyboard.KEY_DOWN)
            StaticScrollOffset.offset+=10;

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        panels.forEach(panelComponent -> panelComponent.onClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        panels.forEach(PanelComponent::mouseReleased);
    }

    @Override
    public void onGuiClosed() {
        //client.fileManager.getConfig("gui_config.json").save();
        //client.fileManager.getConfig("overlay_config.json").save();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }



    public List<PanelComponent> getPanels() {
        return panels;
    }
}