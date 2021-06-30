package me.htrewrite.client.manager;

import me.htrewrite.client.util.ConfigUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class XRayManager {
    private ConfigUtils blocksConfig;
    public ArrayList<Block> blocks;

    public XRayManager() {
        this.blocksConfig = new ConfigUtils("blocks", "xray");
        this.blocks = new ArrayList<>();

        if(blocksConfig.get("blocks") == null) { /* Set defaults */
            blocks.add(Blocks.DIAMOND_ORE);
            blocks.add(Blocks.COAL_ORE);
            blocks.add(Blocks.EMERALD_ORE);
            blocks.add(Blocks.GOLD_ORE);
            blocks.add(Blocks.IRON_ORE);
            blocks.add(Blocks.LAPIS_ORE);
            blocks.add(Blocks.QUARTZ_ORE);
            blocks.add(Blocks.REDSTONE_ORE);
            blocks.add(Blocks.LIT_REDSTONE_ORE);
            blocks.add(Blocks.END_PORTAL_FRAME);
            blocks.add(Blocks.END_PORTAL);
            blocks.add(Blocks.CHEST);
            blocks.add(Blocks.ENDER_CHEST);
            blocks.add(Blocks.TRAPPED_CHEST);
            blocks.add(Blocks.WATER);
            blocks.add(Blocks.FLOWING_WATER);
            blocks.add(Blocks.LAVA);
            blocks.add(Blocks.FLOWING_LAVA);

            save();
            return;
        }

        JSONArray array = (JSONArray)blocksConfig.get("blocks");
        for(int i = 0; i < array.size(); i++)
            blocks.add(Block.getBlockById((int)array.get(i)));
    }

    public void save() {
        JSONArray array = new JSONArray();
        for(Block block : blocks)
            array.add(Block.getIdFromBlock(block));
        blocksConfig.set("blocks", array);
        blocksConfig.save();
    }

    public boolean isXRayBlock(Block block) { return blocks.contains(block); }

    public Block addXRayBlock(Block block) { blocks.add(block); return block; }
    public Block addXRayBlock(int id) { return addXRayBlock(Block.getBlockById(id)); }
    public Block addXRayBlock(String reference) {
        if(reference.matches("\\d+")) /* ID */
            return addXRayBlock(Integer.parseInt(reference));
        return addXRayBlock(Block.getBlockFromName(reference));
    }

    public Block removeXRayBlock(Block block) { blocks.remove(block); return block; }
    public Block removeXRayBlock(int id) { return removeXRayBlock(Block.getBlockById(id)); }
    public Block removeXRayBlock(String reference) {
        if(reference.matches("\\d+"))
            return addXRayBlock(Integer.parseInt(reference));
        return addXRayBlock(Block.getBlockFromName(reference));
    }
}