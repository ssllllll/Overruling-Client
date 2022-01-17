package me.htrewrite.client.module.modules.gui.webpanel.handler.handlers;

import com.sun.net.httpserver.HttpExchange;
import me.htrewrite.client.module.modules.gui.webpanel.handler.WebServerHandler;
import me.htrewrite.client.module.modules.gui.webpanel.handler.response.WebServerResponse;

public class WebServerHandlerRoot extends WebServerHandler {
    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        return new WebServerResponse("<style>\n" +
                "    body {\n" +
                "        background-image: url(\"https://cdn.anvilshop.store/img/splash-bg.png\");\n" +
                "        background-repeat: no-repeat;\n" +
                "        background-size: cover;\n" +
                "        background-position: center;\n" +
                "        background-attachment: fixed;\n" +
                "    }\n" +
                "\n" +
                "    .card {\n" +
                "        width: 250px;\n" +
                "        height: auto;\n" +
                "        border: 1px solid black;\n" +
                "        display: none;\n" +
                "        text-align: center;\n" +
                "        vertical-align: top;\n" +
                "        overflow: auto;\n" +
                "        float: left;\n" +
                "        margin: 20px;\n" +
                "    }\n" +
                "\n" +
                "    .card-header {\n" +
                "        height: 60px;\n" +
                "    }\n" +
                "\n" +
                "    button {\n" +
                "        width: 100%;\n" +
                "        height: 100%;\n" +
                "        border: 1px solid rgba(0, 0, 0, 0.5);\n" +
                "        background-color: transparent;\n" +
                "        cursor: pointer;\n" +
                "        color: white;\n" +
                "    }\n" +
                "</style>\n" +
                "\n" +
                "<script>\n" +
                "    function toggle(moduleName) {\n" +
                "        const xhr = new XMLHttpRequest();\n" +
                "        xhr.open('GET', '/api/module/' + moduleName + \"/toggle\", true);\n" +
                "        xhr.send();\n" +
                "    }\n" +
                "\n" +
                "    let alreadyBoot = false;\n" +
                "    function boot() {\n" +
                "        const xhr = new XMLHttpRequest();\n" +
                "        xhr.open('GET', '/api/module', true);\n" +
                "        xhr.onreadystatechange = function () {\n" +
                "            if(this.readyState !== 4 || alreadyBoot)\n" +
                "                return;\n" +
                "            const response = this.responseText;\n" +
                "            const modules = response.split(\";\");\n" +
                "            console.log(response);\n" +
                "            for(let i = 0; i < modules.length; i++) {\n" +
                "                const module = modules[i];\n" +
                "                const li = document.createElement(\"li\");\n" +
                "                li.className = \"list-group list-group-flush\";\n" +
                "                li.innerHTML = \"<button onclick='toggle(\\\"\" + module + \"\\\")'>\" + module + \"</button>\";\n" +
                "                console.log(module);\n" +
                "                if(module === \"WebPanel\")\n" +
                "                    alreadyBoot = true;\n" +
                "\n" +
                "                const xhr2 = new XMLHttpRequest();\n" +
                "                xhr2.open('GET', '/api/module/' + module + \"/\", true);\n" +
                "                xhr2.onreadystatechange = function() {\n" +
                "                    if(this.readyState !== 4)\n" +
                "                        return;\n" +
                "\n" +
                "                    const responseInfo = this.responseText;\n" +
                "                    const category = responseInfo.split(\";\")[1];\n" +
                "                    const id = category.toLowerCase()+\"-modules\";\n" +
                "\n" +
                "                    document.getElementById(id).appendChild(li);\n" +
                "                    console.log(responseInfo);\n" +
                "                }\n" +
                "                xhr2.send();\n" +
                "            }\n" +
                "        }\n" +
                "        xhr.send();\n" +
                "    }\n" +
                "</script>\n" +
                "\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <meta charset=\"utf-8\">\n" +
                "\n" +
                "        <link rel=\"stylesheet\" href=\"https://cdn.anvilshop.store/css/bootstrap.css\">\n" +
                "        <link rel=\"stylesheet\" href=\"https://cdn.anvilshop.store/css/mdb.min.css\">\n" +
                "        <title>ClickGUI</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div class=\"card combat-card text-white bg-info mb-3\" id=\"combat-card\">\n" +
                "            <div class=\"card-header\" id=\"combat-header\">\n" +
                "                <h2>Combat</h2>\n" +
                "            </div>\n" +
                "            <ul class=\"list-group list-group-flush\" id=\"combat-modules\">\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"card exploits-card text-white bg-info mb-3\" id=\"exploits-card\">\n" +
                "            <div class=\"card-header\" id=\"exploits-header\">\n" +
                "                <h2>Exploits</h2>\n" +
                "            </div>\n" +
                "            <ul class=\"list-group list-group-flush\" id=\"exploits-modules\">\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"card miscellaneous-card text-white bg-info mb-3\" id=\"miscellaneous-card\">\n" +
                "            <div class=\"card-header\" id=\"miscellaneous-header\">\n" +
                "                <h2>Miscellaneous</h2>\n" +
                "            </div>\n" +
                "            <ul class=\"list-group list-group-flush\" id=\"miscellaneous-modules\">\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"card movement-card text-white bg-info mb-3\" id=\"movement-card\">\n" +
                "            <div class=\"card-header\" id=\"movement-header\">\n" +
                "                <h2>Movement</h2>\n" +
                "            </div>\n" +
                "            <ul class=\"list-group list-group-flush\" id=\"movement-modules\">\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"card render-card text-white bg-info mb-3\" id=\"render-card\">\n" +
                "            <div class=\"card-header\" id=\"render-header\">\n" +
                "                <h2>Render</h2>\n" +
                "            </div>\n" +
                "            <ul class=\"list-group list-group-flush\" id=\"render-modules\">\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"card world-card text-white bg-info mb-3\" id=\"world-card\">\n" +
                "            <div class=\"card-header\" id=\"world-header\">\n" +
                "                <h2>World</h2>\n" +
                "            </div>\n" +
                "            <ul class=\"list-group list-group-flush\" id=\"world-modules\">\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"card gui-card text-white bg-info mb-3\" id=\"gui-card\">\n" +
                "            <div class=\"card-header\" id=\"gui-header\">\n" +
                "                <h2>Gui</h2>\n" +
                "            </div>\n" +
                "            <ul class=\"list-group list-group-flush\" id=\"gui-modules\">\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "\n" +
                "        <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>\n" +
                "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\" integrity=\"sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1\" crossorigin=\"anonymous\"></script>\n" +
                "        <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\" integrity=\"sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM\" crossorigin=\"anonymous\"></script>\n" +
                "    </body>\n" +
                "</html>\n" +
                "\n" +
                "<script>boot();</script>");
    }
}