package me.htrewrite.client.module.modules.render;

import me.htrewrite.client.clickgui.components.buttons.settings.bettermode.BetterMode;
import me.htrewrite.client.event.custom.render.RenderEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.shader.FramebufferShader;
import me.htrewrite.client.util.shader.shaders.AquaShader;
import me.htrewrite.client.util.shader.shaders.FlowShader;
import me.htrewrite.client.util.shader.shaders.RedShader;
import me.htrewrite.client.util.shader.shaders.SmokeShader;
import me.htrewrite.exeterimports.mcapi.settings.ModeSetting;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;


public class ShaderESPModule extends Module
{

    public static final ModeSetting mode = new ModeSetting("Mode", null, 0, BetterMode.construct("FLOW", "RED", "AQUA", "SMOKE"));
    public static final ToggleableSetting players = new ToggleableSetting("Crystals", null, true);
    public static final ToggleableSetting crystals = new ToggleableSetting("Players", null, true);

    public ShaderESPModule() {
        super("Notifications", "Notifications", ModuleType.Gui, 0);
        addOption(mode);
        addOption(players);
        addOption(crystals);
        endOption();
    }

    @EventHandler
    private Listener<RenderEvent> renderEvent = new Listener<>(event -> {
        if (nullCheck()) return;
        if (!crystals.isEnabled() || !players.isEnabled()) return;
        FramebufferShader framebufferShader = null;
        switch (mode.getValue()) {
            case "FLOW":
                framebufferShader = FlowShader.FLOW_SHADER;
                break;
            case "RED":
                framebufferShader = RedShader.RED_SHADER;
                break;
            case "AQUA":
                framebufferShader = AquaShader.AQUA_SHADER;
                break;
            case "SMOKE":
                framebufferShader = SmokeShader.SMOKE_SHADER;
                break;
        }
        if (framebufferShader == null) {
            return;
        }
        FramebufferShader shader = framebufferShader;
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        shader.startDraw(event.getPartialTicks());
        try {
            if (players.isEnabled()) {
                for (EntityPlayer entity : mc.world.playerEntities) {
                    if (entity != mc.player) {

                        final Render getEntityRenderObject = mc.getRenderManager().getEntityRenderObject(entity);

                        if (getEntityRenderObject == null) {
                            continue;
                        }

                        final Render entityRenderObject = getEntityRenderObject;
                        final Vec3d vector = interpolateEntity(entity, event.getPartialTicks()).subtract(mc.getRenderManager().viewerPosX, mc.getRenderManager().viewerPosY, mc.getRenderManager().viewerPosZ);
                        entityRenderObject.doRender(entity, vector.x, vector.y, vector.z, entity.rotationYaw, event.getPartialTicks());
                    }
                }
            }
            if (crystals.isEnabled()) {
                for (Entity entity : mc.world.getLoadedEntityList()) {
                    if (entity instanceof EntityEnderCrystal) {

                        final Render getEntityRenderObject = mc.getRenderManager().getEntityRenderObject(entity);

                        if (getEntityRenderObject == null) {
                            continue;
                        }

                        final Render entityRenderObject = getEntityRenderObject;
                        final Vec3d vector = interpolateEntity(entity, event.getPartialTicks()).subtract(mc.getRenderManager().viewerPosX, mc.getRenderManager().viewerPosY, mc.getRenderManager().viewerPosZ);
                        entityRenderObject.doRender(entity, vector.x, vector.y, vector.z, entity.rotationYaw, event.getPartialTicks());
                    }
                }
            }
        }
        catch (Exception ex) {}
        float radius = 3.5f;
        float red = 255f;
        float green = 0f;
        float blue = 255f;
        float alpha = 255f;
        shader.stopDraw(red, green, blue, alpha, radius, 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();

    });

    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }

}

