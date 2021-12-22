package me.htrewrite.client.module.modules.miscellaneous;

import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class Anti0dayModule extends Module {
    public static final ToggleableSetting notify = new ToggleableSetting("Notify", true);

    public Anti0dayModule() {
        super("Anti0Day", "Anti0Day exploit.", ModuleType.Miscellaneous, 0);
        addOption(notify);
        endOption();
    }

    @EventHandler
    private final Listener<ClientChatReceivedEvent> clientChatReceivedEventListener = new Listener<>(event ->{
        if(event.getMessage().getUnformattedText().contains("${")) {
            event.setCanceled(true);

            if(notify.isEnabled())
                sendMessage("&cAnti0Day exploit detected! Message: '&4" + event.getMessage().getUnformattedText().replaceAll("\\$\\{", "").replaceAll("}", "") + "&c'");
        }
    });
}