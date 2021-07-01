package me.htrewrite.client.command.commands;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.command.Command;
import me.htrewrite.client.manager.BlockHighlightManager;
import net.minecraft.block.Block;

public class BlockHighlightCommand extends Command {
    private BlockHighlightManager blockHighlightManager;

    public BlockHighlightCommand() {
        super("blockhighlight", "['list'] ['clear'] [<add/remove> <block>]", "BlockHighlight management command.");

        this.blockHighlightManager = HTRewrite.INSTANCE.getBlockHighlightManager();
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
            for(int i = 0; i < blockHighlightManager.blocks.size(); i++)
                builder.append(blockHighlightManager.blocks.get(i) + ((i==blockHighlightManager.blocks.size()-1)?"}":", "));
            sendMessage(builder.toString());
        } else if(args[0].equalsIgnoreCase("add") && args.length > 1) {
            Block addedBlock = blockHighlightManager.addBlock(args[1]);
            blockHighlightManager.save();
            sendMessage("&aAdded block id &2" + Block.getIdFromBlock(addedBlock));
        } else if((args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rem")) && args.length > 1) {
            Block removedBlock = blockHighlightManager.removeBlock(args[1]);
            blockHighlightManager.save();
            sendMessage("&cRemoved block id &4" + Block.getIdFromBlock(removedBlock));
        } else if(args[0].equalsIgnoreCase("clear")) {
            blockHighlightManager.blocks.clear();
            blockHighlightManager.save();
            sendMessage("&cCleared blocks list!");
        } else sendInvalidUsageMessage();
    }
}