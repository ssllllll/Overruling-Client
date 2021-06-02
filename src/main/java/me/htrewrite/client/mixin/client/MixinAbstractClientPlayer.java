package me.htrewrite.client.mixin.client;

import me.htrewrite.client.HTRewrite;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = {"getLocationCape"}, at = {@At("HEAD")}, cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        NetworkPlayerInfo info = getPlayerInfo();
        if(HTRewrite.INSTANCE.getCapes().wCapes.containsKey(info.getGameProfile().getName()))
            callbackInfoReturnable.setReturnValue(HTRewrite.INSTANCE.getCapes().wCapes.get(info.getGameProfile().getName()).resourceLocation);
    }
}