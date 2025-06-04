package me.milkycoding.zombievirus.utils;

import me.milkycoding.zombievirus.ZombieVirus;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtils {
    private static FileConfiguration config;
    private static double infectionChance;
    private static int stageDuration;

    public static void loadConfig(ZombieVirus plugin) {
        plugin.reloadConfig();
        config = plugin.getConfig();
        infectionChance = config.getDouble("infection.chance", 0.2);
        stageDuration = config.getInt("infection.stage-duration", 300);
    }

    public static String getMessage(String path) {
        return config.getString("messages." + path, "&cMissing message: " + path);
    }

    public static String getPrefix() {
        return config.getString("prefix", "&8[&cZombieVirus&8] &r");
    }

    public static double getInfectionChance() {
        return infectionChance;
    }

    public static int getStageDuration() {
        return stageDuration;
    }

    public static int getSneezeInterval() {
        return config.getInt("infection.sneeze-interval", 120);
    }

    public static boolean isDebugEnabled() {
        return config.getBoolean("debug", false);
    }
} 