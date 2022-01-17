package me.htrewrite.client.module.modules.gui.webpanel.handler.response;

public class WebServerResponse {
    public String content;
    public int code;

    public WebServerResponse(String content, int code) {
        this.content = content;
        this.code = code;
    }
    public WebServerResponse(String content) { this(content, 200); }
    public WebServerResponse(int code) { this("", code); }
    public WebServerResponse() { this(""); }
}