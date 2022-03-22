package me.sevj6.listener.packet;

import me.sevj6.Instance;
import me.sevj6.util.fileutil.Setting;

public class PacketUtil implements Instance {

    public static final Setting<Integer> increment = Setting.getInt("packet_limit.increment");
    public static final Setting<Integer> decrement = Setting.getInt("packet_limit.decrement");
    public static final Setting<Integer> maxViolations = Setting.getInt("violations.maxVLS");
    public static final Setting<String> kickMessage = Setting.getString("violations.kick_message");

    public static int getIncrementation() {
        return increment.getValue();
    }

    public static int getDecrementation() {
        return decrement.getValue();
    }

    public static int getMaxVls() {
        return maxViolations.getValue();
    }

    public static String getKickMessage() {
        return kickMessage.getValue();
    }


}
