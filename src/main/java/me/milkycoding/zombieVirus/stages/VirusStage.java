package me.milkycoding.zombievirus.stages;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum VirusStage {
    STAGE_1(1, 120, new PotionEffect[0]),
    STAGE_2(2, 90, new PotionEffect[0]),
    STAGE_3(3, 60, new PotionEffect[]{
        new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0),
        new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0)
    }),
    STAGE_4(4, 30, new PotionEffect[]{
        new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 2)
    });

    private final int number;
    private final int sneezeInterval;
    private final PotionEffect[] effects;

    VirusStage(int number, int sneezeInterval, PotionEffect[] effects) {
        this.number = number;
        this.sneezeInterval = sneezeInterval;
        this.effects = effects;
    }

    public int getNumber() {
        return number;
    }

    public int getSneezeInterval() {
        return sneezeInterval;
    }

    public PotionEffect[] getEffects() {
        return effects;
    }

    public VirusStage getNextStage() {
        VirusStage[] stages = values();
        int nextIndex = ordinal() + 1;
        return nextIndex < stages.length ? stages[nextIndex] : null;
    }

    public String getMessage() {
        return "stage-" + number;
    }
} 