package me.htrewrite.client.module.modules.miscellaneous;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.event.custom.player.PlayerUpdateEvent;
import me.htrewrite.client.manager.FriendManager;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.ChatColor;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Mouse;

public class MiddleClickModule extends Module {
    FriendManager friendManager;

    public MiddleClickModule() {
        super("MiddleClickF", "MiddleClickFriends stuff.", ModuleType.Miscellaneous, 0);
        endOption();

        friendManager = HTRewrite.INSTANCE.getFriendManager();
    }

    private boolean clicked = false;

    @EventHandler
    private Listener<PlayerUpdateEvent> playerUpdateEventListener = new Listener<>(event -> {
        if (mc.currentScreen != null)
            return;
        if(!Mouse.isButtonDown(2)) {
            clicked = false;
            return;
        }

        if(!clicked) {
            clicked = true;

            final RayTraceResult result = mc.objectMouseOver;
            if (result == null || result.typeOfHit != RayTraceResult.Type.ENTITY)
                return;

            Entity entity = result.entityHit;
            if (entity == null || !(entity instanceof EntityPlayer))
                return;

            if(friendManager.isFriend(entity.getName())) {
                friendManager.remFriend(entity.getName());
                mc.player.sendMessage(new TextComponentString(ChatColor.prefix_parse('&', "&cRemoved friend &l'" + entity.getName() + "'")));
                return;
            }
            friendManager.addFriend(entity.getName());
            mc.player.sendMessage(new TextComponentString(ChatColor.prefix_parse('&', "&aAdded friend &l'" + entity.getName() + "'")));
        }
    });
}