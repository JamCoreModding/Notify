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
