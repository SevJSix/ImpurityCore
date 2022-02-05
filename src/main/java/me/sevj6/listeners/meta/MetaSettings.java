package me.sevj6.listeners.meta;

public class MetaSettings {

    private boolean isMainhandOnly;
    private boolean isFastCA;
    private boolean is32kEnabled;

    public boolean isMainhandOnly() {
        return isMainhandOnly;
    }

    public void setMainhandOnly(boolean mainhandOnly) {
        isMainhandOnly = mainhandOnly;
    }

    public boolean isFastCA() {
        return isFastCA;
    }

    public void setFastCA(boolean fastCA) {
        isFastCA = fastCA;
    }

    public boolean is32kEnabled() {
        return is32kEnabled;
    }

    public void setIs32kEnabled(boolean is32kEnabled) {
        this.is32kEnabled = is32kEnabled;
    }
}
