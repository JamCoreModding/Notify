package io.github.jamalam360.notify.test.metadata;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public record JsonFileModMetadata(String jsonFileUrl) implements ModMetadata {
    @Override
    public boolean containsCustomValue(String key) {
        return key.equals("notify_json");
    }

    @Override
    public boolean containsCustomElement(String key) {
        return key.equals("notify_json");
    }

    @Override
    public CustomValue getCustomValue(String key) {
        return key.equals("notify_json") ? new SimpleCustomValue(jsonFileUrl) : null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getId() {
        return "testmodid";
    }

    @Override
    public Collection<String> getProvides() {
        return null;
    }

    @Override
    public Version getVersion() {
        return null;
    }

    @Override
    public ModEnvironment getEnvironment() {
        return null;
    }

    @Override
    public Collection<ModDependency> getDependencies() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Collection<Person> getAuthors() {
        return null;
    }

    @Override
    public Collection<Person> getContributors() {
        return null;
    }

    @Override
    public ContactInformation getContact() {
        return null;
    }

    @Override
    public Collection<String> getLicense() {
        return null;
    }

    @Override
    public Optional<String> getIconPath(int size) {
        return Optional.empty();
    }

    @Override
    public Map<String, CustomValue> getCustomValues() {
        return null;
    }
}
