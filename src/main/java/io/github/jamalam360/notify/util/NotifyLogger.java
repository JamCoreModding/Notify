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

import io.github.jamalam360.notify.NotifyModInit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jamalam360
 */
public class NotifyLogger {
    private static final Logger LOGGER = LogManager.getLogger("Notify");

    public static void info(boolean verbose, String message) {
        if (verbose && !NotifyModInit.getConfig().verboseLogging) return;
        LOGGER.log(Level.INFO, message);
    }

    public static void info(boolean verbose, String message, Object... args) {
        if (verbose && !NotifyModInit.getConfig().verboseLogging) return;
        LOGGER.log(Level.INFO, message.formatted(args));
    }

    public static void warn(boolean verbose, String message) {
        if (verbose && !NotifyModInit.getConfig().verboseLogging) return;
        LOGGER.log(Level.WARN, message);
    }

    public static void warn(boolean verbose, String message, Object... args) {
        if (verbose && !NotifyModInit.getConfig().verboseLogging) return;
        LOGGER.log(Level.WARN, message.formatted(args));
    }
}
