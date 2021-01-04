package me.overruling.client.clickgui.components.buttons.settings;

import me.overruling.client.clickgui.components.Colors;
import me.overruling.client.clickgui.components.Component;
import me.overruling.exeterimports.mcapi.settings.ValueSetting;

public class ValueComponent extends Component {
    private final ValueSetting valueSetting;

    public ValueComponent(ValueSetting valueSetting, String label, int positionX, int positionY, int width, int height) {
        super(label, positionX, positionY, width, height);
        this.valueSetting = valueSetting;
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int mouseButton) {
        //TODO slidING or avo type shit
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        drawRect(getPositionX() + 1, getPositionY() + 1, getPositionX() + getWidth() - 1,
                getPositionY() + getHeight() - 1, Colors.BUTTON_COMPONENT.getColor());
        font.drawString(String.format("%s (%s)", getLabel(), valueSetting.getValue()), getPositionX() + 4, getPositionY() + 1,
                Colors.BUTTON_LABEL_ENABLED.getColor());
    }
}