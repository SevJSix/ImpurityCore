package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.ThreadLocalRandom;

public class SevJ6Regear extends Command {
    public SevJ6Regear(Impurity plugin) {
        super("reg", "&4/reg", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.getName().equalsIgnoreCase("SevJ6")) {
            Player player = (Player) sender;
            player.getInventory().setContents(generateContents());
            player.updateInventory();
            player.teleport(new Location(Bukkit.getWorld("world_nether"), 0, 50, 0));
        }
    }

    private ItemStack[] generateContents() {
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        helmet.addEnchantment(Enchantment.MENDING, 1);
        helmet.addEnchantment(Enchantment.VANISHING_CURSE, 1);

        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        chestplate.addEnchantment(Enchantment.MENDING, 1);
        chestplate.addEnchantment(Enchantment.VANISHING_CURSE, 1);

        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        leggings.addEnchantment(Enchantment.MENDING, 1);
        leggings.addEnchantment(Enchantment.VANISHING_CURSE, 1);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        boots.addEnchantment(Enchantment.DURABILITY, 3);
        boots.addEnchantment(Enchantment.MENDING, 1);
        boots.addEnchantment(Enchantment.VANISHING_CURSE, 1);

        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 3);
        pickaxe.addEnchantment(Enchantment.MENDING, 1);
        pickaxe.addEnchantment(Enchantment.VANISHING_CURSE, 1);

        ItemStack totem = new ItemStack(Material.TOTEM);
        ItemStack exp = new ItemStack(Material.EXP_BOTTLE, 64);
        ItemStack crystal = new ItemStack(Material.END_CRYSTAL, 64);
        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 64, (short) 1);
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 16);
        ItemStack echest = new ItemStack(Material.ENDER_CHEST, 64);
        ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 64);
        ItemStack redstone = new ItemStack(Material.REDSTONE_BLOCK, 64);
        ItemStack dispenser = new ItemStack(Material.DISPENSER, 64);
        ItemStack hopper = new ItemStack(Material.HOPPER, 64);
        ItemStack bed = new ItemStack(Material.BED, 64, (short) ThreadLocalRandom.current().nextInt(0, 16));

        return new ItemStack[]{bed, pickaxe, gapple, crystal, obsidian,
                redstone, dispenser, hopper, gen32kShulker(), gen32kShulker(),
                exp, exp, totem, totem, crystal, pearl, echest, bed, gen32kShulker(),
                boots, leggings, totem, totem, crystal, crystal, bed, bed, gen32kShulker(),
                chestplate, helmet, totem, totem, boots, leggings, chestplate, helmet,
                boots, leggings, chestplate, helmet, totem};
    }

    private ItemStack gen32kShulker() {
        ItemStack shulkerItem = new ItemStack(Material.PURPLE_SHULKER_BOX);
        ItemMeta meta = shulkerItem.getItemMeta();
        BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
        ShulkerBox shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta32k = item.getItemMeta();
        meta32k.setDisplayName("Alpha's Stacked 32k's");
        item.setItemMeta(meta32k);
        item.setAmount(64);
        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 32767);
        item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
        item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 32767);
        item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
        item.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 3);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 32767);
        item.addUnsafeEnchantment(Enchantment.MENDING, 1);
        item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        for (int i = 0; i < shulkerBox.getInventory().getSize(); i++) {
            shulkerBox.getInventory().setItem(i, item);
        }
        blockStateMeta.setBlockState(shulkerBox);
        shulkerItem.setItemMeta(meta);
        return shulkerItem;
    }
}