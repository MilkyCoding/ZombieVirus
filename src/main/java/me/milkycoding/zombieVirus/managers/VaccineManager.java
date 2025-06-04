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

/**
 * Менеджер вакцин, отвечающий за создание и использование вакцин.
 */
public class VaccineManager {
    // Ключ для идентификации вакцины в лоре предмета
    private static final String VACCINE_KEY = "zombievirus_vaccine";
    private static final NamespacedKey RECIPE_KEY = new NamespacedKey(ZombieVirus.getInstance(), "vaccine");

    public VaccineManager() {
        registerVaccineRecipe();
    }

    /**
     * Регистрация рецепта вакцины
     */
    private void registerVaccineRecipe() {
        // Проверяем, не зарегистрирован ли уже рецепт
        if (ZombieVirus.getInstance().getServer().getRecipe(RECIPE_KEY) != null) {
            return;
        }

        ItemStack vaccine = createVaccineItem();
        ShapedRecipe recipe = new ShapedRecipe(RECIPE_KEY, vaccine);
        
        // Форма рецепта:
        // SPS
        // RHR
        // SPS
        // S - сахарный тростник
        // P - зелье
        // R - светокамень
        // H - золотое яблоко
        recipe.shape("SPS", "RHR", "SPS");
        recipe.setIngredient('S', Material.SUGAR_CANE);
        recipe.setIngredient('P', Material.POTION);
        recipe.setIngredient('R', Material.GLOWSTONE_DUST);
        recipe.setIngredient('H', Material.GOLDEN_APPLE);
        
        ZombieVirus.getInstance().getServer().addRecipe(recipe);
    }

    /**
     * Создание предмета вакцины
     * @return Предмет вакцины
     */
    private ItemStack createVaccineItem() {
        ItemStack vaccine = new ItemStack(Material.POTION);
        ItemMeta meta = vaccine.getItemMeta();
        
        // Устанавливаем название и описание
        meta.setDisplayName(ChatUtils.color(ConfigUtils.getMessage("vaccine-name")));
        meta.setLore(Arrays.asList(
            ChatUtils.color(ConfigUtils.getMessage("vaccine-lore-1")),
            ChatUtils.color(ConfigUtils.getMessage("vaccine-lore-2")),
            ChatUtils.color("&7" + VACCINE_KEY)
        ));
        
        vaccine.setItemMeta(meta);
        return vaccine;
    }

    /**
     * Проверка, является ли предмет вакциной
     * @param item Предмет для проверки
     * @return true если предмет является вакциной
     */
    public boolean isVaccine(ItemStack item) {
        if (item == null || item.getItemMeta() == null || !item.getItemMeta().hasLore()) return false;
        return item.getItemMeta().getLore().contains(ChatUtils.color("&7" + VACCINE_KEY));
    }

    /**
     * Обработка создания вакцины
     * @param result Созданный предмет
     */
    public void handleVaccineCreation(ItemStack result) {
        if (isVaccine(result)) {
            // TODO: Добавить визуальные эффекты при создании
        }
    }
} 