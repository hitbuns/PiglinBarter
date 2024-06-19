package com.ConquestTech.Utilities;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class Utils {

    public static int RNGInt(double min,double max) {
        return (int) Math.min(Math.ceil(min),Math.max(Math.floor(max),Math.round(RNG(min,max))));
    }

    public static double RNG(double min,double max) {
        double rngBounds = max-min;
        return min+Math.random()*rngBounds;
    }

    public static String color(String s) {
        String s1 = s != null ? ChatColor.translateAlternateColorCodes('&',s) : null;

        return translateHexColorCodes("&#","",s1);
    }

    public static List<String> translateHexColorCodes(String startTag, String endTag, List<String> list) {
        return list != null ? list.stream()
                .map(s -> translateHexColorCodes(startTag,endTag,s))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    public static String translateHexColorCodes(String startTag, String endTag, String message)
    {

        if (message == null) return null;

        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }


    public static List<String> color(List<String> strings) {
        return strings != null ? strings.stream().map(Utils::color)
                .collect(Collectors.toList()) : new ArrayList<>();
    }


}
