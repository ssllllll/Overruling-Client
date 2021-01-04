package me.overruling.client.event;

import me.overruling.client.Overruling;
import me.overruling.client.event.custom.player.PlayerDisconnectEvent;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;

public class CEventProcessor implements Listenable {
    private Thread saveThread = null;

    @EventHandler
    private Listener<PlayerDisconnectEvent> playerDisconnectEventListener = new Listener<>(event -> {
        saveThread = new Thread(() -> {
            Overruling.INSTANCE.saveEverything();
        });
        saveThread.start();
    });
}