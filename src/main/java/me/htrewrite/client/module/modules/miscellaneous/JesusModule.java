package me.htrewrite.client.module.modules.miscellaneous;

import me.htrewrite.client.event.custom.CustomEvent;
import me.htrewrite.client.event.custom.networkmanager.NetworkPacketEvent;
import me.htrewrite.client.event.custom.player.PlayerUpdateEvent;
import me.htrewrite.client.event.custom.render.CollisionBoxAddEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class JesusModule extends Module {
    public JesusModule() {
        super("Jesus", "Walk on water.", ModuleType.Miscellaneous, 0);
        endOption();
    }

    private boolean isAboveLand(Entity entity){
        if(entity == null)
            return false;
        double y = entity.posY - 0.01;

        for(int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++)
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
                BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);

                if (mc.world.getBlockState(pos).getBlock().isFullBlock(mc.world.getBlockState(pos)))
                    return true;
            }
        return false;
    }
    private boolean isAboveWater(Entity entity){
        double y = entity.posY - 0.03;
        for(int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++)
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
                BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)
                    return true;
            }
        return false;
    }
    private boolean isInWater(Entity entity) {
        if(entity == null)
            return false;
        double y = entity.posY + 0.01;

        for(int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++)
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
                BlockPos pos = new BlockPos(x, (int) y, z);
                if (mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)
                    return true;
            }
        return false;
    }

    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {
        if(isInWater(mc.player) && !mc.player.isSneaking()) {
            mc.player.motionY = .1;
            if(mc.player.getRidingEntity() != null)
                mc.player.getRidingEntity().motionY = .2;
        }
    });

    @EventHandler
    private Listener<NetworkPacketEvent> packetEventListener = new Listener<>(event -> {
        if(!(!event.reading && event.getEra() == CustomEvent.Era.PRE && event.getPacket() instanceof CPacketPlayer))
            return;
        if(isAboveWater(mc.player) && !isInWater(mc.player) && !isAboveLand(mc.player))
            if((mc.player.ticksExisted % 2) == 0)
                ((CPacketPlayer)event.getPacket()).y += .02;
    });

    @EventHandler
    private Listener<CollisionBoxAddEvent> collisionBoxAddEventListener = new Listener<>(event -> {
        if((mc.player == null || !(event.block instanceof BlockLiquid)) || (isInWater(mc.player) || mc.player.isSneaking() || mc.player.fallDistance > 3f) || (mc.player.getRidingEntity() != null))
            return;
        event.box = new AxisAlignedBB(0, 0, 0, 1, .99, 1);
    });
}