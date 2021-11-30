/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jamalam360
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360.notify.test;

import io.github.jamalam360.notify.resolver.api.GradlePropertiesResolver;
import io.github.jamalam360.notify.test.metadata.GradlePropertiesModMetadata;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GradlePropertiesTests {
    private final GradlePropertiesResolver resolver = new GradlePropertiesResolver();

    private final ModMetadata test1 = new GradlePropertiesModMetadata("https://raw.githubusercontent.com/JamCoreModding/NotifyTesting/main/gradle.properties/1.properties", "mod_version");
    private final ModMetadata test2 = new GradlePropertiesModMetadata("https://raw.githubusercontent.com/JamCoreModding/NotifyTesting/main/gradle.properties/2.properties", "mod_version");
    private final ModMetadata test3 = new GradlePropertiesModMetadata("https://raw.githubusercontent.com/JamCoreModding/NotifyTesting/main/gradle.properties/3.properties", "mod_version");
    private final ModMetadata test4 = new GradlePropertiesModMetadata("https://raw.githubusercontent.com/JamCoreModding/NotifyTesting/main/gradle.properties/4.properties", "mod_version");
    private final ModMetadata test5 = new GradlePropertiesModMetadata("https://raw.githubusercontent.com/JamCoreModding/NotifyTesting/main/gradle.properties/5.properties", "different_version_key");

    /**
     * Check whether everything works under ideal conditions
     */
    @Test
    public void testResolveLatestVersionNormal() throws Exception {
        assertEquals("1.1.0", resolver.resolveLatestVersion(test1, "1.16.5").getFriendlyString()); // Ideal conditions
        assertEquals("1.1.0", resolver.resolveLatestVersion(test5, "20w41a").getFriendlyString()); // Different version key
    }

    /**
     * gradle.properties resolve-ment should be Minecraft version independent
     */
    @Test
    public void testResolveLatestVersionVariedMinecraft() throws Exception {
        assertEquals("1.1.0", resolver.resolveLatestVersion(test1, "1.100.1000").getFriendlyString()); // Invalid Minecraft version shouldn't change output
        assertEquals("1.1.0", resolver.resolveLatestVersion(test5, "").getFriendlyString()); // Invalid Minecraft version shouldn't change output
    }

    /**
     * Checking the regex and parsing works as expected.
     */
    @Test
    public void testResolveLatestVersionParsing() throws Exception {
        assertEquals("1.1.0", resolver.resolveLatestVersion(test2, "1.16.5").getFriendlyString()); // gradle.properties has spaces inbetween key values
        assertEquals("1.1.0", resolver.resolveLatestVersion(test3, "1.16.5").getFriendlyString()); // gradle.properties leading spaces
        assertEquals("1.1.0", resolver.resolveLatestVersion(test4, "1.16.5").getFriendlyString()); // gradle.properties trailing spaces
    }
}
