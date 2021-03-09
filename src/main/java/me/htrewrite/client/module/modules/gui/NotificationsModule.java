package me.htrewrite.client.module.modules.gui;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.event.custom.module.ModuleToggleEvent;
import me.htrewrite.client.event.custom.networkmanager.NetworkPacketEvent;
import me.htrewrite.client.event.custom.player.PlayerUpdateEvent;
import me.htrewrite.client.manager.FriendManager;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.ChatColor;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.text.TextComponentString;
import scala.reflect.internal.Trees;

import java.util.HashMap;

public class NotificationsModule extends Module {
    private FriendManager friendManager;

    public ToggleableSetting toggleNotifications = new ToggleableSetting("ModuleToggle", null, true);
    public ToggleableSetting totemNotifications = new ToggleableSetting("TotemPop",null,false);
    public static final HashMap<String, Integer> totem_pop_counter = new HashMap<String, Integer>();

    public NotificationsModule() {
        super("Notifications", "Notifications", ModuleType.Gui, 0);
        addOption(toggleNotifications);
        addOption(totemNotifications);
        endOption();
        this.friendManager = HTRewrite.INSTANCE.getFriendManager();
    }

    @EventHandler
    private Listener<ModuleToggleEvent> moduleToggleEventListener = new Listener<>(event -> {
        if(!toggleNotifications.isEnabled() || mc.player == null || mc.world == null)
            return;

        mc.player.sendMessage(new TextComponentString(ChatColor.prefix_parse('&', event.toggled ?
                "&a&l" + event.module.getName() + " &ais now ON!":
                "&c&l" + event.module.getName() + " &cis now OFF!")));
    });


    @EventHandler
    private Listener<NetworkPacketEvent> packetEventListener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packetEntityStatus = (SPacketEntityStatus) event.getPacket();
            if (packetEntityStatus.getOpCode()==35){
                Entity entity = packetEntityStatus.getEntity(mc.world);
                int count = 1;
                if (totem_pop_counter.containsKey(entity.getName())) {
                    count = totem_pop_counter.get(entity.getName());
                    totem_pop_counter.put(entity.getName(), ++count);
                } else {
                    totem_pop_counter.put(entity.getName(), count);
                }

                if (entity == mc.player) return;
                sendMessage("&b"+entity.getName()+" has poped "+count+" totems!");
            }
        }
    });

    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {
        for (EntityPlayer player : mc.world.playerEntities){
            if (!totem_pop_counter.containsKey(player)) continue;
            if (player.isDead||player.getHealth()<=0){
                int count=totem_pop_counter.get(player.getName());
                totem_pop_counter.remove(player.getName());
                if (player== mc.player) continue;
                sendMessage("&c"+player.getName()+" died after poping "+count+" totems!");
            }
        }

    });
}