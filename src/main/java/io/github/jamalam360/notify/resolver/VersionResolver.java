package io.github.jamalam360.notify.resolver;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.io.IOException;

/**
 * Represents a class that gets the latest version of a mod based on metadata from a fabric.mod.json
 * @author Jamalam360
 */
public interface VersionResolver {
    /**
     * @param metadata the metadata to check
     * @return true if the metadata contains the necessary information to use this resolver
     */
    boolean canResolve(ModMetadata metadata);

    /**
     * @param metadata the metadata to use to resolve the version
     * @param minecraftVersion the version of Minecraft to find the latest version for
     * @return the latest version of the mod for the specified Minecraft version
     */
    Version resolveLatestVersion(ModMetadata metadata, String minecraftVersion) throws VersionParsingException, IOException;
}
