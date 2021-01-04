package me.overruling.client.event.custom.world;

import me.overruling.client.event.custom.CustomEvent;
import net.minecraft.block.Block;

public class BlockPlaceEvent extends CustomEvent {
    public final Block block;

    public BlockPlaceEvent(Block block) {
        this.block = block;
    }
}