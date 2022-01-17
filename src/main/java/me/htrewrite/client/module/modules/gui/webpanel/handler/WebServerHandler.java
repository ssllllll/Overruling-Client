package me.htrewrite.client.module.modules.gui.webpanel.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.htrewrite.client.module.modules.gui.webpanel.handler.response.WebServerResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public abstract class WebServerHandler implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        WebServerResponse response = createResponse(httpExchange);
        httpExchange.sendResponseHeaders(response.code, response.content.getBytes(StandardCharsets.UTF_8).length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.content.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    public String[] getQuery(URI uri) {
        String query = uri.getQuery();
        if(query == null)
            return new String[0];
        return query.contains("&") ? query.split("&") : new String[]{query};
    }
    public abstract WebServerResponse createResponse(HttpExchange httpExchange);
}