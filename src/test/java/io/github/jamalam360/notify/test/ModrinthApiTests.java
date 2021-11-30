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

import io.github.jamalam360.notify.resolver.api.ModrinthApiResolver;
import io.github.jamalam360.notify.test.metadata.HomepageModMetadata;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Jamalam360
 */
public class ModrinthApiTests {
    private final ModrinthApiResolver resolver = new ModrinthApiResolver();

    private final ModMetadata test1 = new HomepageModMetadata("https://modrinth.com/mod/lambdynamiclights");
    private final ModMetadata test2 = new HomepageModMetadata("https://modrinth.com/mod/fabric-api");
    private final ModMetadata test3 = new HomepageModMetadata("https://modrinth.com/mod/dashloader");

    /**
     * Check whether everything works under ideal conditions
     */
    @Test
    public void testResolveLatestVersionNormal() throws Exception {
        assertEquals("1.3.4+1.16", resolver.resolveLatestVersion(test1, "1.16.2").getFriendlyString());
        assertEquals("0.2.7+build.127", resolver.resolveLatestVersion(test2, "1.14").getFriendlyString());
        assertEquals("1.4.5-stable", resolver.resolveLatestVersion(test3, "1.16.5").getFriendlyString());
    }

    /**
     * Check the parsing works on snapshot and RC version numbering.
     */
    @Test
    public void testResolveLatestVersionSnapshots() throws Exception {
        assertEquals("0.1.0.46", resolver.resolveLatestVersion(test2, "18w49a").getFriendlyString());
        assertEquals("1.5", resolver.resolveLatestVersion(test3, "1.17-pre5").getFriendlyString());
        assertEquals("1.5", resolver.resolveLatestVersion(test3, "1.17-rc1").getFriendlyString());
    }

    /**
     * Checking the regex and parsing works as expected.
     */
    @Test
    public void testResolveLatestVersionParsing() throws Exception {
        assertEquals("1.3.4+1.16", resolver.resolveLatestVersion(test1, "1.16.2").getFriendlyString()); // author left a trailing slash
    }
}
