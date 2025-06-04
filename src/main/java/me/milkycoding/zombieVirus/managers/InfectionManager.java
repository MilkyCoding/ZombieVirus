package me.milkycoding.zombievirus.managers;

import me.milkycoding.zombievirus.ZombieVirus;
import me.milkycoding.zombievirus.stages.VirusStage;
import me.milkycoding.zombievirus.utils.ChatUtils;
import me.milkycoding.zombievirus.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


public class InfectionManager {
    // Хранилище зараженных игроков и их стадий
    private final Map<UUID, VirusStage> infectedPlayers;
    // Задачи для чихания игроков
    private final Map<UUID, BukkitTask> sneezeTasks;
    // Задачи для прогрессии стадий
    private final Map<UUID, BukkitTask> progressionTasks;
    // Генератор случайных чисел для шансов
    private final Random random;

    public InfectionManager() {
        this.infectedPlayers = new HashMap<>();
        this.sneezeTasks = new HashMap<>();
        this.progressionTasks = new HashMap<>();
        this.random = new Random();
    }


    public void handleZombieSpawn(Zombie zombie) {
        if (random.nextDouble() < ConfigUtils.getInfectionChance()) {
            zombie.setCustomName(ChatUtils.color(ConfigUtils.getMessage("zombie-name")));
            zombie.setCustomNameVisible(true);
        }
    }


    public void infectPlayer(Player player) {
        if (isInfected(player)) {
            player.sendMessage(ChatUtils.format(ConfigUtils.getMessage("already-infected")));
            return;
        }
        
        UUID playerId = player.getUniqueId();
        infectedPlayers.put(playerId, VirusStage.STAGE_1);
        startSneezeTask(player);
        startProgressionTask(player);
        
        player.sendMessage(ChatUtils.format(ConfigUtils.getMessage("infected")));
    }


    private void startProgressionTask(Player player) {
        BukkitTask task = Bukkit.getScheduler().runTaskLater(ZombieVirus.getInstance(), () -> {
            if (!isInfected(player)) return;
            progressInfection(player);
        }, ConfigUtils.getStageDuration() * 20L);
        
        progressionTasks.put(player.getUniqueId(), task);
    }


    public void progressInfection(Player player) {
        if (!isInfected(player)) return;

        VirusStage currentStage = infectedPlayers.get(player.getUniqueId());
        VirusStage nextStage = currentStage.getNextStage();
        
        if (nextStage == null) return; // Уже на максимальной стадии
        
        infectedPlayers.put(player.getUniqueId(), nextStage);
        applyStageEffects(player, nextStage);
        
        player.sendMessage(ChatUtils.format(String.format(ConfigUtils.getMessage("stage-progress"), nextStage.getNumber())));
        startProgressionTask(player);
    }


    private void applyStageEffects(Player player, VirusStage stage) {
        // Отменяем текущую задачу чихания
        cancelSneezeTask(player);
        
        // Применяем новые эффекты
        for (PotionEffect effect : stage.getEffects()) {
            player.addPotionEffect(effect);
        }
        
        // Запускаем новую задачу чихания с обновленным интервалом
        startSneezeTask(player);
    }


    private void startSneezeTask(Player player) {
        VirusStage stage = infectedPlayers.get(player.getUniqueId());
        if (stage == null) return;
        
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(ZombieVirus.getInstance(), () -> {
            if (!isInfected(player)) return;
            
            // Создаем частицы вокруг головы игрока
            player.getWorld().spawnParticle(
                org.bukkit.Particle.SNEEZE,
                player.getLocation().add(0, 1.7, 0),
                10, 0.3, 0.3, 0.3, 0
            );
            
            // Проигрываем звук чихания
            player.getWorld().playSound(
                player.getLocation(),
                org.bukkit.Sound.ENTITY_PLAYER_ATTACK_WEAK,
                1.0f,
                1.0f
            );
        }, stage.getSneezeInterval() * 20L, stage.getSneezeInterval() * 20L);
        
        sneezeTasks.put(player.getUniqueId(), task);
    }


    private void cancelSneezeTask(Player player) {
        BukkitTask task = sneezeTasks.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }


    private void cancelProgressionTask(Player player) {
        BukkitTask task = progressionTasks.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }


    public void curePlayer(Player player) {
        if (!isInfected(player)) {
            player.sendMessage(ChatUtils.format(ConfigUtils.getMessage("not-infected")));
            return;
        }
        
        UUID playerId = player.getUniqueId();
        infectedPlayers.remove(playerId);
        cancelSneezeTask(player);
        cancelProgressionTask(player);
        
        // Удаляем все эффекты
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.SLOWNESS);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        
        player.sendMessage(ChatUtils.format(ConfigUtils.getMessage("cured")));
    }


    public boolean isInfected(Player player) {
        return infectedPlayers.containsKey(player.getUniqueId());
    }


    public VirusStage getInfectionStage(Player player) {
        return infectedPlayers.getOrDefault(player.getUniqueId(), null);
    }

    public void saveAllData() {
        // TODO: Реализовать сохранение данных
    }


    public void clearAll() {
        // Отменяем все задачи чихания
        for (BukkitTask task : sneezeTasks.values()) {
            task.cancel();
        }
        sneezeTasks.clear();

        // Отменяем все задачи прогрессии
        for (BukkitTask task : progressionTasks.values()) {
            task.cancel();
        }
        progressionTasks.clear();

        // Очищаем список зараженных игроков
        infectedPlayers.clear();
    }
} 