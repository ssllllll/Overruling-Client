package me.htrewrite.client.module.modules.movement;

import me.htrewrite.client.event.custom.player.PlayerUpdateMoveStateEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public class AutoWalkModule extends Module {
    public AutoWalkModule() {
        super("AutoWalk", "It makes you walk.", ModuleType.Movement, 0);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if(mc.player == null) toggle();
    }

    @EventHandler
    private Listener<PlayerUpdateMoveStateEvent> playerUpdateMoveStateListener = new Listener<>(event -> {
        mc.player.movementInput.moveForward++;
    });
}