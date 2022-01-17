package me.htrewrite.client.module.modules.gui.webpanel.handler.handlers.module;

import com.sun.net.httpserver.HttpExchange;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.modules.gui.webpanel.handler.response.WebServerResponse;
import me.htrewrite.exeterimports.mcapi.settings.*;
import org.json.JSONObject;

public class WebServerHandlerModuleConfig extends WebServerHandlerModule {
    public WebServerHandlerModuleConfig(Module module) {
        super(module);
    }

    public JSONObject generateFromModule(Module module) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("enabled", module.isEnabled());
        for(Setting setting : module.getOptions()) {
            if(setting instanceof ModeSetting) {
                ModeSetting modeSetting = (ModeSetting)setting;
                jsonObject.put(modeSetting.getLabel(), modeSetting.getI());
            } else if(setting instanceof StringSetting) {
                StringSetting stringSetting = (StringSetting)setting;
                jsonObject.put(stringSetting.getLabel(), stringSetting.getValue());
            } else if(setting instanceof ValueSetting) {
                ValueSetting valueSetting = (ValueSetting)setting;
                jsonObject.put(valueSetting.getLabel(), valueSetting.getValue());
            } else if(setting instanceof ToggleableSetting) {
                ToggleableSetting toggleableSetting = (ToggleableSetting)setting;
                jsonObject.put(toggleableSetting.getLabel(), toggleableSetting.isEnabled());
            } else if(setting instanceof BindSetting) {
                BindSetting bindSetting = (BindSetting)setting;
                jsonObject.put("key", bindSetting.getBind());
            }
        }

        return jsonObject;
    }

    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        return new WebServerResponse(generateFromModule(module).toString());
    }
}