package me.sevj6.listeners.illegals;

import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CheckUtil {

    public static void checkItemStack(ItemStack itemStack) {
        if (itemStack == null) return;
        if (isOverEnchanted(itemStack)) revertEnchants(itemStack);
        if (isOverstacked(itemStack)) itemStack.setAmount(itemStack.getMaxStackSize());
        if (isUnbreakable(itemStack)) setNormalDurability(itemStack);
    }

    public static void checkPlayer(Player player) {
        if (player == null) return;
        for (ItemStack content : player.getInventory().getContents()) {
            if (content != null) {
                checkItemStack(content);
            }
        }
    }

    public static void checkEntityEquipment(EntityEquipment entityEquipment) {
        if (entityEquipment == null) return;
        for (ItemStack armorContent : entityEquipment.getArmorContents()) {
            if (armorContent != null) {
                checkItemStack(armorContent);
            }
        }
    }

    public static void checkContainer(Container container) {
        if (container == null) return;
        for (ItemStack content : container.getInventory().getContents()) {
            if (content != null) {
                checkItemStack(content);
            }
        }
    }

    public static void checkInventory(Inventory inventory) {
        if (inventory == null) return;
        for (ItemStack content : inventory.getContents()) {
            if (content != null) {
                checkItemStack(content);
            }
        }
    }

    public static boolean isOverEnchanted(ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta()) {
            return itemStack.getEnchantments().entrySet().stream().anyMatch(e -> e.getValue() > e.getKey().getMaxLevel());
        } else return false;
    }

    public static void revertEnchants(ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta()) {
            itemStack.getEnchantments().forEach((enchantment, integer) -> {
                itemStack.removeEnchantment(enchantment);
                itemStack.addEnchantment(enchantment, enchantment.getMaxLevel());
            });
        }
    }

    public static boolean isOverstacked(ItemStack itemStack) {
        if (itemStack.getType() == Material.BED) return false;
        return itemStack.getAmount() > itemStack.getMaxStackSize();
    }

    public static boolean isUnbreakable(ItemStack itemStack) {
        return ((itemStack.getType().getMaxDurability() > 50) && (itemStack.getDurability() < 0 || itemStack.getDurability() > itemStack.getType().getMaxDurability()));
    }

    public static void setNormalDurability(ItemStack itemStack) {
        itemStack.setDurability(itemStack.getType().getMaxDurability());
    }

    public static boolean isIllegal(ItemStack itemStack) {
        return isOverstacked(itemStack) || isOverEnchanted(itemStack) || isUnbreakable(itemStack);
    }
}
