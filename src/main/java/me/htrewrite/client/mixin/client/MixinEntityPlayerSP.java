package me.htrewrite.client.mixin.client;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.event.custom.CustomEvent;
import me.htrewrite.client.event.custom.player.PlayerChatEvent;
import me.htrewrite.client.event.custom.player.PlayerMotionUpdateEvent;
import me.htrewrite.client.event.custom.player.PlayerUpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {
    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo callbackInfo) {
        PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
        HTRewrite.EVENT_BUS.post(playerUpdateEvent);
        if(playerUpdateEvent.isCancelled())
            callbackInfo.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onUpdateWalkingPlayer(CallbackInfo callbackInfo) {
        PlayerMotionUpdateEvent playerMotionUpdateEvent = new PlayerMotionUpdateEvent(CustomEvent.Era.PRE);
        HTRewrite.EVENT_BUS.post(playerMotionUpdateEvent);
        if(playerMotionUpdateEvent.isCancelled())
            callbackInfo.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void onUpdateWalkingPlayerRet(CallbackInfo callbackInfo) {
        PlayerMotionUpdateEvent playerMotionUpdateEvent = new PlayerMotionUpdateEvent(CustomEvent.Era.POST);
        HTRewrite.EVENT_BUS.post(playerMotionUpdateEvent);
        if(playerMotionUpdateEvent.isCancelled())
            callbackInfo.cancel();
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void onMessageEvent(String message, CallbackInfo callbackInfo) {
        PlayerChatEvent playerChatEvent = new PlayerChatEvent(message);
        HTRewrite.EVENT_BUS.post(playerChatEvent);
        if(playerChatEvent.isCancelled())
            callbackInfo.cancel();
    }
}