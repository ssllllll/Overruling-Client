package me.overruling.client.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class MixinWorld {
    @Inject(method = "onEntityAdded", at = @At("HEAD"), cancellable = true)
    public void onEntityAdded(Entity entity, CallbackInfo callbackInfo) {

    }
}