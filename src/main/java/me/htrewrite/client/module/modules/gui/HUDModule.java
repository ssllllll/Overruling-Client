package me.htrewrite.client.module.modules.gui;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.clickgui.components.buttons.settings.bettermode.BetterMode;
import me.htrewrite.client.event.custom.event.RenderGetFOVModifierEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.ChatColor;
import me.htrewrite.client.util.ColorUtils;
import me.htrewrite.client.util.RenderUtils;
import me.htrewrite.exeterimports.mcapi.settings.ModeSetting;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.world.DimensionType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class HUDModule extends Module {
    public static final ModeSetting setting = new ModeSetting("Setting", null, 0, BetterMode.construct("Toggle", "Edit"));
    public static final ToggleableSetting watermark = new ToggleableSetting("Watermark", null, true);
    public static final ToggleableSetting position = new ToggleableSetting("Position", null, true);
    public static final ToggleableSetting fps = new ToggleableSetting("FPS", null, true);
    public static final ToggleableSetting arraylist = new ToggleableSetting("ArrayList", null, true);
    public static final ValueSetting<Double> watermarkScale = new ValueSetting<>("WaterSize", null, 1.5D, 0.5D, 3D);
    public static final ToggleableSetting customFovEnabled = new ToggleableSetting("CustomFovEnabled", null, false);
    public static final ValueSetting<Double> customFov = new ValueSetting<>("CustomFov", null, 0D, 170D, 10D);

    public HUDModule() {
        super("HUD", "Interface", ModuleType.Gui, 0);
        addOption(setting);
        /* SETTINGS */
        addOption(watermark.setVisibility(a -> setting.getI() == 0));
        addOption(position.setVisibility(a -> setting.getI() == 0));
        addOption(fps.setVisibility(a -> setting.getI() == 0));
        addOption(arraylist.setVisibility(a -> setting.getI() == 0));
        addOption(customFovEnabled.setVisibility(a -> setting.getI() == 0));
        /* EDIT */
        addOption(watermarkScale.setVisibility(a -> setting.getI() == 1));
        addOption(customFov.setVisibility(a -> setting.getI() == 1));

        endOption();
    }

    @EventHandler
    private Listener<RenderGetFOVModifierEvent> fovModifierEventListener = new Listener<>(event -> {
        if(!customFovEnabled.isEnabled())
            return;

        event.cancel();
        event.setFOV(customFov.getValue().floatValue());
    });

    @EventHandler
    private Listener<RenderGameOverlayEvent.Text> textListener = new Listener<>(event -> {
        if(watermark.isEnabled()) {
            GL11.glPushMatrix();
            GL11.glScalef(watermarkScale.getValue().floatValue(), watermarkScale.getValue().floatValue(), watermarkScale.getValue().floatValue());
            mc.fontRenderer.drawStringWithShadow(HTRewrite.NAME + " " + HTRewrite.VERSION, 4, 4, Color.WHITE.getRGB());
            GL11.glPopMatrix();
        }

        int colorRect = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F);
        int colorRect2 = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F);
        boolean isChatOpen = mc.currentScreen instanceof GuiChat;
        ScaledResolution sr = new ScaledResolution(mc);
        boolean nether = mc.world.provider.isNether();
        boolean end = mc.world.provider.getDimensionType() == DimensionType.THE_END;
        if(position.isEnabled()) {
            double x = mc.player.posX;
            double y = mc.player.posY;
            double z = mc.player.posZ;
            String coords = String.format("\u00a77X: \u00a7f%s \u00a77Y: \u00a7f%s \u00a77Z: \u00a7f%s", RenderUtils.DF((float)x, 1), RenderUtils.DF((float)y, 1), RenderUtils.DF((float)z, 1));
            if(nether) {
                double tX = x*8;
                double tZ = z*8;
                coords = String.format("\u00a77X: \u00a7f%s \u00a77Y: \u00a7f%s \u00a77Z: \u00a7f%s \u00a77(\u00a7f%s \u00a7f%s\u00a77)", RenderUtils.DF((float)x, 1), RenderUtils.DF((float)y, 1), RenderUtils.DF((float)z, 1), RenderUtils.DF((float)tX, 1), RenderUtils.DF((float)tZ, 1));
            } else if(!nether && !end) {
                double tX = x/8;
                double tZ = z/8;
                coords = String.format("\u00a77X: \u00a7f%s \u00a77Y: \u00a7f%s \u00a77Z: \u00a7f%s \u00a77(\u00a7f%s \u00a7f%s\u00a77)", RenderUtils.DF((float)x, 1), RenderUtils.DF((float)y, 1), RenderUtils.DF((float)z, 1), RenderUtils.DF((float)tX, 1), RenderUtils.DF((float)tZ, 1));
            }

            int heightCoords = isChatOpen ? sr.getScaledHeight() - 25 : sr.getScaledHeight() - 10;

            RenderUtils.drawStringWithRect(coords, 4, heightCoords, Color.WHITE.getRGB(),
                    colorRect, colorRect2);
        }
        if(fps.isEnabled()) {
            int heightFPS = isChatOpen ? sr.getScaledHeight() - 37 : sr.getScaledHeight() - 22;
            RenderUtils.drawStringWithRect("\u00a77FPS: \u00a7f" + mc.getDebugFPS(), 4, heightFPS, Color.WHITE.getRGB(),
                    colorRect, colorRect2);
        }
        if(arraylist.isEnabled()) {
            int yPos = (int)((24/1.5)*watermarkScale.getValue());
            int xPos = 4;
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
    });
}