package me.sevj6.command.commands;

import me.sevj6.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RegearCommand implements TabExecutor {

    private final HashMap<Player, ItemStack[]> map = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp() || isInLocation(player)) {
                ItemStack[] array;
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("set")) {
                        if (map.containsKey(player)) {
                            map.replace(player, player.getInventory().getContents());
                        } else {
                            map.put(player, player.getInventory().getContents());
                        }
                        MessageUtil.sendMessage(player, "&6Updated your regear inventory set");
                        return true;
                    } else {
                        MessageUtil.sendMessage(player, "&4do /reg set");
                    }
                }
                if (map.containsKey(player)) {
                    array = map.get(player);
                } else {
                    array = generateContents();
                }
                player.getInventory().setContents(array);
                player.updateInventory();
                if (player.isOp()) {
                    player.teleport(new Location(Bukkit.getWorld("world_nether"), 0, 15, 0));
                }
            } else {
                player.sendMessage("You are not in range to use /reg");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.singletonList("set");
    }

    public boolean isInLocation(Player player) {
        int range = 20;
        int x = 3813900;
        int y = 69;
        int z = 313207;
        List<Location> possible = new ArrayList<>();
        for (int x1 = x - range; x1 <= x + range; x1++) {
            for (int y1 = y - range; y1 <= y + range; y1++) {
                for (int z1 = z - range; z1 <= z + range; z1++) {
                    Location location = new Location(player.getWorld(), x1, y1, z1);
                    possible.add(location);
                }
            }
        }
        Location p = player.getLocation();
        for (Location location : possible) {
            if (player.getWorld() == location.getWorld()) {
                if (p.getBlockY() == location.getBlockY() && (p.getBlockX() == location.getBlockX() || p.getBlockZ() == location.getBlockZ())) {
                    return true;
                }
            }
        }
        return false;
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
