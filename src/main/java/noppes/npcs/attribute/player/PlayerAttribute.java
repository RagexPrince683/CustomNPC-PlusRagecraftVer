package noppes.npcs.attribute.player;


import noppes.npcs.attribute.AttributeDefinition;
import noppes.npcs.attribute.IAttributeInstance;

public class PlayerAttribute implements IAttributeInstance {
    private final AttributeDefinition attribute;
    private double baseValue;

    public PlayerAttribute(AttributeDefinition attribute, double baseValue) {
        this.attribute = attribute;
        this.baseValue = baseValue;
    }

    @Override
    public AttributeDefinition getAttribute() {
        return attribute;
    }

    @Override
    public double getValue() {
        return baseValue;
    }

    @Override
    public void setValue(double value) {
        this.baseValue = value;
    }
}
