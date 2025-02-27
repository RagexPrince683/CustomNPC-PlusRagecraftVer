package noppes.npcs.attribute;

import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.attribute.player.PlayerAttributeTracker;
import noppes.npcs.controllers.MagicController;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.Map;
import java.util.Random;

import static noppes.npcs.attribute.AttributeController.getTracker;

public class AttributeAttackHelper {
    private static final Random random = new Random();

    public static float calculateDamagePlayer(EntityPlayer attacker, EntityPlayer defender, float base){
        return calculateDamagePlayer(getTracker(attacker), getTracker(defender), base);
    }

    public static float calculateDamagePlayer(PlayerAttributeTracker attacker, PlayerAttributeTracker defender, float baseDamage) {

        // Calculate physical damage
        float mainAttack = attacker.getAttributeValue(ModAttributes.MAIN_ATTACK);
        float mainBoost = attacker.getAttributeValue(ModAttributes.MAIN_BOOST) + 1;
        float physicalDamage = (baseDamage * mainBoost) + mainAttack;

        // Calculate neutral damage
        float neutralDamage = attacker.getAttributeValue(ModAttributes.NEUTRAL_ATTACK);
        float neutralBoost = attacker.getAttributeValue(ModAttributes.NEUTRAL_BOOST) + 1;
        float neutralTotal = neutralDamage * neutralBoost;

        // Calculate magic damage
        float magicTotal = 0.0f;
        MagicController magicController = MagicController.getInstance();
        for (Map.Entry<Integer, Float> entry : attacker.magicDamage.entrySet()) {
            int magicId = entry.getKey();
            if (magicController.getMagic(magicId) != null) {
                float attackMagic = entry.getValue();
                float defenseMagic = defender.magicDefense.getOrDefault(magicId, 0.0f);
                float effectiveMagic = attackMagic - defenseMagic;

                float boostMagic = attacker.magicBoost.getOrDefault(magicId, 0.0f);
                float resistanceMagic = defender.magicResistance.getOrDefault(magicId, 0.0f);
                float effectiveBoost = boostMagic - resistanceMagic;

                float magicDamageForMagic = effectiveMagic * (effectiveBoost + 1);
                // Ensure magic damage doesn't drop below 0 for this magic
                magicTotal += Math.max(0, magicDamageForMagic);
            }
        }

        // Sum all damage components
        float totalDamage = physicalDamage + neutralTotal + magicTotal;

        // Critical hit: if critical, float damage and add bonus
        float criticalChance = attacker.getAttributeValue(ModAttributes.CRITICAL_CHANCE);
        float criticalBonus = attacker.getAttributeValue(ModAttributes.CRITICAL_DAMAGE);
        if (random.nextFloat() < (criticalChance / 100)) {
            totalDamage = (totalDamage * 2) + criticalBonus;
        }

        return totalDamage;
    }

    public static float calculateDamageNPC(PlayerAttributeTracker attacker, EntityNPCInterface defender, float baseDamage) {

        // Calculate physical damage
        float mainAttack = attacker.getAttributeValue(ModAttributes.MAIN_ATTACK);
        float mainBoost = attacker.getAttributeValue(ModAttributes.MAIN_BOOST) + 1;
        float physicalDamage = (baseDamage * mainBoost) + mainAttack;

        // Calculate neutral damage
        float neutralDamage = attacker.getAttributeValue(ModAttributes.NEUTRAL_ATTACK);
        float neutralBoost = attacker.getAttributeValue(ModAttributes.NEUTRAL_BOOST) + 1;
        float neutralTotal = neutralDamage * neutralBoost;

        // Calculate magic damage
//        float magicTotal = 0.0;
//        MagicController magicController = MagicController.getInstance();
//        for (Map.Entry<Integer, Float> entry : attacker.magicDamage.entrySet()) {
//            int magicId = entry.getKey();
//            if (magicController.getMagic(magicId) != null) {
//                float attackMagic = entry.getValue();
//                float defenseMagic = defender.magicDefense.getOrDefault(magicId, 0.0);
//                float effectiveMagic = attackMagic - defenseMagic;
//
//                float boostMagic = attacker.magicBoost.getOrDefault(magicId, 0.0);
//                float resistanceMagic = defender.magicResistance.getOrDefault(magicId, 0.0);
//                float effectiveBoost = boostMagic - resistanceMagic;
//
//                float magicDamageForMagic = effectiveMagic * (effectiveBoost + 1);
//                // Ensure magic damage doesn't drop below 0 for this magic
//                magicTotal += Math.max(0, magicDamageForMagic);
//            }
//        }
//
//        // Sum all damage components
//        float totalDamage = physicalDamage + neutralTotal + magicTotal;
//
//        // Critical hit: if critical, float damage and add bonus
//        float criticalChance = attacker.getAttributeValue(ModAttributes.CRITICAL_CHANCE);
//        float criticalBonus = attacker.getAttributeValue(ModAttributes.CRITICAL_DAMAGE);
//        if (random.nextFloat() < (criticalChance / 100)) {
//            totalDamage = (totalDamage * 2) + criticalBonus;
//        }

        return 0;
    }

    /**
     * Calculates a player's total damage when attacking an opponent with no defense.
     * In this calculation, any defensive modifiers (such as magic defense or resistance)
     * are assumed to be zero.
     *
     * @param attacker   The attacker's attribute tracker.
     * @return The total calculated damage.
     */
    public static float calculateMaximumOutput(PlayerAttributeTracker attacker) {
        // Neutral damage remains unchanged.
        float neutralDamage = attacker.getAttributeValue(ModAttributes.NEUTRAL_ATTACK);
        float neutralBoost = attacker.getAttributeValue(ModAttributes.NEUTRAL_BOOST) + 1;
        float neutralTotal = neutralDamage * neutralBoost;

        // For magic damage, we ignore any defensive values.
        float magicTotal = 0.0f;
        MagicController magicController = MagicController.getInstance();
        for (Map.Entry<Integer, Float> entry : attacker.magicDamage.entrySet()) {
            int magicId = entry.getKey();
            if (magicController.getMagic(magicId) != null) {
                float attackMagic = entry.getValue();
                // No defender, so effective magic equals attack magic.
                float effectiveMagic = attackMagic;

                float boostMagic = attacker.magicBoost.getOrDefault(magicId, 0.0f);
                // No resistance present.
                float effectiveBoost = boostMagic;

                float magicDamageForMagic = effectiveMagic * (effectiveBoost + 1);
                magicTotal += Math.max(0, magicDamageForMagic);
            }
        }
        return neutralTotal + magicTotal;
    }

    public static float calculateOutgoing(EntityPlayer entityPlayer, float baseDamage) {
        PlayerAttributeTracker attacker = getTracker(entityPlayer);

        float mainAttack = attacker.getAttributeValue(ModAttributes.MAIN_ATTACK);
        float mainBoost = attacker.getAttributeValue(ModAttributes.MAIN_BOOST) + 1;
        float physicalDamage = (baseDamage * mainBoost) + mainAttack;
        return attacker.maximumOutput + physicalDamage;
    }
}
