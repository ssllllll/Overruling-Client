package me.htrewrite.client.module.modules.world;

import me.htrewrite.client.event.custom.CustomEvent;
import me.htrewrite.client.event.custom.networkmanager.NetworkPacketEvent;
import me.htrewrite.client.event.custom.player.PlayerMotionUpdateEvent;
import me.htrewrite.client.event.custom.player.PlayerMoveEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.Timer;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;
import me.htrewrite.salimports.util.BlockInteractionHelper;
import me.htrewrite.salimports.util.PlayerUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ScaffoldModule extends Module {
    public static final ToggleableSetting noFall = new ToggleableSetting("NoFall", true);
    public static final ValueSetting<Double> delay = new ValueSetting<>("Delay", 0d, 0d, 1d);

    private Timer timer, tTimer, tPTimer;
    public ScaffoldModule() {
        super("Scaffold", "Places blocks.", ModuleType.World, 0);
        addOption(noFall);
        addOption(delay);
        endOption();

        timer = new Timer();
        tTimer = new Timer();
        tPTimer = new Timer();
    }

    private boolean isValidStack(ItemStack itemStack) { return !itemStack.isEmpty() && itemStack.getItem() instanceof ItemBlock;}
    private boolean checkPlace(BlockPos blockPos) {
        BlockInteractionHelper.ValidResult result = BlockInteractionHelper.valid(blockPos);
        if(result == BlockInteractionHelper.ValidResult.AlreadyBlockThere)
            return mc.world.getBlockState(blockPos).getMaterial().isReplaceable();
        return result == BlockInteractionHelper.ValidResult.Ok;
    }
    private boolean checkOffset(double x, double y, double z) { return mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(x, y, z)).isEmpty(); }

    @EventHandler
    private Listener<PlayerMotionUpdateEvent> motionUpdateEventListener = new Listener<>(event -> {
        if(event.isCancelled() || event.getEra() != CustomEvent.Era.PRE || !timer.passed(delay.getValue()*1000))
            return;

        int slot = -1;
        ItemStack itemStack = mc.player.getHeldItemMainhand();
        if(!isValidStack(itemStack)) {
            for(int i = 0; i < 9; ++i) {
                itemStack = mc.player.inventory.getStackInSlot(i);
                if(isValidStack(itemStack)) {
                    slot = mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = i;
                    mc.playerController.updateController();
                    break;
                }
            }
        } if(!isValidStack(itemStack))
            return;
        timer.reset();

        BlockPos placeBlockPos = null, feetBlockPos = PlayerUtil.GetLocalPlayerPosFloored().down();
        boolean shouldPlaceAtFeet = checkPlace(feetBlockPos);
        if(shouldPlaceAtFeet && mc.player.movementInput.jump && tTimer.passed(250) && !mc.player.isElytraFlying()) {
            if(tPTimer.passed(1500)) {
                tPTimer.reset();
                mc.player.motionY = -.28f;
            } else mc.player.setVelocity(0f, .41999998688f, 0f);
        } if(shouldPlaceAtFeet)
            placeBlockPos = feetBlockPos;
        else {
            BlockInteractionHelper.ValidResult validResult = BlockInteractionHelper.valid(feetBlockPos);
            if(validResult != BlockInteractionHelper.ValidResult.Ok && validResult != BlockInteractionHelper.ValidResult.AlreadyBlockThere) {
                BlockPos[] blockPos = {feetBlockPos.north(), feetBlockPos.south(), feetBlockPos.east(), feetBlockPos.west()};
                BlockPos selectedBlockPos = null;
                double distance = 420d;
                for(BlockPos pos : blockPos) {
                    if(!checkPlace(pos))
                        continue;

                    double dist = pos.getDistance((int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ);
                    if(distance > dist) {
                        distance = dist;
                        selectedBlockPos = pos;
                    }
                }
                if(blockPos != null)
                    placeBlockPos = selectedBlockPos;
            }
        }
        if(placeBlockPos != null) {
            final Vec3d eyeVec = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
            for(final EnumFacing enumFacing : EnumFacing.values()) {
                final BlockPos blockPos = placeBlockPos.offset(enumFacing);
                final EnumFacing facing = enumFacing.getOpposite();
                if(mc.world.getBlockState(blockPos).getBlock().canCollideCheck(mc.world.getBlockState(blockPos), false)) {
                    final Vec3d vec = new Vec3d(blockPos).add(.5, .5, .5).add(new Vec3d(facing.getDirectionVec()).scale(.5));
                    if(eyeVec.distanceTo(vec) <= 5f) {
                        float[] rotations = BlockInteractionHelper.getFacingRotations(placeBlockPos.getX(), placeBlockPos.getY(), placeBlockPos.getZ(), enumFacing);

                        event.cancel();
                        PlayerUtil.PacketFacePitchAndYaw(rotations[1], rotations[0]);
                        break;
                    }
                }
            } BlockInteractionHelper.place(placeBlockPos, 5f, false, false, true);
        } else tPTimer.reset();

        if(slot != -1) {
            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
        }
    });

    @EventHandler
    private Listener<NetworkPacketEvent> packetEventListener = new Listener<>(event -> {
        if(event.getEra() == CustomEvent.Era.PRE && event.getPacket() instanceof SPacketPlayerPosLook)
            tTimer.reset();
    });

    @EventHandler
    private Listener<PlayerMoveEvent> moveEventListener = new Listener<>(event -> {
        if(!noFall.isEnabled())
            return;

        double x = event.x;
        double y = event.y;
        double z = event.z;
        if(mc.player.onGround && !mc.player.noClip) {
            double increment;
            for(increment = .05d; x != 0d && checkOffset(x, -1d, 0d);)
                if(x < increment && x >= -increment)
                    x = 0d;
                else if(x > 0d)
                    x -= increment;
                else x += increment;
            for(; z != 0d && checkOffset(0d, -1d, z);)
                if(z < increment && z >= -increment)
                    z = 0d;
                else if(z > 0d)
                    z -= increment;
                else z += increment;
            for(; x != 0d && z != 0d && checkOffset(x, -1d, z);) {
                if(x < increment && x >= -increment)
                    x = 0d;
                else if(x > 0d)
                    x -= increment;
                else x += increment;

                if(z < increment && z >= -increment)
                    z = 0d;
                else if(z > 0d)
                    z -= increment;
                else z += increment;
            }
        }

        event.x = x;
        event.y = y;
        event.z = z;
        event.cancel();
    });
}