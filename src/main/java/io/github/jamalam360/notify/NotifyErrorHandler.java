package io.github.jamalam360.notify;

/**
 * @author Jamalam360
 */
public class NotifyErrorHandler {
    private static String currentModId = null;

    public static void setCurrentModId(String modId) {
        currentModId = modId;
    }

    public static void finishedResolving() {
        currentModId = null;
    }

    public static boolean hasError() {
        return currentModId != null;
    }

    public static String getErrorMod() {
        return currentModId;
    }
}
