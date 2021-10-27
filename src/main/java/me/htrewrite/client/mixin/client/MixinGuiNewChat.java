package me.htrewrite.client.mixin.client;

import me.htrewrite.client.module.modules.gui.NotificationsModule;
import me.htrewrite.client.util.RainbowUtil;
import me.htrewrite.client.util.font.CFonts;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat extends Gui {

    private final Minecraft mc = Minecraft.getMinecraft();

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    int drawStringWithShadow(FontRenderer fontRenderer, String message, float x, float y, int color) {
        if (!NotificationsModule.INSTANCE.isEnabled()) return mc.fontRenderer.drawStringWithShadow(message, x, y, color);

        if (NotificationsModule.rainbowNotifications.getValue() == "GRADIENT") {

            CFonts.roboto32.drawRainbowGradientString(message, x, y, 150.0f, false);

        } else if (NotificationsModule.rainbowNotifications.getValue() == "NORMAL") {

            this.mc.fontRenderer.drawString(message, (int) x, (int) y, RainbowUtil.getRainbow(0).hashCode());

        } else if (NotificationsModule.rainbowNotifications.getValue() == "NONE") {

            mc.fontRenderer.drawStringWithShadow(message, x, y, color);

        }
      
        return 0;
    }

}

