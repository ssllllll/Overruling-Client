package me.htrewrite.client.module.modules.miscellaneous;

import com.mojang.authlib.GameProfile;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.GameType;

import java.util.UUID;

public class FakePlayerModule extends Module {

    public FakePlayerModule() {
            super("FakePlayer", "Fake player.", ModuleType.Miscellaneous, 0);
            endOption();
        }

    private EntityOtherPlayerMP clonedPlayer;

    public void onEnable() {
        if (mc.player == null || mc.player.isDead) {
            onDisable();
            return;
        }

        clonedPlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("fdee323e-7f0c-4c15-8d1c-0f277442342a"), "TheRealCG3"));
        clonedPlayer.copyLocationAndAnglesFrom(mc.player);
        clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
        clonedPlayer.rotationYaw = mc.player.rotationYaw;
        clonedPlayer.rotationPitch = mc.player.rotationPitch;
        clonedPlayer.setGameType(GameType.SURVIVAL);
        clonedPlayer.setHealth(20);
        mc.world.addEntityToWorld(-1234, clonedPlayer);
        clonedPlayer.onLivingUpdate();
    }

    public void onDisable() {
        if (mc.world != null)
            mc.world.removeEntityFromWorld(-1234);
    }
}