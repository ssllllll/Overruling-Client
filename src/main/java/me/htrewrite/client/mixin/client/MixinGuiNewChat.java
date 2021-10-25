package me.htrewrite.client.mixin.client;

import me.htrewrite.client.module.modules.gui.NotificationsModule;
import me.htrewrite.client.util.RainbowUtil;
import me.htrewrite.client.util.font.CFontRenderer;
import me.htrewrite.client.util.font.CFonts;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {
    // To finish -DriftyDev
    @Shadow
    @Final
    public List<ChatLine> drawnChatLines;

    private final Minecraft mc = Minecraft.getMinecraft();

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    int drawStringWithShadow(FontRenderer fontRenderer, String message, float x, float y, int color) {
        if (!NotificationsModule.INSTANCE.isEnabled()) return mc.fontRenderer.drawStringWithShadow(message, x, y, color);
        if (NotificationsModule.rainbowNotifications.getValue() == "GRADIENT") {
            //CFonts.roboto18.drawStringWithShadow(message, x, y, RainbowUtil.getRainbow(0).getRGB(), 150.0f, false);
            message.split("HT");
        } else if (NotificationsModule.rainbowNotifications.getValue() == "NORMAL"){
            CFonts.roboto18.drawString(message, x, y, RainbowUtil.getRainbow(0).hashCode());
        } else if ((NotificationsModule.rainbowNotifications.getValue() == "NONE")) {
            fontRenderer.drawStringWithShadow(message, x, y, color);
        }
      
        return 0;
    }

}

