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

/**
 * Основной класс плагина ZombieVirus.
 * Реализует систему заражения зомби-вирусом с различными стадиями и вакцинацией.
 */
public class ZombieVirus extends JavaPlugin {
    // Статический экземпляр плагина для доступа из других классов
    private static ZombieVirus instance;
    
    // Менеджеры для управления заражением и вакцинами
    private InfectionManager infectionManager;
    private VaccineManager vaccineManager;

    @Override
    public void onEnable() {
        // Сохраняем экземпляр плагина
        instance = this;
        
        // Загружаем конфигурацию
        saveDefaultConfig();
        ConfigUtils.loadConfig(this);
        
        // Инициализируем менеджеры
        infectionManager = new InfectionManager();
        vaccineManager = new VaccineManager();
        
        // Регистрируем команды
        Objects.requireNonNull(getCommand("zombievirus")).setExecutor(new ZombieVirusCommand());
        
        // Регистрируем слушатели событий
        getServer().getPluginManager().registerEvents(new InfectionListener(), this);
        getServer().getPluginManager().registerEvents(new VaccineListener(), this);
        
        getLogger().info("ZombieVirus успешно включен!");
    }

    @Override
    public void onDisable() {
        // Сохраняем данные при выключении
        if (infectionManager != null) {
            infectionManager.saveAllData();
        }

        // Отменяем все задачи
        getServer().getScheduler().cancelTasks(this);

        // Удаляем рецепт вакцины
        getServer().removeRecipe(new NamespacedKey(this, "vaccine"));

        // Очищаем все зараженных игроков
        if (infectionManager != null) {
            infectionManager.clearAll();
        }

        // Очищаем статический экземпляр
        instance = null;

        getLogger().info("ZombieVirus выключен!");
    }

    /**
     * Получить экземпляр плагина
     * @return Экземпляр плагина
     */
    public static ZombieVirus getInstance() {
        return instance;
    }

    /**
     * Получить менеджер заражения
     * @return Менеджер заражения
     */
    public InfectionManager getInfectionManager() {
        return infectionManager;
    }

    /**
     * Получить менеджер вакцин
     * @return Менеджер вакцин
     */
    public VaccineManager getVaccineManager() {
        return vaccineManager;
    }
}
