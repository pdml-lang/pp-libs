package dev.pp.basics.utilities.string;

import dev.pp.basics.annotations.NotNull;

public class StringNewLineUtils {

    public static @NotNull String replaceWindowsNewLinesWithUnixNewLines ( @NotNull String string ) {

        return string.replace ( StringConstants.WINDOWS_NEW_LINE, StringConstants.UNIX_NEW_LINE );
    }
}
