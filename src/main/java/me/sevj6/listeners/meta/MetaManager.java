package me.sevj6.listeners.meta;

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

}
