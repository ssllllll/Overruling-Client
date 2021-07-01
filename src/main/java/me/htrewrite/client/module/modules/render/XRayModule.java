package me.htrewrite.client.module.modules.render;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.manager.XRayManager;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.BlockUtil;
import me.htrewrite.client.util.GLUtils;
import me.htrewrite.client.util.RenderUtils;
import me.htrewrite.client.util.TickedTimer;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class XRayModule extends Module {
    public static final ValueSetting<Double> distance = new ValueSetting<>("Distance", null, 50d, 5d, 100d);
    public static final ValueSetting<Double> delay = new ValueSetting<>("TickDelay", null, 2d, 0d, 20d);
    public static final ValueSetting<Double> r = new ValueSetting<>("Red", null, 255d, 0d, 255d);
    public static final ValueSetting<Double> g = new ValueSetting<>("Green", null, 0d, 0d, 255d);
    public static final ValueSetting<Double> b = new ValueSetting<>("Blue", null, 0d, 0d, 255d);
    public static final ValueSetting<Double> a = new ValueSetting<>("Alpha", null, 0d, 0d, 255d);
    public static final ValueSetting<Double> width = new ValueSetting<>("Width", null, 1d, 1d, 10d);

    private XRayManager xRayManager;
    private List<BlockPos> blocksPos;
    private TickedTimer tickedTimer;

    public XRayModule() {
        super("XRay", "See blocks through walls.", ModuleType.Render, 0);
        addOption(distance);
        addOption(delay);
        endOption();

        xRayManager = HTRewrite.INSTANCE.getxRayManager();
        blocksPos = new ArrayList<>();

        tickedTimer = new TickedTimer();
        tickedTimer.stop();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        blocksPos.clear();
        tickedTimer.reset();
        tickedTimer.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        tickedTimer.stop();
    }

    @EventHandler
    private Listener<TickEvent.ClientTickEvent> tickEventListener = new Listener<>(event -> {
        if(!tickedTimer.passed(delay.getValue().intValue()))
            return;
        if(nullCheck()) {
            tickedTimer.reset();
            return;
        }

        int distance = XRayModule.distance.getValue().intValue();
        blocksPos.clear();
        for(BlockPos blockPos : BlockUtil.nearbyBlocks(mc.player, distance))
            if(xRayManager.isXRayBlock(mc.world.getBlockState(blockPos).getBlock()))
                blocksPos.add(blockPos);

        tickedTimer.reset();
    });

    @EventHandler
    private Listener<RenderWorldLastEvent> renderWorldLastEventListener = new Listener<>(event -> {
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(width.getValue().intValue());
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);

        for(BlockPos blockPos : blocksPos) {
            double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double)event.getPartialTicks();
            double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double)event.getPartialTicks();
            double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double)event.getPartialTicks();
            GLUtils.glColor(new Color(0, 255, 0, 255).getRGB());
            RenderUtils.drawSelectionBoundingBox(mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos).grow(0.0020000000949949026D).offset(-x, -y, -z));
        }

        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glPopMatrix();
    });
}