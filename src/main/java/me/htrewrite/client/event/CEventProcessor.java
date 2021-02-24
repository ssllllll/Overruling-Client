package me.htrewrite.client.event;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.event.custom.player.PlayerDisconnectEvent;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;

public class CEventProcessor implements Listenable {
    private Thread saveThread = null;

    @EventHandler
    private Listener<PlayerDisconnectEvent> playerDisconnectEventListener = new Listener<>(event -> {
        saveThread = new Thread(() -> {
            HTRewrite.INSTANCE.saveEverything();
        });
        saveThread.start();
    });
}