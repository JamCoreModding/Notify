package io.github.jamalam360.notify.test;

import io.github.jamalam360.notify.resolver.api.ModrinthApiResolver;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Jamalam360
 */
public class ModrinthApiTests {
    private final ModrinthApiResolver resolver = new ModrinthApiResolver();

    private final ModMetadata lambdynamiclights = new HomepageModMetadata("https://modrinth.com/mod/lambdynamiclights");
    private final ModMetadata fabricapi = new HomepageModMetadata("https://modrinth.com/mod/fabric-api");
    private final ModMetadata dashloader = new HomepageModMetadata("https://modrinth.com/mod/dashloader");

    /**
     * Check whether everything works under ideal conditions
     */
    @Test
    public void testResolveLatestVersionNormal() throws Exception {
        assertEquals("1.3.4+1.16", resolver.resolveLatestVersion(lambdynamiclights, "1.16.2").getFriendlyString());
        assertEquals("0.2.7+build.127", resolver.resolveLatestVersion(fabricapi, "1.14").getFriendlyString());
        assertEquals("1.4.5-stable", resolver.resolveLatestVersion(dashloader, "1.16.5").getFriendlyString());
    }

    /**
     * Check the parsing works on snapshot and RC version numbering.
     */
    @Test
    public void testResolveLatestVersionSnapshots() throws Exception {
        assertEquals("0.1.0.46", resolver.resolveLatestVersion(fabricapi, "18w49a").getFriendlyString());
        assertEquals("1.5", resolver.resolveLatestVersion(dashloader, "1.17-pre5").getFriendlyString());
        assertEquals("1.5", resolver.resolveLatestVersion(dashloader, "1.17-rc1").getFriendlyString());
    }

    /**
     * Checking the regex and parsing works as expected.
     */
    @Test
    public void testResolveLatestVersionParsing() throws Exception {
        assertEquals("1.3.4+1.16", resolver.resolveLatestVersion(lambdynamiclights, "1.16.2").getFriendlyString()); // author left a trailing slash
    }
}
