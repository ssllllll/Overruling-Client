package me.htrewrite.client.event.custom.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderArmorLayerEvent extends Event {

    public EntityLivingBase Entity;

    public void EventRenderArmorLayer(EntityLivingBase p_Entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn)
    {
        Entity = p_Entity;
    }

}
