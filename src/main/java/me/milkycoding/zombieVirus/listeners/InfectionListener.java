package me.milkycoding.zombievirus.listeners;

import me.milkycoding.zombievirus.ZombieVirus;
import me.milkycoding.zombievirus.managers.InfectionManager;
import me.milkycoding.zombievirus.stages.VirusStage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Слушатель событий, связанных с заражением.
 * Обрабатывает появление зомби, атаки и взаимодействия между игроками.
 */
public class InfectionListener implements Listener {
    private final InfectionManager infectionManager;

    public InfectionListener() {
        this.infectionManager = ZombieVirus.getInstance().getInfectionManager();
    }

    /**
     * Обработка появления зомби
     * @param event Событие появления сущности
     */
    @EventHandler
    public void onZombieSpawn(EntitySpawnEvent event) {
        if (event.getEntityType() != EntityType.ZOMBIE) return;
        infectionManager.handleZombieSpawn((Zombie) event.getEntity());
    }

    /**
     * Обработка атаки зомби на игрока
     * @param event Событие урона сущности
     */
    @EventHandler
    public void onZombieAttack(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Zombie)) return;
        
        Player player = (Player) event.getEntity();
        Zombie zombie = (Zombie) event.getDamager();
        
        // Проверяем, является ли зомби зараженным
        if (zombie.getCustomName() != null && zombie.getCustomName().contains("Зараженный")) {
            infectionManager.infectPlayer(player);
        }
    }

    /**
     * Обработка атаки игрока на игрока
     * @param event Событие урона сущности
     */
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        
        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        
        // Если атакующий заражен и на 2+ стадии, заражаем жертву
        if (infectionManager.isInfected(attacker)) {
            VirusStage stage = infectionManager.getInfectionStage(attacker);
            if (stage != null && stage.getNumber() >= 2) {
                infectionManager.infectPlayer(victim);
            }
        }
    }

    /**
     * Обработка взаимодействия игрока с игроком
     * @param event Событие взаимодействия с сущностью
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) return;
        
        Player attacker = event.getPlayer();
        Player victim = (Player) event.getRightClicked();
        
        // Если атакующий заражен и на 2+ стадии, заражаем жертву
        if (infectionManager.isInfected(attacker)) {
            VirusStage stage = infectionManager.getInfectionStage(attacker);
            if (stage != null && stage.getNumber() >= 2) {
                infectionManager.infectPlayer(victim);
            }
        }
    }
} 