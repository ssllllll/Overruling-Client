package me.htrewrite.client.command.commands;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.command.Command;
import me.htrewrite.client.manager.XRayManager;
import net.minecraft.block.Block;

public class XRayCommand extends Command {
    private XRayManager xRayManager;

    public XRayCommand() {
        super("xray", "['list'] [<add/remove> <block>]", "XRay management command.");

        this.xRayManager = HTRewrite.INSTANCE.getxRayManager();
    }

    @Override
    public void call(String[] args) {
        if(args.length < 1) {
            sendInvalidUsageMessage();
            return;
        }

        if(args[0].equalsIgnoreCase("list")) {
            StringBuilder builder = new StringBuilder();
            builder.append("  &eBlocks= &6{");
            for(int i = 0; i < xRayManager.blocks.size(); i++)
                builder.append(Block.getIdFromBlock(xRayManager.blocks.get(i)) + ((i==xRayManager.blocks.size()-1)?"}":", "));
            sendMessage(builder.toString());
        } else if(args[0].equalsIgnoreCase("add") && args.length > 1) {
            Block addedBlock = xRayManager.addXRayBlock(args[1]);
            xRayManager.save();
            sendMessage("&aAdded block id &2" + Block.getIdFromBlock(addedBlock));
        } else if((args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rem")) && args.length > 1) {
            Block removedBlock = xRayManager.removeXRayBlock(args[1]);
            xRayManager.save();
            sendMessage("&cRemoved block id &4" + Block.getIdFromBlock(removedBlock));
        } else sendInvalidUsageMessage();
    }
}