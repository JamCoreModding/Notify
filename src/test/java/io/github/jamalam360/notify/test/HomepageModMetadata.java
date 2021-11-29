package io.github.jamalam360.notify.test;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Simple implementation of ModMetadata with only a homepage field present, to test the Modrinth and CurseForge API compatibility.
 *
 * @author Jamalam360
 */
public record HomepageModMetadata(String homepage) implements ModMetadata {
    @Override
    public ContactInformation getContact() {
        return new ContactInformation() {
            @Override
            public Optional<String> get(String key) {
                if (key.equals("homepage")) {
                    return Optional.of(HomepageModMetadata.this.homepage);
                }

                return Optional.empty();
            }

            @Override
            public Map<String, String> asMap() {
                return null;
            }
        };
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getId() {
        return null;
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
    public Collection<String> getLicense() {
        return null;
    }

    @Override
    public Optional<String> getIconPath(int size) {
        return Optional.empty();
    }

    @Override
    public boolean containsCustomValue(String key) {
        return false;
    }

    @Override
    public CustomValue getCustomValue(String key) {
        return null;
    }

    @Override
    public Map<String, CustomValue> getCustomValues() {
        return null;
    }

    @Override
    public boolean containsCustomElement(String key) {
        return false;
    }
}
