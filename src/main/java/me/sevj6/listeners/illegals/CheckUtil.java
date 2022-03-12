package me.sevj6.listeners.illegals;

import me.sevj6.Impurity;
import me.sevj6.listeners.illegals.wrapper.IllegalWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class CheckUtil {

    private static final IllegalWrapper.Strictness strictness = IllegalWrapper.Strictness.valueOf(Impurity.getPlugin().getConfig().getString("IllegalStrictness"));

    public static void checkItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        switch (strictness) {
            case NON_STRICT:
                doNonStrictCheck(itemStack);
                break;
            case SEMI_STRICT:
                doSemiStrictCheck(itemStack);
                break;
            case VERY_STRICT:
                doVeryStrictCheck(itemStack);
                break;
        }
    }

    private static void doNonStrictCheck(ItemStack itemStack) {
        if (isOverEnchanted(itemStack)) revertEnchants(itemStack);
        if (isOverstacked(itemStack)) itemStack.setAmount(itemStack.getMaxStackSize());
        if (isUnbreakable(itemStack)) setNormalDurability(itemStack);
    }

    private static void doSemiStrictCheck(ItemStack itemStack) {
        doNonStrictCheck(itemStack);
        if (hasItemFlags(itemStack)) removeItemFlags(itemStack);
        if (isColoredNamed(itemStack)) removeColorFromName(itemStack);
        if (hasLore(itemStack)) removeLore(itemStack);
    }

    private static void doVeryStrictCheck(ItemStack itemStack) {
        doSemiStrictCheck(itemStack);
        if (hasAttributes(itemStack)) itemStack.setAmount(0);
        if (isNestedShulker(itemStack)) itemStack.setAmount(0);
        if (hasBlockEntityTagIllegally(itemStack)) itemStack.setAmount(0);
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
                if (container instanceof ShulkerBox) {
                    if (content.getItemMeta() instanceof BlockStateMeta && ((BlockStateMeta) content.getItemMeta()).getBlockState() instanceof ShulkerBox) {
                        container.getInventory().remove(content);
                    }
                }
            }
        }
    }

    public static void checkInventory(Inventory inventory) {
        if (inventory == null) return;
        for (ItemStack content : inventory.getContents()) {
            if (content != null) {
                checkItemStack(content);
                if (inventory.getType() == InventoryType.SHULKER_BOX) {
                    if (content.getItemMeta() instanceof BlockStateMeta && ((BlockStateMeta) content.getItemMeta()).getBlockState() instanceof ShulkerBox) {
                        inventory.remove(content);
                    }
                }
            }
        }
    }

    public static boolean isOverEnchanted(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            return itemStack.getEnchantments().entrySet().stream().anyMatch(e -> e.getValue() > e.getKey().getMaxLevel());
        } else return false;
    }

    public static void revertEnchants(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
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

    public static boolean hasItemFlags(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            return itemStack.getItemMeta().getItemFlags().size() > 0 || itemStack.getItemMeta().isUnbreakable();
        }
        return false;
    }

    public static void removeItemFlags(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.removeItemFlags(ItemFlag.values());
        if (meta.isUnbreakable()) {
            meta.setUnbreakable(false);
        }
        itemStack.setItemMeta(meta);
    }

    public static boolean isColoredNamed(ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) return false;
        if (!itemStack.hasItemMeta()) return false;
        ItemMeta meta = itemStack.getItemMeta();
        String rawName = ChatColor.stripColor(meta.getDisplayName());
        String regName = meta.getDisplayName();
        if (rawName == null || regName == null) return false;
        return regName.length() > rawName.length();
    }

    public static void removeColorFromName(ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) return;
        if (!itemStack.hasItemMeta()) return;
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.stripColor(meta.getDisplayName()));
        itemStack.setItemMeta(meta);
    }

    public static boolean hasLore(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return false;
        return itemStack.getItemMeta().hasLore();
    }

    public static void removeLore(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return;
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(null);
        itemStack.setItemMeta(meta);
    }

    public static boolean hasAttributes(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack copy = CraftItemStack.asNMSCopy(itemStack);
        if (copy.hasTag() && copy.getTag() != null) {
            return copy.getTag().hasKey("AttributeModifiers");
        }
        return false;
    }

    public static boolean isNestedShulker(ItemStack itemStack) {
        if (isShulker(itemStack)) {
            ShulkerBox shulkerBox = (ShulkerBox) ((BlockStateMeta) itemStack.getItemMeta()).getBlockState();
            for (ItemStack content : shulkerBox.getInventory().getContents()) {
                if (isShulker(content)) return true;
            }
        }
        return false;
    }

    public static boolean hasBlockEntityTagIllegally(ItemStack itemStack) {
        if (isShulker(itemStack)) return false;
        net.minecraft.server.v1_12_R1.ItemStack copy = CraftItemStack.asNMSCopy(itemStack);
        if (copy.getTag() == null) return false;
        return copy.getTag().hasKey("BlockEntityTag");
    }

    private static boolean isShulker(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.hasItemMeta()) {
                return itemStack.getItemMeta() instanceof BlockStateMeta && ((BlockStateMeta) itemStack.getItemMeta()).getBlockState() instanceof ShulkerBox;
            }
        }
        return false;
    }
}
