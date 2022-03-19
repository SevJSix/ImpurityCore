package me.sevj6.listener.packet;

import me.sevj6.Instance;
import me.sevj6.util.fileutil.Setting;
import net.minecraft.server.v1_12_R1.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PacketLimit implements Instance {

    public static final Setting<List<Integer>> useEntityVLID = Setting.getIntegerList("use_entity.vlID");
    public static final Setting<List<Integer>> windowClickVLID = Setting.getIntegerList("window_click.vlID");
    public static final Setting<List<Integer>> blockDigVLID = Setting.getIntegerList("block_dig.vlID");
    public static final Setting<List<Integer>> blockPlaceVLID = Setting.getIntegerList("block_place.vlID");
    public static final Setting<List<Integer>> autoRecipeVLID = Setting.getIntegerList("auto_recipe.vlID");
    public static final Setting<List<Integer>> teleportAcceptVLID = Setting.getIntegerList("teleport_accept.vlID");
    public static final Setting<Integer> useEntityMax = Setting.getInt("use_entity.maxVLS");
    public static final Setting<Integer> windowClickMax = Setting.getInt("window_click.maxVLS");
    public static final Setting<Integer> blockDigMax = Setting.getInt("block_dig.maxVLS");
    public static final Setting<Integer> blockPlaceMax = Setting.getInt("block_place.maxVLS");
    public static final Setting<Integer> autoRecipeMax = Setting.getInt("auto_recipe.maxVLS");
    public static final Setting<Integer> teleportAcceptMax = Setting.getInt("teleport_accept.maxVLS");

    public static int getMaxVLS(Class<?> clazz) {
        if (getViolationMap(clazz) != null) {
            for (Map.Entry<int[], Integer> integerEntry : Objects.requireNonNull(getViolationMap(clazz)).entrySet()) {
                return integerEntry.getValue();
            }
        }
        return 0;
    }

    public static int getIncrementation(Class<?> clazz) {
        if (getViolationMap(clazz) != null) {
            for (Map.Entry<int[], Integer> integerEntry : Objects.requireNonNull(getViolationMap(clazz)).entrySet()) {
                return integerEntry.getKey()[0];
            }
        }
        return 0;
    }

    public static int getDecrementation(Class<?> clazz) {
        if (getViolationMap(clazz) != null) {
            for (Map.Entry<int[], Integer> integerEntry : Objects.requireNonNull(getViolationMap(clazz)).entrySet()) {
                return integerEntry.getKey()[1];
            }
        }
        return 0;
    }

    public static HashMap<int[], Integer> getViolationMap(Class<?> clazz) {
        Packets packets = Packets.getType(clazz);
        if (packets != null) {
            int[] array;
            int max;
            HashMap<int[], Integer> map = new HashMap<>();
            switch (packets) {
                case WINDOW_CLICK:
                    array = new int[]{windowClickVLID.getValue().get(0), windowClickVLID.getValue().get(1)};
                    max = windowClickMax.getValue();
                    break;
                case TELEPORT_ACCEPT:
                    array = new int[]{teleportAcceptVLID.getValue().get(0), teleportAcceptVLID.getValue().get(1)};
                    max = teleportAcceptMax.getValue();
                    break;
                case BLOCK_PLACE:
                    array = new int[]{blockPlaceVLID.getValue().get(0), blockPlaceVLID.getValue().get(1)};
                    max = blockPlaceMax.getValue();
                    break;
                case BLOCK_DIG:
                    array = new int[]{blockDigVLID.getValue().get(0), blockDigVLID.getValue().get(1)};
                    max = blockDigMax.getValue();
                    break;
                case AUTO_RECIPE:
                    array = new int[]{autoRecipeVLID.getValue().get(0), autoRecipeVLID.getValue().get(1)};
                    max = autoRecipeMax.getValue();
                    break;
                case USE_ENTITY:
                    array = new int[]{useEntityVLID.getValue().get(0), useEntityVLID.getValue().get(1)};
                    max = useEntityMax.getValue();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + packets);
            }
            map.put(array, max);
            return map;
        }
        return null;
    }

    public enum Packets {
        WINDOW_CLICK,
        TELEPORT_ACCEPT,
        BLOCK_PLACE,
        BLOCK_DIG,
        AUTO_RECIPE,
        USE_ENTITY;

        public static Packets getType(Class<?> clazz) {
            if (PacketPlayInWindowClick.class.equals(clazz)) return WINDOW_CLICK;
            if (PacketPlayInTeleportAccept.class.equals(clazz)) return TELEPORT_ACCEPT;
            if (PacketPlayInBlockPlace.class.equals(clazz)) return BLOCK_PLACE;
            if (PacketPlayInBlockDig.class.equals(clazz)) return BLOCK_DIG;
            if (PacketPlayInAutoRecipe.class.equals(clazz)) return AUTO_RECIPE;
            if (PacketPlayInUseEntity.class.equals(clazz)) return USE_ENTITY;
            else return null;
        }
    }
}
