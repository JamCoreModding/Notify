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

package io.github.jamalam360.notify.util;

import io.github.jamalam360.notify.resolver.NotifyVersionChecker;
import io.github.jamalam360.notify.util.NotifyLogger;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jamalam360
 */
public class NotifyThreading {
    private final ThreadPoolExecutor executor;
    /**
     * The process of logging a message to say that a mod has updates is fairly slow when in a large modpack like AOF4.
     * So we do this in a separate thread.
     */
    private final ThreadPoolExecutor loggingExecutor;
    private final Map<String, Future<NotifyVersionChecker.VersionComparisonResult>> futures = new HashMap<>();

    public NotifyThreading() {
        this.executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 2L, TimeUnit.SECONDS, new SynchronousQueue<>(), new NotifyThreadFactory("Version Resolver"));
        this.loggingExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new NotifyThreadFactory("Logging"));
    }

    public void queueLogging(Map<String, NotifyVersionChecker.VersionComparisonResult> map) {
        for (Map.Entry<String, NotifyVersionChecker.VersionComparisonResult> entry : map.entrySet()) {
            this.loggingExecutor.submit(() -> {
                NotifyVersionChecker.VersionComparisonResult result = entry.getValue();
                String s = entry.getKey();
                try {
                    switch (result) {
                        case UPDATED -> NotifyLogger.info(
                                true,
                                "Mod %s is updated to the latest version",
                                s
                        );
                        case OUTDATED -> NotifyLogger.info(
                                false,
                                "Mod %s has updates available",
                                s
                        );
                        case FAILURE -> NotifyLogger.info(
                                true,
                                "Failed to get version of mod %s",
                                s
                        );
                    }
                } catch (Exception ignored) {
                }
            });
        }

        this.loggingExecutor.execute(this.loggingExecutor::shutdown);
    }

    public Future<NotifyVersionChecker.VersionComparisonResult> queue(ModContainer m) {
        this.futures.put(m.getMetadata().getId(), this.executor.submit(() -> NotifyVersionChecker.checkVersion(m)));
        return this.futures.get(m.getMetadata().getId());
    }

    public Map<String, Future<NotifyVersionChecker.VersionComparisonResult>> allFutures() {
        return futures;
    }

    public boolean isFinished() {
        return this.executor.getQueue().size() == 0;
    }

    public void close() {
        this.executor.shutdown();
    }

    public static class NotifyThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public NotifyThreadFactory(String name) {
            group = Thread.currentThread().getThreadGroup();
            namePrefix = "Notify/" + name + "-";
        }

        public Thread newThread(@NotNull Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
