package me.overruling.client.clickgui.components.buttons;

import me.overruling.client.Wrapper;
import me.overruling.client.clickgui.components.Colors;
import me.overruling.client.clickgui.components.Component;
import me.overruling.client.clickgui.components.buttons.settings.ModeComponent;
import me.overruling.client.clickgui.components.buttons.settings.StringComponent;
import me.overruling.client.clickgui.components.buttons.settings.ToggleableComponent;
import me.overruling.client.clickgui.components.buttons.settings.ValueComponent;
import me.overruling.client.module.Module;
import me.overruling.exeterimports.mcapi.interfaces.Toggleable;
import me.overruling.exeterimports.mcapi.settings.*;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModComponent extends Component {
    private final Module mod;
    private boolean open = false;
    private final List<Component> components = new CopyOnWriteArrayList<>();

    public ModComponent(Module mod, String label, int positionX, int positionY, int width, int height) {
        super(label, positionX, positionY, width, height);
        this.mod = mod;
        for (Setting setting : mod.getOptions()) {
            if (setting instanceof ModeSetting) {
                components.add(new ModeComponent((ModeSetting) setting, setting.getLabel(), positionX,
                        positionY, width, height));
            } else if (setting instanceof StringSetting) {
                components.add(new StringComponent((StringSetting) setting, setting.getLabel(), positionX,
                        positionY, width, height));
            } else if (setting instanceof ValueSetting) {
                components.add(new ValueComponent((ValueSetting) setting, setting.getLabel(), positionX,
                        positionY, width, height));
            } else if (setting instanceof ToggleableSetting) {
                components.add(new ToggleableComponent((ToggleableSetting) setting, setting.getLabel(), positionX,
                        positionY, width, height));
            }
        }
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovering(mouseX, mouseY, 20)) {
            switch (mouseButton) {
                case 0:
                    mod.toggle();
                    break;
                case 1:
                    this.open = !open;
                    break;
            }
        }
        components.forEach(component -> component.onClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        drawBorderedRectReliant(getPositionX() + 1, getPositionY() + 1, getPositionX() + getWidth() - 1,
                getPositionY() + 19, 1.7F, mod.isEnabled() ?
                        Colors.BUTTON_ENABLED.getColor() : Colors.BUTTON.getColor(),
                Colors.PANEL_BORDER.getColor());
        font.drawString(getLabel(), getPositionX() + 4, getPositionY() + 1, mod.isEnabled() ?
                Colors.BUTTON_LABEL_ENABLED.getColor() : Colors.BUTTON_LABEL.getColor());
        if (components.size() > 0) {
            font.drawString("...", getPositionX() + getWidth() - 13, getPositionY() - 1, Colors.BUTTON_LABEL.getColor());
        }
        setHeight(!open ? 20 : 20 + components.size() * 20);
        if (open) {
            int positionY = getPositionY() + 20;
            for (Component component : components) {
                component.drawComponent(mouseX, mouseY);
                component.setPositionX(getPositionX());
                component.setPositionY(positionY);
                positionY += 20;
            }
        }
    }

    public List<Component> getComponents() {
        return components;
    }

    public Module getMod() {
        return mod;
    }
}