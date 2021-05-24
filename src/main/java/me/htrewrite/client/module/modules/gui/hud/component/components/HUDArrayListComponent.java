package me.htrewrite.client.module.modules.gui.hud.component.components;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.modules.gui.hud.HUDModule;
import me.htrewrite.client.module.modules.gui.hud.component.HUDComponent;
import me.htrewrite.client.util.ChatColor;
import me.htrewrite.client.util.ColorUtils;
import me.htrewrite.client.util.RenderUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;

public class HUDArrayListComponent implements HUDComponent {
    @Override
    public void render(RenderGameOverlayEvent.Text event, int x, int y) {
        int colorRect = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F);
        int colorRect2 = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F);

        int yPos = (int)((y/1.5)*getScale());
        int xPos = x;
        for(Module module : HTRewrite.INSTANCE.getModuleManager().getModules()) {
            if(!module.isEnabled()) continue;
            RenderUtils.drawStringWithRect(
                    module.getName() + ((module.getMeta().contentEquals("") ? "" : (" " + ChatColor.parse('&', "&7" + module.getMeta())))),
                    xPos,
                    yPos,
                    Color.YELLOW.getRGB(),
                    colorRect,
                    colorRect2);
            yPos += 12;
        }
    }

    private double getScale() { return HUDModule.watermarkScale.getValue(); }
}