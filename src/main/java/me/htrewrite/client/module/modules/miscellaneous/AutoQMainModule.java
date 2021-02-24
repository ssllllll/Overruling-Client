package me.htrewrite.client.module.modules.miscellaneous;

import me.htrewrite.client.event.custom.player.PlayerUpdateEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.ChatColor;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.util.text.TextComponentString;

public class AutoQMainModule extends Module {
    public static final ToggleableSetting message = new ToggleableSetting("Debug", null, false);
    public static final ValueSetting delay = new ValueSetting("Seconds", null, 120, 10, 600);
    public static final ToggleableSetting only2b2t = new ToggleableSetting("Only2b2t", null, true);

    private int wait;

    public AutoQMainModule() {
        super("AutoQMain", "Sends /queue main.", ModuleType.Miscellaneous, 0);
        addOption(message);
        addOption(delay);
        addOption(only2b2t);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.wait = 12000;
    }

    @EventHandler
    private Listener<PlayerUpdateEvent> playerUpdateEventListener = new Listener<>(event -> {
        if(mc.player == null) return;

        if(mc.getCurrentServerData() != null || (mc.getCurrentServerData() != null && !mc.getCurrentServerData().serverIP.equals("2b2t.org") && only2b2t.isEnabled())) {
            this.wait = 0;
            mc.player.sendMessage(new TextComponentString(ChatColor.prefix_parse('&', "&cServer is not 2b2t.org, disabling...")));
            toggle();
            return;
        }

        if(this.wait <= 0) {
            mc.player.sendChatMessage("/queue main");
            if(message.isEnabled())
                mc.player.sendMessage(new TextComponentString(ChatColor.prefix_parse('&', "&aRan command /queue main!")));
            this.wait = delay.getValue().intValue();
        }
        this.wait--;
    });
}