package me.htrewrite.client.module.modules.render;

import me.htrewrite.client.event.custom.player.PlayerUpdateEvent;
import me.htrewrite.client.event.custom.render.RenderEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.*;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFurnace;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class ChestESPModule extends Module {
    public static final ValueSetting<Double> delay = new ValueSetting<>("TickDelay", null, 1d, 0d, 20d);
    public static final ToggleableSetting renderChest = new ToggleableSetting("Chest", null, true);
    public static final ToggleableSetting renderShulkerBox = new ToggleableSetting("ShulkerBox", null, true);
    public static final ToggleableSetting renderHopper = new ToggleableSetting("Hopper", null, true);
    public static final ToggleableSetting renderFurnace = new ToggleableSetting("Furnace", null, true);

    private ArrayList<Entry<BlockPos, SColor>> storages;
    private ICamera camera;
    private TickedTimer tickedTimer;

    public ChestESPModule() {
        super("ChestESP", "Highlight storages.", ModuleType.Render, 0);
        addOption(delay);
        addOption(renderChest);
        addOption(renderShulkerBox);
        addOption(renderHopper);
        addOption(renderFurnace);
        endOption();

        this.storages = new ArrayList<>();
        this.camera = new Frustum();

        this.tickedTimer = new TickedTimer();
        this.tickedTimer.stop();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        tickedTimer.start();
        tickedTimer.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        tickedTimer.stop();
    }

    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {
        if(!tickedTimer.passed(delay.getValue().intValue()))
            return;

        storages.clear();
        for(TileEntity entity : mc.world.loadedTileEntityList)
            if(entity instanceof TileEntityEnderChest && renderChest.isEnabled())
                storages.add(new Entry<>(entity.getPos(), SColor.SHULKERBOX));
            else if(entity instanceof TileEntityChest && renderChest.isEnabled())
                storages.add(new Entry<>(entity.getPos(), SColor.CHEST));
            else if(entity instanceof TileEntityShulkerBox && renderShulkerBox.isEnabled())
                storages.add(new Entry<>(entity.getPos(), SColor.SHULKERBOX));
            else if(entity instanceof TileEntityHopper && renderHopper.isEnabled())
                storages.add(new Entry<>(entity.getPos(), SColor.CHEST));
            else if(entity instanceof TileEntityFurnace && renderFurnace.isEnabled())
                storages.add(new Entry<>(entity.getPos(), SColor.CHEST));
        for(Entity entity : mc.world.loadedEntityList)
            if(entity instanceof EntityMinecartChest && renderChest.isEnabled())
                storages.add(new Entry<>(entity.getPosition(), SColor.CHEST));
            else if(entity instanceof EntityMinecartHopper && renderHopper.isEnabled())
                storages.add(new Entry<>(entity.getPosition(), SColor.CHEST));
            else if(entity instanceof EntityMinecartFurnace && renderFurnace.isEnabled())
                storages.add(new Entry<>(entity.getPosition(), SColor.CHEST));
        for(BlockPos blockPos : BlockUtil.nearbyBlocks(mc.player, 80)) {
            Block block = mc.world.getBlockState(blockPos).getBlock();
            if(block instanceof BlockEnderChest && renderChest.isEnabled())
                storages.add(new Entry<>(blockPos, SColor.CHEST));
            else if(block instanceof BlockFurnace)
                storages.add(new Entry<>(blockPos, SColor.SHULKERBOX));
        }

        tickedTimer.reset();
    });

    @EventHandler
    private Listener<RenderEvent> renderEventListener = new Listener<>(event -> {
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(1);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);

        for(Entry<BlockPos, SColor> entry : storages) {
            SColor sColor = entry.getValue();

            double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double)event.getPartialTicks();
            double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double)event.getPartialTicks();
            double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double)event.getPartialTicks();
            GLUtils.glColor(new Color(sColor.red, sColor.green, sColor.blue, sColor.alpha));
            RenderUtils.drawSelectionBoundingBox(mc.world.getBlockState(entry.getKey()).getSelectedBoundingBox(mc.world, entry.getKey()).grow(0.0020000000949949026D).offset(-x, -y, -z));
        }

        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glPopMatrix();
    });

    public enum SColor {
        CHEST(.94f, 1f, 0f, .6f),
        SHULKERBOX(1f, 0f, .59f, .6f);

        public final float red, green, blue, alpha;
        SColor(float red, float green, float blue, float alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
    }
}