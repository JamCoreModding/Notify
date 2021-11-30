package io.github.jamalam360.notify.resolver.api;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

import java.io.IOException;

public interface ModVersionResolver {
    Version getLatestVersion(ModContainer mod, String minecraftVersion) throws IOException, VersionParsingException;
}
