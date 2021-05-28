package me.htrewrite.client.module.modules.combat;

import me.htrewrite.client.clickgui.components.buttons.settings.bettermode.BetterMode;
import me.htrewrite.client.event.custom.player.PlayerMotionUpdateEvent;
import me.htrewrite.client.event.custom.player.PlayerUpdateEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.MathUtil;
import me.htrewrite.client.util.TickedTimer;
import me.htrewrite.exeterimports.mcapi.settings.ModeSetting;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;

public class AutoAimModule extends Module {
    public static final ValueSetting<Double> range = new ValueSetting<>("Range", null, 30d, 0d, 50d);

    private Entity target;
    private float[] rotation = new float[2];
    public AutoAimModule() {
        super("AutoAim", "Aims automatically at players. (colisseum.net)", ModuleType.Combat, 0);
        addOption(range);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        rotation[0] = mc.player.rotationYaw;
        rotation[1] = mc.player.rotationPitch;
    }

    private boolean isValidEntity(Entity entity) {
        return (entity instanceof EntityPlayer) && mc.player != entity && mc.player.getDistance(entity)<= range.getValue().intValue();
    }

    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {
        target = mc.world.loadedEntityList.stream()
                .filter(loadedEntity -> isValidEntity(loadedEntity))
                .min(Comparator.comparing(loadedEntity -> mc.player.getDistance(loadedEntity.getPosition().getX(), loadedEntity.getPosition().getY(), loadedEntity.getPosition().getZ())))
                .orElse(null);
        if(target == null)
            return;

        rotation = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), target.getPositionEyes(mc.getRenderPartialTicks()));
    });

    @EventHandler
    private Listener<PlayerMotionUpdateEvent> motionUpdateEventListener = new Listener<>(event -> {
        if(target == null)
            return;
        mc.player.rotationYawHead = rotation[0];
        mc.player.rotationYaw = rotation[0];
        mc.player.rotationPitch = rotation[1];
    });
}