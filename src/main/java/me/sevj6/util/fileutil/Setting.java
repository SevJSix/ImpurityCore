package me.sevj6.util.fileutil;

import me.sevj6.Instance;

import java.util.ArrayList;
import java.util.List;

public class Setting<T> implements Instance {
    private final T defaultValue;
    private T value;

    public Setting(T defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public static Setting<Integer> getInt(String key) {
        for (String settingsKey : settings.getKeys(true)) {
            if (settingsKey.contains(key)) {
                return new Setting<>(settings.getInt(settingsKey));
            }
        }
        return new Setting<>(0);
    }

    public static Setting<Long> getLong(String key) {
        for (String settingsKey : settings.getKeys(true)) {
            if (settingsKey.contains(key)) {
                return new Setting<>(settings.getLong(settingsKey));
            }
        }
        return new Setting<>(0L);
    }

    public static Setting<String> getString(String key) {
        for (String settingsKey : settings.getKeys(true)) {
            if (settingsKey.contains(key)) {
                return new Setting<>(settings.getString(settingsKey));
            }
        }
        return new Setting<>("");
    }

    public static Setting<Boolean> getBoolean(String key) {
        for (String settingsKey : settings.getKeys(true)) {
            if (settingsKey.contains(key)) {
                return new Setting<>(settings.getBoolean(settingsKey));
            }
        }
        return new Setting<>(false);
    }

    public static Setting<Double> getDouble(String key) {
        for (String settingsKey : settings.getKeys(true)) {
            if (settingsKey.contains(key)) {
                return new Setting<>(settings.getDouble(settingsKey));
            }
        }
        return new Setting<>(0D);
    }

    public static Setting<List<String>> getStringList(String key) {
        for (String settingsKey : settings.getKeys(true)) {
            if (settingsKey.contains(key)) {
                return new Setting<>(settings.getStringList(settingsKey));
            }
        }
        return new Setting<>(new ArrayList<>());
    }

    public static Setting<List<Integer>> getIntegerList(String key) {
        for (String settingsKey : settings.getKeys(true)) {
            if (settingsKey.contains(key)) {
                return new Setting<>(settings.getIntegerList(settingsKey));
            }
        }
        return new Setting<>(new ArrayList<>());
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getDefaultValue() {
        return defaultValue;
    }
}
