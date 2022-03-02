package me.sevj6.listeners.illegals.wrapper;

import me.sevj6.listeners.illegals.CheckUtil;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ObjectWrapper<T> extends IllegalCheck {

    private final T type;
    private final Object object;

    public ObjectWrapper(Object object, T type) {
        this.object = object;
        this.type = type;
    }

    public T getType() {
        return type;
    }

    @Override
    public void check() {
        ObjectType objectType = ObjectType.of(this);
        switch (objectType) {
            case INVENTORY:
                CheckUtil.checkInventory((Inventory) this.object);
                break;
            case CONTAINER:
                CheckUtil.checkContainer((Container) this.object);
                break;
            case PLAYER:
                CheckUtil.checkPlayer((Player) this.object);
                break;
            case ITEMSTACK:
                CheckUtil.checkItemStack((ItemStack) this.object);
                break;
            case ENTITYEQUIPMENT:
                CheckUtil.checkEntityEquipment((EntityEquipment) this.object);
                break;
        }
    }

    public enum ObjectType {
        INVENTORY,
        CONTAINER,
        PLAYER,
        ITEMSTACK,
        ENTITYEQUIPMENT;

        public static ObjectType of(ObjectWrapper<?> objWrapper) {
            if (objWrapper == null) return null;
            Object classObj = objWrapper.getType();
            if (Inventory.class.equals(classObj)) {
                return INVENTORY;
            } else if (Container.class.equals(classObj)) {
                return CONTAINER;
            } else if (Player.class.equals(classObj)) {
                return PLAYER;
            } else if (ItemStack.class.equals(classObj)) {
                return ITEMSTACK;
            } else if (EntityEquipment.class.equals(classObj)) {
                return ENTITYEQUIPMENT;
            }
            return null;
        }
    }
}


