package me.htrewrite.client.module.modules.gui.webpanel.handler.handlers.module;

import com.sun.net.httpserver.HttpExchange;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.modules.gui.webpanel.handler.response.WebServerResponse;

public class WebServerHandlerModuleInfo extends WebServerHandlerModule {
    public WebServerHandlerModuleInfo(Module module) {
        super(module);
    }

    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        return new WebServerResponse(module.getName()+";"+module.getCategory().name()+";"+module.getDesc()+";"+(module.isEnabled()?"1":"0"));
    }
}