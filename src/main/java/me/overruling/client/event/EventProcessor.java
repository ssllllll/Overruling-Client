package me.overruling.client.event;

import static me.overruling.client.Overruling.EVENT_BUS;
import static me.overruling.client.command.CommandReturnStatus.*;

import me.overruling.client.Overruling;
import me.overruling.client.Wrapper;
import me.overruling.client.command.CommandManager;
import me.overruling.client.command.CommandReturnStatus;
import me.overruling.client.event.custom.player.PlayerDisconnectEvent;
import me.overruling.client.event.custom.world.BlockPlaceEvent;
import me.overruling.client.module.Module;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

public class EventProcessor {
    @SubscribeEvent
    public void onInput(InputEvent.KeyInputEvent inputEvent) {
        boolean eventKeyState = Keyboard.getEventKeyState();
        int eventKey = Keyboard.getEventKey();
        if(!eventKeyState) return;

        if (eventKey != Keyboard.KEY_NONE && Keyboard.getEventKeyState())
            for(Module module : Overruling.INSTANCE.getModuleManager().getModules())
                if(module.getKey() == eventKey)
                    module.toggle();
        EVENT_BUS.post(inputEvent);
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        EVENT_BUS.post(event);

        CommandReturnStatus returnStatus = Overruling.INSTANCE.getCommandManager().gotMessage(event.getMessage());
        if(returnStatus != COMMAND_INVALID_SYNTAX) event.setCanceled(true);
        if(returnStatus == COMMAND_INVALID) Wrapper.getPlayer().sendMessage(new TextComponentString("\u00a7cInvalid command, do " + CommandManager.prefix + "help"));
        if(returnStatus == COMMAND_ERROR) Wrapper.getPlayer().sendMessage(new TextComponentString("\u00a7cThere was an error executing the command."));
    }

    @SubscribeEvent public void onQuitClientServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) { EVENT_BUS.post(event); EVENT_BUS.post(new PlayerDisconnectEvent()); }
    @SubscribeEvent public void onQuitServerClient(FMLNetworkEvent.ServerDisconnectionFromClientEvent event) { EVENT_BUS.post(event); EVENT_BUS.post(new PlayerDisconnectEvent()); }

    @SubscribeEvent public void onClientTickEvent(TickEvent.ClientTickEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onRenderGameOverlayTextEvent(RenderGameOverlayEvent.Text event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onRenderWorldLast(RenderWorldLastEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onPlace(BlockEvent.PlaceEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onPlayerTick(TickEvent.PlayerTickEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) { EVENT_BUS.post(event); }
}