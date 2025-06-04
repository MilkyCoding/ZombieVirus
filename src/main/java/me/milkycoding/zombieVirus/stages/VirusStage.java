package me.milkycoding.zombievirus.stages;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Перечисление стадий вируса.
 * Определяет эффекты и интервалы чихания для каждой стадии.
 */
public enum VirusStage {
    // Первая стадия - без эффектов, чихание каждые 120 секунд
    STAGE_1(1, 120, new PotionEffect[0]),
    
    // Вторая стадия - без эффектов, чихание каждые 90 секунд
    STAGE_2(2, 90, new PotionEffect[0]),
    
    // Третья стадия - слепота и замедление, чихание каждые 60 секунд
    STAGE_3(3, 60, new PotionEffect[]{
        new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0),
        new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0)
    }),
    
    // Четвертая стадия - слабость III, чихание каждые 30 секунд
    STAGE_4(4, 30, new PotionEffect[]{
        new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 2)
    });

    private final int number;
    private final int sneezeInterval;
    private final PotionEffect[] effects;

    /**
     * Конструктор стадии вируса
     * @param number Номер стадии
     * @param sneezeInterval Интервал чихания в секундах
     * @param effects Эффекты стадии
     */
    VirusStage(int number, int sneezeInterval, PotionEffect[] effects) {
        this.number = number;
        this.sneezeInterval = sneezeInterval;
        this.effects = effects;
    }

    /**
     * Получить номер стадии
     * @return Номер стадии
     */
    public int getNumber() {
        return number;
    }

    /**
     * Получить интервал чихания
     * @return Интервал в секундах
     */
    public int getSneezeInterval() {
        return sneezeInterval;
    }

    /**
     * Получить эффекты стадии
     * @return Массив эффектов
     */
    public PotionEffect[] getEffects() {
        return effects;
    }

    /**
     * Получить следующую стадию
     * @return Следующая стадия или null, если текущая стадия последняя
     */
    public VirusStage getNextStage() {
        VirusStage[] stages = values();
        int nextIndex = ordinal() + 1;
        return nextIndex < stages.length ? stages[nextIndex] : null;
    }

    /**
     * Получить ключ сообщения для стадии
     * @return Ключ сообщения
     */
    public String getMessage() {
        return "stage-" + number;
    }
} 