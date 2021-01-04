package me.overruling.client.util;

import me.overruling.client.Overruling;

public class ChatColor {
    public static String parse(char c, String txt) { return txt.replaceAll(String.valueOf(c), "\u00a7"); }
    public static String prefix_parse(char c, String txt) { return parse('&', "&8&l[&b" + Overruling.NAME + "&8&l] &r" + txt); }
    public static String enumList(Enum[] enums) {
        StringBuilder list = new StringBuilder();
        for(int i = 0; i < enums.length; i++) {
            list.append(enums[i].name());
            if(enums.length-1!=i)
                list.append(", ");
        }
        return list.toString();
    }
}