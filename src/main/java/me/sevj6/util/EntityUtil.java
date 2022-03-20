package me.sevj6.util;

import me.sevj6.Instance;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.entity.EntityType;

public class EntityUtil extends Utils implements Instance {

    private static final Setting<Integer> animals = Setting.getInt("entities.animals");
    private static final Setting<Integer> mobs = Setting.getInt("entities.mobs");
    private static final Setting<Integer> withers = Setting.getInt("entities.withers");
    private static final Setting<Integer> ender_crystals = Setting.getInt("entities.ender_crystals");
    private static final Setting<Integer> armor_stands = Setting.getInt("entities.armor_stands");
    private static final Setting<Integer> primed_tnt = Setting.getInt("entities.primed_tnt");
    private static final Setting<Integer> wither_skulls = Setting.getInt("entities.wither_skulls");
    private static final Setting<Integer> falling_block = Setting.getInt("entities.falling_block");
    private static final Setting<Integer> dropped_items = Setting.getInt("entities.dropped_items");
    private static final Setting<Boolean> entityLimitEnabled = Setting.getBoolean("entity_per_chunk_limit.enabled");

    public static void setupEntityMap() {
        if (entityLimitEnabled.getValue()) {
            for (EntityType neutralEntity : Utils.neutralEntities) {
                entityMap.put(neutralEntity, animals.getValue());
            }
            for (EntityType hostileEntity : Utils.hostileEntities) {
                entityMap.put(hostileEntity, mobs.getValue());
            }
            entityMap.put(EntityType.WITHER, withers.getValue());
            entityMap.put(EntityType.DROPPED_ITEM, dropped_items.getValue());
            entityMap.put(EntityType.ENDER_CRYSTAL, ender_crystals.getValue());
            entityMap.put(EntityType.ARMOR_STAND, armor_stands.getValue());
            entityMap.put(EntityType.PRIMED_TNT, primed_tnt.getValue());
            entityMap.put(EntityType.WITHER_SKULL, wither_skulls.getValue());
            entityMap.put(EntityType.FALLING_BLOCK, falling_block.getValue());
        }
    }
}
