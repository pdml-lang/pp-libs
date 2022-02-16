package dev.pp.basics.utilities.file;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class FilePathUtils {

    public static @Nullable File filePathToExistingFile (
        @NotNull Path filePath,
        @NotNull File rootDirectoryForRelativeFile ) throws FileNotFoundException {

        File file = filePathToFile ( filePath, rootDirectoryForRelativeFile );
        FileCheckUtils.checkIsExistingFile ( file );
        return file;
    }

    public static @NotNull File filePathToFile (
        @NotNull Path filePath,
        @NotNull File rootDirectoryForRelativeFile ) {

        if ( filePath.isAbsolute() ) {
            return filePath.toFile();
        } else {
            // return Path.of ( rootDirectoryForRelativeFile, filePath ).toFile();
            return rootDirectoryForRelativeFile.toPath().resolve ( filePath ).toFile();
        }
    }

    public static @NotNull File filePathToFile (
        @NotNull String filePath,
        @NotNull File rootDirectoryForRelativeFile ) {

        return filePathToFile ( Path.of ( filePath ), rootDirectoryForRelativeFile );
    }

    public static String getAbsoluteOSPath ( File file ) {

        try {
            return file.getCanonicalPath();
        } catch ( IOException e ) {
            return file.getAbsolutePath();
        }
    }
}
