package me.htrewrite.client.module.modules.gui;

import me.htrewrite.client.event.custom.module.ModuleToggleEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.ChatColor;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.util.text.TextComponentString;

public class NotificationsModule extends Module {
    public ToggleableSetting toggleNotifications = new ToggleableSetting("ModuleToggle", null, true);

    public NotificationsModule() {
        super("Notifications", "Notifications", ModuleType.Gui, 0);
        addOption(toggleNotifications);
        endOption();
    }

    @EventHandler
    private Listener<ModuleToggleEvent> moduleToggleEventListener = new Listener<>(event -> {
        if(!toggleNotifications.isEnabled() || mc.player == null || mc.world == null)
            return;

        mc.player.sendMessage(new TextComponentString(ChatColor.prefix_parse('&', event.toggled ?
                "&a&l" + event.module.getName() + " &ais now ON!":
                "&c&l" + event.module.getName() + " &cis now OFF!")));
    });
}