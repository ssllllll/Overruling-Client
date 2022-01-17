package me.htrewrite.client.module.modules.gui.webpanel.handler.handlers;

import com.sun.net.httpserver.HttpExchange;
import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.module.modules.gui.webpanel.handler.WebServerHandler;
import me.htrewrite.client.module.modules.gui.webpanel.handler.response.WebServerResponse;

public class WebServerHandlerModuleList extends WebServerHandler {
    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        StringBuilder sb = new StringBuilder();
        HTRewrite.INSTANCE.getModuleManager().getModules().forEach(module -> sb.append(module.getName() + ";"));
        return sb.toString().isEmpty() ? new WebServerResponse("No modules found", 404) : new WebServerResponse(sb.toString().substring(0, sb.toString().length()-1), 200);
    }
}