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
    public static NotifyStatistics INSTANCE = null;

    private final long resolveTime;
    private final int totalSupportedModCount;
    private final int totalModCount;
    private final double percentageCoverage;

    public NotifyStatistics(long resolveTime) {
        INSTANCE = this;

        this.totalModCount = Utils.getLoadedNonIgnoredModCount();
        this.totalSupportedModCount = (int) NotifyModInit.MOD_UPDATE_STATUS_MAP.entrySet().stream().filter(e -> e.getValue() != NotifyVersionChecker.VersionComparisonResult.IGNORED && e.getValue() != NotifyVersionChecker.VersionComparisonResult.UNSUPPORTED).count();
        this.percentageCoverage = ((double) this.totalSupportedModCount / (double) this.totalModCount) * 100D;
        this.resolveTime = resolveTime;
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
