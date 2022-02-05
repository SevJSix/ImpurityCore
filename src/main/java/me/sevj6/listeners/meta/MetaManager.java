package me.sevj6.listeners.meta;

import java.time.LocalDateTime;

public class MetaManager {

    private final MetaSettings settings;

    public MetaManager(MetaType type) {
        settings = new MetaSettings();
        switch (type) {
            case NO_OFFHAND_32K:
                settings.setMainhandOnly(true);
                settings.setIs32kEnabled(true);
                settings.setFastCA(false);
                break;
            case OFFHAND_32K:
                settings.setMainhandOnly(false);
                settings.setIs32kEnabled(true);
                settings.setFastCA(false);
                break;
            case NO_32k:
                settings.setMainhandOnly(true);
                settings.setIs32kEnabled(false);
                settings.setFastCA(false);
                break;
            case FAST_CA:
                settings.setMainhandOnly(false);
                settings.setIs32kEnabled(false);
                settings.setFastCA(true);
                break;
            case ALL_METAS:
                settings.setMainhandOnly(false);
                settings.setFastCA(true);
                settings.setIs32kEnabled(true);
                break;
        }
    }

    public MetaSettings getSettings() {
        return settings;
    }

    public static MetaType getTypeAccordingToDay() {
        LocalDateTime time = LocalDateTime.now();
        MetaType type;
        switch (time.getDayOfWeek()) {
            case MONDAY:
                type = MetaType.NO_OFFHAND_32K;
                break;
            case TUESDAY:
                type = MetaType.OFFHAND_32K;
                break;
            case WEDNESDAY:
                type = MetaType.FAST_CA;
                break;
            case THURSDAY:
                type = MetaType.NO_32k;
                break;
            case FRIDAY:
            case SATURDAY:
            case SUNDAY:
                type = MetaType.ALL_METAS;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + time.getDayOfWeek());
        }
        return type;
    }

    public static String getTablistPlaceholder() {
        StringBuilder builder = new StringBuilder();
        switch (getTypeAccordingToDay()) {
            case NO_OFFHAND_32K:
                builder.append("Mainhand CA Only With 32k's Enabled");
                break;
            case OFFHAND_32K:
                builder.append("Offhand CA Allowed With 32k's Enabled");
                break;
            case FAST_CA:
                builder.append("Fast Server-Sided CA Meta With 32k's Disabled");
                break;
            case NO_32k:
                builder.append("Mainhand CA Only With 32k's Disabled");
                break;
            case ALL_METAS:
                builder.append("All Metas Enabled (Fast CA, 32k's, Offhand Allowed)");
                break;
        }
        return builder.toString();
    }
}
