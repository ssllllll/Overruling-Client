package me.htrewrite.client.mixin.mixins;

import me.htrewrite.client.util.RainbowUtil;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value={GuiNewChat.class})
public class MixinGuiNewChat extends Gui {
    // To finish -DriftyDev
    @Shadow
    @Final
    public List<ChatLine> drawnChatLines;

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadow(FontRenderer fontRenderer, String message, float x, float y, int color) {
        if (!) return fontRenderer.drawStringWithShadow(message, x, y, color);
        if () {
            FontManager.drawStringGradientRainbow(message, x, y, Rainbow.getRainbow(0).getRGB(), 150.0f, false);
        } else {
            FontManager.drawStringWithShadow(message, x, y, color);
        }
        fontRenderer.drawStringWithShadow(message, x, y, color);
      
        return 0;
    }

}

