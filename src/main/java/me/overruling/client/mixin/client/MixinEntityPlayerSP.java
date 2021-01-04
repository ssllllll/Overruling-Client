package me.overruling.client.mixin.client;

import me.overruling.client.Overruling;
import me.overruling.client.event.custom.player.PlayerUpdateEvent;
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
        Overruling.EVENT_BUS.post(playerUpdateEvent);
        if(playerUpdateEvent.isCancelled())
            callbackInfo.cancel();
    }
}