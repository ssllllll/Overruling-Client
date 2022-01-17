package me.htrewrite.client.module.modules.gui.webpanel.handler.handlers.module;

import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.modules.gui.webpanel.handler.WebServerHandler;

public abstract class WebServerHandlerModule extends WebServerHandler {
    public final Module module;
    public WebServerHandlerModule(Module module) { this.module = module; }
}