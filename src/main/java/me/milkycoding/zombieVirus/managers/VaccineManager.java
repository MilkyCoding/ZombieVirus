package me.milkycoding.zombievirus.managers;

import me.milkycoding.zombievirus.ZombieVirus;
import me.milkycoding.zombievirus.utils.ChatUtils;
import me.milkycoding.zombievirus.utils.ConfigUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

public class VaccineManager {
    private static final String VACCINE_KEY = "zombievirus_vaccine";
    private static final NamespacedKey RECIPE_KEY = new NamespacedKey(ZombieVirus.getInstance(), "vaccine");

    public VaccineManager() {
        registerVaccineRecipe();
    }

    private void registerVaccineRecipe() {
        if (ZombieVirus.getInstance().getServer().getRecipe(RECIPE_KEY) != null) {
            return;
        }

        ItemStack vaccine = createVaccineItem();
        ShapedRecipe recipe = new ShapedRecipe(RECIPE_KEY, vaccine);
        
        recipe.shape("SPS", "RHR", "SPS");
        recipe.setIngredient('S', Material.SUGAR_CANE);
        recipe.setIngredient('P', Material.POTION);
        recipe.setIngredient('R', Material.GLOWSTONE_DUST);
        recipe.setIngredient('H', Material.GOLDEN_APPLE);
        
        ZombieVirus.getInstance().getServer().addRecipe(recipe);
    }

    private ItemStack createVaccineItem() {
        ItemStack vaccine = new ItemStack(Material.POTION);
        ItemMeta meta = vaccine.getItemMeta();
        
        assert meta != null;
        meta.setDisplayName(ChatUtils.color(ConfigUtils.getMessage("vaccine-name")));
        meta.setLore(Arrays.asList(
            ChatUtils.color(ConfigUtils.getMessage("vaccine-lore-1")),
            ChatUtils.color(ConfigUtils.getMessage("vaccine-lore-2")),
            ChatUtils.color("&7" + VACCINE_KEY)
        ));
        
        vaccine.setItemMeta(meta);
        return vaccine;
    }

    public boolean isVaccine(ItemStack item) {
        if (item == null || item.getItemMeta() == null || !item.getItemMeta().hasLore()) return false;
        return Objects.requireNonNull(item.getItemMeta().getLore()).contains(ChatUtils.color("&7" + VACCINE_KEY));
    }
} 