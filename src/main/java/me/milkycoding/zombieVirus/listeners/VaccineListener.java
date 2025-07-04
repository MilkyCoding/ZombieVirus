package me.milkycoding.zombievirus.listeners;

import me.milkycoding.zombievirus.ZombieVirus;
import me.milkycoding.zombievirus.managers.InfectionManager;
import me.milkycoding.zombievirus.managers.VaccineManager;
import me.milkycoding.zombievirus.utils.ChatUtils;
import me.milkycoding.zombievirus.utils.ConfigUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class VaccineListener implements Listener {
    private final InfectionManager infectionManager;
    private final VaccineManager vaccineManager;

    public VaccineListener() {
        this.infectionManager = ZombieVirus.getInstance().getInfectionManager();
        this.vaccineManager = ZombieVirus.getInstance().getVaccineManager();
    }

    @EventHandler
    public void onVaccineCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        ItemStack result = event.getRecipe().getResult();
        if (vaccineManager.isVaccine(result)) {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage(ChatUtils.format(ConfigUtils.getMessage("vaccine-created")));
        }
    }

    @EventHandler
    public void onVaccineUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item != null && vaccineManager.isVaccine(item)) {
            event.setCancelled(true);
            
            if (infectionManager.isInfected(player)) {
                infectionManager.curePlayer(player);
                player.sendMessage(ChatUtils.format(ConfigUtils.getMessage("vaccine-used")));
                
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
            } else {
                player.sendMessage(ChatUtils.format(ConfigUtils.getMessage("not-infected")));
            }
        }
    }
} 