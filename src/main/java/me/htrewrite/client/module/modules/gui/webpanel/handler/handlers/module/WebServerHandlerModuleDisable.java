package me.htrewrite.client.module.modules.gui.webpanel.handler.handlers.module;

import com.sun.net.httpserver.HttpExchange;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.modules.gui.webpanel.handler.response.WebServerResponse;

public class WebServerHandlerModuleDisable extends WebServerHandlerModule {
    public WebServerHandlerModuleDisable(Module module) {
        super(module);
    }

    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        module.rawToggle(false);
        module.onDisable();

        return new WebServerResponse("OK");
    }
}