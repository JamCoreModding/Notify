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

package io.github.jamalam360.notify.config;

import io.github.jamalam360.notify.NotifyModInit;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.List;

/**
 * @author Jamalam360
 */

@Config(name = "notify/notify_config")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean verboseLogging = false;

    @ConfigEntry.Gui.Tooltip
    public boolean displayUpdatedBadge = true;

    @ConfigEntry.Gui.Tooltip
    public boolean displayUnsupportedBadge = false;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    public boolean renderMainMenuText = true;

    @ConfigEntry.Gui.RequiresRestart
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean dumpInfoOnLaunch = false;

    @ConfigEntry.Gui.RequiresRestart
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean enableNonSpecifiedGradleProperties = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public List<String> blacklist = List.of("minecraft", "fabricloader", "java", "fabric");

    @Override
    public void validatePostLoad() {
        try {
            NotifyModInit.statistics.update();
        } catch (RuntimeException ignored) { // If the config hasn't finished registering yet, update() will throw an exception
        }
    }
}
