package dev.pp.basics.utilities.os;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public class OSConfigUtils {

    public static @NotNull String OSName() {
        return System.getProperty ( "os.name" );
    }

    public static boolean isWindowsOS() {
        return OSName().toLowerCase().contains ( "win" );
    }
}
