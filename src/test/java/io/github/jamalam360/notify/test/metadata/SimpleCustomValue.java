package io.github.jamalam360.notify.test.metadata;

import net.fabricmc.loader.api.metadata.CustomValue;

public record SimpleCustomValue(String value) implements CustomValue {
    @Override
    public String getAsString() {
        return value();
    }

    @Override
    public CvType getType() {
        return CvType.STRING;
    }

    @Override
    public CvObject getAsObject() {
        return null;
    }

    @Override
    public CvArray getAsArray() {
        return null;
    }

    @Override
    public Number getAsNumber() {
        return null;
    }

    @Override
    public boolean getAsBoolean() {
        return false;
    }
}
