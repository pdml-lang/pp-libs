package dev.pp.basics.utilities.file;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

public class FileNameUtils {

    public static char NAME_EXTENSION_SEPARATOR = '.';
    // public static @NotNull String NAME_EXTENSION_SEPARATOR_STRING = ".";

    public static @Nullable String getExtension ( @NotNull String fileName ) {

        int separatorIndex = getSeparatorIndex ( fileName );
        return separatorIndex > 0 ? fileName.substring ( separatorIndex + 1 ) : null;
    }

    public static @NotNull String changeExtension ( @NotNull String fileName, @NotNull String newExtension ) {

        int separatorIndex = getSeparatorIndex ( fileName );
        if ( separatorIndex > 0 ) {
            return fileName.substring ( 0, separatorIndex ) + NAME_EXTENSION_SEPARATOR + newExtension;
        } else {
            return fileName + NAME_EXTENSION_SEPARATOR + newExtension;
        }
    }

    private static int getSeparatorIndex ( @NotNull String fileName ) {

        return fileName.lastIndexOf ( NAME_EXTENSION_SEPARATOR );
    }
}
