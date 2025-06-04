package me.milkycoding.zombievirus.commands;

import me.milkycoding.zombievirus.ZombieVirus;
import me.milkycoding.zombievirus.managers.InfectionManager;
import me.milkycoding.zombievirus.utils.ChatUtils;
import me.milkycoding.zombievirus.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Обработчик команд плагина.
 * Реализует команды для проверки статуса заражения, лечения и перезагрузки конфигурации.
 */
public class ZombieVirusCommand implements CommandExecutor {
    private final InfectionManager infectionManager;

    public ZombieVirusCommand() {
        this.infectionManager = ZombieVirus.getInstance().getInfectionManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatUtils.format(ConfigUtils.getMessage("command-help")));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "status":
                // Проверка статуса заражения
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatUtils.format(ConfigUtils.getMessage("player-only")));
                    return true;
                }
                handleStatus((Player) sender);
                break;

            case "cure":
                // Лечение игрока
                if (!sender.hasPermission("zombievirus.cure")) {
                    sender.sendMessage(ChatUtils.format(ConfigUtils.getMessage("no-permission")));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatUtils.format(ConfigUtils.getMessage("cure-usage")));
                    return true;
                }
                handleCure(sender, args[1]);
                break;

            case "reload":
                // Перезагрузка конфигурации
                if (!sender.hasPermission("zombievirus.admin")) {
                    sender.sendMessage(ChatUtils.format(ConfigUtils.getMessage("no-permission")));
                    return true;
                }
                ZombieVirus.getInstance().reloadConfig();
                sender.sendMessage(ChatUtils.format(ConfigUtils.getMessage("reload")));
                break;

            default:
                sender.sendMessage(ChatUtils.format(ConfigUtils.getMessage("command-help")));
                break;
        }

        return true;
    }

    /**
     * Обработка команды проверки статуса
     * @param player Игрок
     */
    private void handleStatus(Player player) {
        if (infectionManager.isInfected(player)) {
            player.sendMessage(ChatUtils.format(ConfigUtils.getMessage("infected")));
            player.sendMessage(ChatUtils.format(ConfigUtils.getMessage(infectionManager.getInfectionStage(player).getMessage())));
        } else {
            player.sendMessage(ChatUtils.format(ConfigUtils.getMessage("not-infected")));
        }
    }

    /**
     * Обработка команды лечения
     * @param sender Отправитель команды
     * @param targetName Имя целевого игрока
     */
    private void handleCure(CommandSender sender, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sender.sendMessage(ChatUtils.format(ConfigUtils.getMessage("player-not-found")));
            return;
        }

        infectionManager.curePlayer(target);
        sender.sendMessage(ChatUtils.format(String.format(ConfigUtils.getMessage("player-cured"), target.getName())));
    }
} 