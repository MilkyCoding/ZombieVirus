package me.milkycoding.zombievirus.utils;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern RGB_PATTERN = Pattern.compile("&rgb\\((\\d+),(\\d+),(\\d+)\\)");
    
    public static String color(String message) {
        if (message == null) return "";
        
        Matcher hexMatcher = HEX_PATTERN.matcher(message);
        while (hexMatcher.find()) {
            String hex = hexMatcher.group(1);
            message = message.replace("&#" + hex, ChatColor.of("#" + hex).toString());
        }
        
        Matcher rgbMatcher = RGB_PATTERN.matcher(message);
        while (rgbMatcher.find()) {
            int r = Integer.parseInt(rgbMatcher.group(1));
            int g = Integer.parseInt(rgbMatcher.group(2));
            int b = Integer.parseInt(rgbMatcher.group(3));
            message = message.replace(
                "&rgb(" + r + "," + g + "," + b + ")",
                ChatColor.of(new java.awt.Color(r, g, b)).toString()
            );
        }
        
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String format(String message) {
        return color(ConfigUtils.getPrefix() + message);
    }
    
    public static String format(String path, Object... args) {
        String message = ConfigUtils.getMessage(path);
        return format(String.format(message, args));
    }
    
    public static String error(String message) {
        return format("&c" + message);
    }
    
    public static String success(String message) {
        return format("&a" + message);
    }
    
    public static String warning(String message) {
        return format("&e" + message);
    }
    
    public static String info(String message) {
        return format("&b" + message);
    }
} 