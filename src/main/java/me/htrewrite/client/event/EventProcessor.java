package me.htrewrite.client.event;

import static me.htrewrite.client.HTRewrite.EVENT_BUS;
import static me.htrewrite.client.command.CommandReturnStatus.*;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.Wrapper;
import me.htrewrite.client.command.CommandManager;
import me.htrewrite.client.command.CommandReturnStatus;
import me.htrewrite.client.event.custom.event.RenderGetFOVModifierEvent;
import me.htrewrite.client.event.custom.player.PlayerDisconnectEvent;
import me.htrewrite.client.event.custom.render.RenderEvent;
import me.htrewrite.client.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class EventProcessor {
    @SubscribeEvent
    public void onInput(InputEvent.KeyInputEvent inputEvent) {
        boolean eventKeyState = Keyboard.getEventKeyState();
        int eventKey = Keyboard.getEventKey();
        if(!eventKeyState) return;

        if (eventKey != Keyboard.KEY_NONE && Keyboard.getEventKeyState())
            for(Module module : HTRewrite.INSTANCE.getModuleManager().getModules())
                if(module.getKey() == eventKey)
                    module.toggle();
        EVENT_BUS.post(inputEvent);
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        EVENT_BUS.post(event);

        CommandReturnStatus returnStatus = HTRewrite.INSTANCE.getCommandManager().gotMessage(event.getMessage());
        if(returnStatus != COMMAND_INVALID_SYNTAX) event.setCanceled(true);
        if(returnStatus == COMMAND_INVALID) Wrapper.getPlayer().sendMessage(new TextComponentString("\u00a7cInvalid command, do " + CommandManager.prefix + "help"));
        if(returnStatus == COMMAND_ERROR) Wrapper.getPlayer().sendMessage(new TextComponentString("\u00a7cThere was an error executing the command."));
    }

    @SubscribeEvent public void onQuitClientServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) { EVENT_BUS.post(event); EVENT_BUS.post(new PlayerDisconnectEvent()); }
    @SubscribeEvent public void onQuitServerClient(FMLNetworkEvent.ServerDisconnectionFromClientEvent event) { EVENT_BUS.post(event); EVENT_BUS.post(new PlayerDisconnectEvent()); }

    @SubscribeEvent public void onClientTickEvent(TickEvent.ClientTickEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onRenderGameOverlayTextEvent(RenderGameOverlayEvent.Text event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (event.isCanceled())
            return;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        GlStateManager.glLineWidth(1f);
        HTRewrite.EVENT_BUS.post(new RenderEvent(event.getPartialTicks()));
        GlStateManager.glLineWidth(1f);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
    }
    @SubscribeEvent public void onPlace(BlockEvent.PlaceEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onPlayerTick(TickEvent.PlayerTickEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onInteractWithItem(PlayerInteractEvent.RightClickItem event) { EVENT_BUS.post(event); }


    @SubscribeEvent public void startInteract(LivingEntityUseItemEvent.Start event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void stopInteract(LivingEntityUseItemEvent.Stop event) { EVENT_BUS.post(event); }
    @SubscribeEvent
    public void getFOVModifier(EntityViewRenderEvent.FOVModifier event) {
        RenderGetFOVModifierEvent renderGetFOVModifierEvent = new RenderGetFOVModifierEvent((float)event.getRenderPartialTicks(), true);
        EVENT_BUS.post(renderGetFOVModifierEvent);
        if(renderGetFOVModifierEvent.isCancelled())
            renderGetFOVModifierEvent.setFOV(event.getFOV());
    }
}