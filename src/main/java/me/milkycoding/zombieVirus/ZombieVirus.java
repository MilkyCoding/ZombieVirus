package me.milkycoding.zombievirus;

import me.milkycoding.zombievirus.commands.ZombieVirusCommand;
import me.milkycoding.zombievirus.listeners.InfectionListener;
import me.milkycoding.zombievirus.listeners.VaccineListener;
import me.milkycoding.zombievirus.managers.InfectionManager;
import me.milkycoding.zombievirus.managers.VaccineManager;
import me.milkycoding.zombievirus.utils.ConfigUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ZombieVirus extends JavaPlugin {
    private static ZombieVirus instance;
    private InfectionManager infectionManager;
    private VaccineManager vaccineManager;

    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        ConfigUtils.loadConfig(this);
        
        infectionManager = new InfectionManager();
        vaccineManager = new VaccineManager();
        
        Objects.requireNonNull(getCommand("zombievirus")).setExecutor(new ZombieVirusCommand());
        
        getServer().getPluginManager().registerEvents(new InfectionListener(), this);
        getServer().getPluginManager().registerEvents(new VaccineListener(), this);
        
        getLogger().info("ZombieVirus успешно включен!");
    }

    @Override
    public void onDisable() {
        if (infectionManager != null) {
            infectionManager.saveAllData();
        }

        getServer().getScheduler().cancelTasks(this);
        getServer().removeRecipe(new NamespacedKey(this, "vaccine"));

        if (infectionManager != null) {
            infectionManager.clearAll();
        }

        instance = null;

        getLogger().info("ZombieVirus выключен!");
    }

    public static ZombieVirus getInstance() {
        return instance;
    }

    public InfectionManager getInfectionManager() {
        return infectionManager;
    }

    public VaccineManager getVaccineManager() {
        return vaccineManager;
    }
}
