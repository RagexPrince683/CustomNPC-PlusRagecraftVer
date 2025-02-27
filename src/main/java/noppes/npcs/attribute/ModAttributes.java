package noppes.npcs.attribute;

/**
 * ModAttributes registers the core attributes (both non–magic and magic–based).
 */
public class ModAttributes {
    public static final String HEALTH_KEY = "health";
    public static final String MAIN_ATTACK_KEY = "main_attack";
    public static final String MAIN_ATTACK_BOOST_KEY = "main_attack_boost";
    public static final String NEUTRAL_KEY = "neutral";
    public static final String NEUTRAL_BOOST_KEY = "neutral_boost";
    public static final String CRITICAL_DAMAGE_KEY = "critical_damage";
    public static final String CRITICAL_CHANCE_KEY = "critical_chance";
    public static final String MAGIC_DAMAGE_KEY = "magic_damage";
    public static final String MAGIC_BOOST_KEY = "magic_boost";
    public static final String MAGIC_DEFENSE_KEY = "magic_defense";
    public static final String MAGIC_RESISTANCE_KEY = "magic_resistance";


    public static AttributeDefinition MAIN_ATTACK;
    public static AttributeDefinition MAIN_BOOST;
    public static AttributeDefinition NEUTRAL_ATTACK;
    public static AttributeDefinition NEUTRAL_BOOST;

    public static AttributeDefinition HEALTH;
    public static AttributeDefinition CRITICAL_CHANCE;
    public static AttributeDefinition CRITICAL_DAMAGE;

    public static AttributeDefinition MAGIC_DAMAGE;
    public static AttributeDefinition MAGIC_BOOST;
    public static AttributeDefinition MAGIC_DEFENSE;
    public static AttributeDefinition MAGIC_RESISTANCE;

    public ModAttributes(){
        HEALTH = AttributeController.registerAttribute(HEALTH_KEY, "Health", AttributeValueType.FLAT, AttributeDefinition.AttributeSection.BASE);

        MAIN_ATTACK = AttributeController.registerAttribute(MAIN_ATTACK_KEY, "Main Attack Damage", AttributeValueType.FLAT, AttributeDefinition.AttributeSection.BASE);
        MAIN_BOOST = AttributeController.registerAttribute(MAIN_ATTACK_BOOST_KEY, "Main Attack Damage (%)", AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);

        NEUTRAL_ATTACK = AttributeController.registerAttribute(NEUTRAL_KEY, "Neutral Damage", AttributeValueType.FLAT, AttributeDefinition.AttributeSection.BASE);
        NEUTRAL_BOOST = AttributeController.registerAttribute(NEUTRAL_BOOST_KEY, "Neutral Damage (%)", AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);

        CRITICAL_CHANCE = AttributeController.registerAttribute(CRITICAL_CHANCE_KEY, "Critical Chance (%)", AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.INFO);
        CRITICAL_DAMAGE = AttributeController.registerAttribute(CRITICAL_DAMAGE_KEY, "Critical Damage Bonus", AttributeValueType.FLAT, AttributeDefinition.AttributeSection.INFO);

        MAGIC_DAMAGE = AttributeController.registerAttribute(MAGIC_DAMAGE_KEY, "Magic Damage", AttributeValueType.MAGIC, AttributeDefinition.AttributeSection.BASE);
        MAGIC_BOOST = AttributeController.registerAttribute(MAGIC_BOOST_KEY, "Magic Damage (%)", AttributeValueType.MAGIC, AttributeDefinition.AttributeSection.MODIFIER);

        MAGIC_DEFENSE = AttributeController.registerAttribute(MAGIC_DEFENSE_KEY, "Magic Defense", AttributeValueType.MAGIC, AttributeDefinition.AttributeSection.BASE);
        MAGIC_RESISTANCE = AttributeController.registerAttribute(MAGIC_RESISTANCE_KEY, "Magic Resistance (%)", AttributeValueType.MAGIC, AttributeDefinition.AttributeSection.MODIFIER);
    }
}
