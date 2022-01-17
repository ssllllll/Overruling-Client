package me.htrewrite.client.module.modules.gui.webpanel;

import com.sun.net.httpserver.HttpServer;
import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.event.custom.CustomEvent;
import me.htrewrite.client.event.custom.client.ClientSettingChangeEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.module.modules.gui.webpanel.handler.handlers.WebServerHandlerModuleList;
import me.htrewrite.client.module.modules.gui.webpanel.handler.handlers.WebServerHandlerRoot;
import me.htrewrite.client.module.modules.gui.webpanel.handler.handlers.module.*;
import me.htrewrite.exeterimports.mcapi.settings.IntegerSetting;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;

public class WebPanelModule extends Module {
    public static final ToggleableSetting openPanel = new ToggleableSetting("Open Panel", false);
    public static final IntegerSetting port = new IntegerSetting("Port", 13890, 0, 65535);

    private HttpServer server;

    public WebPanelModule() {
        super("WebPanel", "A panel to manage your modules", ModuleType.Gui, 0);
        defaultEnabled();
        addOption(openPanel);
        addOption(port);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        try {
            this.server = HttpServer.create(new InetSocketAddress(port.getValue()), 0);
        } catch (IOException exception) {
            exception.printStackTrace();
            sendMessage("&cFailed to start server at port " + port.getValue());
            toggle();
        }

        this.server.createContext("/", new WebServerHandlerRoot());
        this.server.createContext("/api/module", new WebServerHandlerModuleList());
        for(Module module : HTRewrite.INSTANCE.getModuleManager().getModules()) {
            String ROOT_CONTEXT = "/api/module/" + module.getName() + "/";

            this.server.createContext(ROOT_CONTEXT, new WebServerHandlerModuleInfo(module));
            this.server.createContext(ROOT_CONTEXT + "enable", new WebServerHandlerModuleEnable(module));
            this.server.createContext(ROOT_CONTEXT + "disable", new WebServerHandlerModuleDisable(module));
            this.server.createContext(ROOT_CONTEXT + "toggle", new WebServerHandlerModuleToggle(module));
            this.server.createContext(ROOT_CONTEXT + "config", new WebServerHandlerModuleConfig(module));
        }
        this.server.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        this.server.stop(0);
    }

    @EventHandler
    private Listener<ClientSettingChangeEvent> settingChangeEventListener = new Listener<>(event -> {
        if(event.setting != openPanel)
            return;
        if(openPanel.isEnabled()) {
            try {
                Desktop.getDesktop().browse(new URL("http://localhost:" + port.getValue()).toURI());
            } catch (Exception e) {
                e.printStackTrace();
                sendMessage("&cFailed to open web browser");
            }

            openPanel.setEnabled(false);
        }
    });
}