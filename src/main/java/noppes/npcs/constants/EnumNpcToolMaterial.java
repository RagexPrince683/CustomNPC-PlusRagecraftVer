package noppes.npcs.constants;

public enum EnumNpcToolMaterial {
    WOOD(0, 59, 2.0F, 0, 15),
    STONE(1, 131, 4.0F, 1, 5),
    BRONZE(2, 170, 5.0F, 2, 15),
    IRON(2, 250, 6.0F, 2, 14),
    DIA(3, 1561, 8.0F, 3, 10),
    GOLD(0, 32, 12.0F, 1, 22),
    EMERALD(3, 1000, 8.0F, 4, 10),
    DEMONIC(3, 100, 8.0F, 6, 10),
    FROST(2, 59, 6.0F, 3, 5),
    MITHRIL(3, 3000, 8.0F, 3, 10);

    /**
     * The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 = STONE, 0 = IRON/GOLD)
     */
    private final int harvestLevel;

    /**
     * The number of uses this material allows. (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
     */
    private final int maxUses;

    /**
     * The strength of this tool material against blocks which it is effective against.
     */
    private final float efficiencyOnProperMaterial;

    /**
     * Damage versus entities.
     */
    private final int damageVsEntity;

    /**
     * Defines the natural enchantability factor of the material.
     */
    private final int enchantability;

    EnumNpcToolMaterial(int par3, int par4, float par5, int par6, int par7) {
        this.harvestLevel = par3;
        this.maxUses = par4;
        this.efficiencyOnProperMaterial = par5;
        this.damageVsEntity = par6;
        this.enchantability = par7;
    }

    /**
     * The number of uses this material allows. (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
     */
    public int getMaxUses() {
        return this.maxUses;
    }

    /**
     * The strength of this tool material against blocks which it is effective against.
     */
    public float getEfficiencyOnProperMaterial() {
        return this.efficiencyOnProperMaterial;
    }

    /**
     * Damage versus entities.
     */
    public int getDamageVsEntity() {
        return this.damageVsEntity;
    }

    /**
     * The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 = STONE, 0 = IRON/GOLD)
     */
    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    /**
     * Return the natural enchantability factor of the material.
     */
    public int getEnchantability() {
        return this.enchantability;
    }
}
