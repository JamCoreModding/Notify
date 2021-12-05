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

package io.github.jamalam360.notify;

import io.github.jamalam360.notify.resolver.NotifyVersionChecker;
import io.github.jamalam360.notify.util.Utils;

/**
 * @author Jamalam360
 */
public class NotifyStatistics {
    private long resolveTime;
    private int totalSupportedModCount;
    private int totalModCount;
    private double percentageCoverage;

    private int modrinthResolvedCount = 0;
    private int curseForgeResolvedCount = 0;
    private int specifiedGradlePropertiesResolvedCount = 0;
    private int nonSpecifiedGradlePropertiesResolvedCount = 0;
    private int jsonResolvedCount = 0;

    public void update() {
        this.totalModCount = Utils.getLoadedNonIgnoredModCount();
        this.totalSupportedModCount = (int) NotifyModInit.MOD_UPDATE_STATUS_MAP.entrySet().stream().filter(e -> e.getValue() != NotifyVersionChecker.VersionComparisonResult.UNSUPPORTED && e.getValue() != NotifyVersionChecker.VersionComparisonResult.IGNORED).count();
        this.percentageCoverage = ((double) this.totalSupportedModCount / (double) this.totalModCount) * 100D;
    }

    public void setResolveTime(long resolveTime) {
        this.resolveTime = resolveTime;
    }

    public void modrinthMod() {
        modrinthResolvedCount++;
    }

    public void curseForgeMod() {
        curseForgeResolvedCount++;
    }

    public void specifiedGradlePropertiesMod() {
        specifiedGradlePropertiesResolvedCount++;
    }

    public void nonSpecifiedGradlePropertiesMod() {
        nonSpecifiedGradlePropertiesResolvedCount++;
    }

    public void jsonMod() {
        jsonResolvedCount++;
    }

    public int getModrinthResolvedCount() {
        return modrinthResolvedCount;
    }

    public int getCurseForgeResolvedCount() {
        return curseForgeResolvedCount;
    }

    public int getSpecifiedGradlePropertiesResolvedCount() {
        return specifiedGradlePropertiesResolvedCount;
    }

    public int getNonSpecifiedGradlePropertiesResolvedCount() {
        return nonSpecifiedGradlePropertiesResolvedCount;
    }

    public int getJsonResolvedCount() {
        return jsonResolvedCount;
    }

    public long getResolveTime() {
        return resolveTime;
    }

    public int getTotalSupportedModCount() {
        return totalSupportedModCount;
    }

    public int getTotalModCount() {
        return totalModCount;
    }

    public double getPercentageCoverage() {
        return percentageCoverage;
    }
}
