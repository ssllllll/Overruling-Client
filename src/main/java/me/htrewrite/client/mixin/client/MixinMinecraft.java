package me.htrewrite.client.mixin.client;

import me.htrewrite.client.HTMinecraft;
import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.customgui.SplashProgressGui;
import me.htrewrite.client.event.custom.client.ClientShutdownEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.fml.client.FMLConfigGuiFactory;
import net.minecraftforge.fml.client.SplashProgress;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    private HTMinecraft htMinecraft = new HTMinecraft((Minecraft) (Object) this);
    private boolean alreadyCrashed = false;

    @Redirect(method={"run"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReportHook(Minecraft minecraft, CrashReport crashReport) {
        if(alreadyCrashed)
            return;

        System.out.println("\n\nDetected CrashReport! \n" + crashReport.getCompleteReport());
        HTRewrite.INSTANCE.saveEverything();
        HTRewrite.EVENT_BUS.post(new ClientShutdownEvent(ClientShutdownEvent.ShutdownType.CRASH));
    }

    @Inject(method={"shutdown"}, at={@At(value="HEAD")})
    public void shutdownHook(CallbackInfo info) {
        HTRewrite.INSTANCE.saveEverything();
        HTRewrite.EVENT_BUS.post(new ClientShutdownEvent(ClientShutdownEvent.ShutdownType.SHUTDOWN));
    }

    @Overwrite
    public void drawSplashScreen(TextureManager tm) {
        SplashProgressGui.drawSplash(tm);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void onStartGame(CallbackInfo ci) {
        htMinecraft.onStartGame();
        SplashProgress.pause();
    }

    @Inject(method = "init", at = @At(value = "INVOKE", remap = false, target = "java/util/List.add(Ljava/lang/Object;)Z", shift = At.Shift.BEFORE))
    private void onLoadDefaultResourcePack(CallbackInfo ci) {
        htMinecraft.onLoadDefaultResourcePack();
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "net/minecraft/client/Minecraft.createDisplay()V", shift = At.Shift.BEFORE))
    private void onCreateDisplay(CallbackInfo ci) {
        htMinecraft.onCreateDisplay();
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/OpenGlHelper.initializeTextures()V", shift = At.Shift.BEFORE))
    private void onLoadTexture(CallbackInfo ci) {
        htMinecraft.onLoadTexture();
    }
}