package me.htrewrite.client.module.modules.gui.webpanel.handler.handlers.module;

import com.sun.net.httpserver.HttpExchange;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.modules.gui.webpanel.handler.response.WebServerResponse;

public class WebServerHandlerModuleToggle extends WebServerHandlerModule {
    public WebServerHandlerModuleToggle(Module module) {
        super(module);
    }

    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        module.toggle();

        return new WebServerResponse("OK");
    }
}