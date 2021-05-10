package me.htrewrite.client.module.modules.gui;

import com.sun.javafx.geom.Vec2d;
import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.clickgui.components.buttons.settings.bettermode.BetterMode;
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

    public static final ModeSetting moduleEdit = new ModeSetting("SModule", null, 0, BetterMode.construct("Watermark", "Position", "FPS", "ArrayList"));
    /* WATERMARK */
    public static final ValueSetting<Double> watermarkX = new ValueSetting<>("WaterX", null, 4D, 0D, 1000D);
    public static final ValueSetting<Double> watermarkY = new ValueSetting<>("WaterY", null, 4D, 0D, 1000D);
    public static final ValueSetting<Double> watermarkScale = new ValueSetting<>("WaterSize", null, 1.5D, 0.5D, 3D);
    /* POSITION */
    public static final ValueSetting<Double> positionX = new ValueSetting<>("PosX", null, 4d, 0D, 1000D);
    public static final ValueSetting<Double> positionY = new ValueSetting<>("PosY", null, 0d, 0d, 1000d);
    /* FPS */
    public static final ValueSetting<Double> fpsX = new ValueSetting<>("FPSX", null, 4d, 0D, 1000D);
    public static final ValueSetting<Double> fpsY = new ValueSetting<>("FPSY", null, 0d, 0d, 1000d);
    /* ArrayList */
    public static final ValueSetting<Double> arrayX = new ValueSetting<>("ArrayX", null, 4d, 0d, 1000d);
    public static final ValueSetting<Double> arrayY = new ValueSetting<>("ArrayY", null, 24d, 0d, 1000d);

    public HUDModule() {
        super("HUD", "Interface", ModuleType.Gui, 0);
        addOption(setting);
        /* SETTINGS */
        addOption(watermark.setVisibility(a -> setting.getI() == 0));
        addOption(position.setVisibility(a -> setting.getI() == 0));
        addOption(fps.setVisibility(a -> setting.getI() == 0));
        addOption(arraylist.setVisibility(a -> setting.getI() == 0));
        /* EDIT */
        addOption(moduleEdit.setVisibility(v -> setting.getI()==1));
        // - Water - \\
        addOption(watermarkX.setVisibility(v -> setting.getI()==1&&moduleEdit.getI()==0));
        addOption(watermarkY.setVisibility(v -> setting.getI()==1&&moduleEdit.getI()==0));
        addOption(watermarkScale.setVisibility(v -> setting.getI()==1&&moduleEdit.getI()==0));
        // - Position - \\
        addOption(positionX.setVisibility(v -> setting.getI()==1&&moduleEdit.getI()==1));
        addOption(positionY.setVisibility(v -> setting.getI()==1&&moduleEdit.getI()==1));
        // - FPS - \\
        addOption(fpsX.setVisibility(v -> setting.getI()==1&&moduleEdit.getI()==2));
        addOption(fpsY.setVisibility(v -> setting.getI()==1&&moduleEdit.getI()==2));
        // - ArrayList - \\
        addOption(arrayX.setVisibility(v -> setting.getI()==1&&moduleEdit.getI()==3));
        addOption(arrayY.setVisibility(v -> setting.getI()==1&&moduleEdit.getI()==3));

        endOption();
    }

    @EventHandler
    private Listener<RenderGameOverlayEvent.Text> textListener = new Listener<>(event -> {
        if(watermark.isEnabled()) {
            GL11.glPushMatrix();
            GL11.glScalef(watermarkScale.getValue().floatValue(), watermarkScale.getValue().floatValue(), watermarkScale.getValue().floatValue());
            mc.fontRenderer.drawStringWithShadow(HTRewrite.NAME + " " + HTRewrite.VERSION, watermarkX.getValue().intValue(), watermarkY.getValue().intValue(), Color.WHITE.getRGB());
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

            int scaledHeight = sr.getScaledHeight() - positionY.getValue().intValue();
            int heightCoords = isChatOpen ? scaledHeight - 25 : scaledHeight - 10;

            RenderUtils.drawStringWithRect(coords, positionX.getValue().intValue(), heightCoords, Color.WHITE.getRGB(),
                    colorRect, colorRect2);
        }
        if(fps.isEnabled()) {
            int scaledHeight = sr.getScaledHeight() - fpsY.getValue().intValue();
            int heightFPS = isChatOpen ? scaledHeight - 37 : scaledHeight - 22;
            RenderUtils.drawStringWithRect("\u00a77FPS: \u00a7f" + mc.getDebugFPS(), fpsX.getValue().intValue(), heightFPS, Color.WHITE.getRGB(),
                    colorRect, colorRect2);
        }
        if(arraylist.isEnabled()) {
            int yPos = (int)((arrayY.getValue().intValue()/1.5)*watermarkScale.getValue());
            int xPos = arrayX.getValue().intValue();
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