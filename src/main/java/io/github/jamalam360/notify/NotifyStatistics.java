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
