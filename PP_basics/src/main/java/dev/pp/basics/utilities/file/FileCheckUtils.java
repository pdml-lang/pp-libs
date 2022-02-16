package dev.pp.basics.utilities.file;

import dev.pp.basics.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;

public class FileCheckUtils {

    public static boolean hasExtension ( @NotNull File file, @NotNull String fileNameExtension ) {
        assert ! fileNameExtension.isEmpty();

        return file.getName().endsWith ( "." + fileNameExtension );

    }

    public static void checkIsExistingFile ( @NotNull File file ) throws FileNotFoundException {

        checkIsNotDirectory ( file );
        // checkIsExistingFileOrDirectory ( file );
        if ( ! file.exists() ) throw new FileNotFoundException (
            "File '" + absolutePath ( file ) + "' does not exist." );
    }

    public static void checkIsExistingFileOrDirectory ( @NotNull File file ) throws FileNotFoundException {

        if ( ! file.exists() ) throw new FileNotFoundException (
            "File or directory '" + absolutePath ( file ) + "' does not exist." );
    }

/*
    public static void checkIsExistingFileOrDirectory ( @NotNull File file ) throws FileNotFoundException {

        if ( ! file.exists() ) {
            throw new FileNotFoundException ( fileNotExistsMessage ( file ) ) {
                public String toString() {
                    return getMessage();
                }
            };
        }
    }

    private static String fileNotExistsMessage ( @NotNull File file ) {
        return "File '" + FilePathUtils.getAbsoluteOSPath ( file ) + "' does not exist."; }

 */

    public static void checkIsNotDirectory ( @NotNull File file ) throws FileNotFoundException {

        if ( file.isDirectory() ) throw new FileNotFoundException (
            "'" + absolutePath ( file ) + "' is a directory. But a file is required." );
    }

    private static @NotNull String absolutePath ( @NotNull File file ) {

        return FilePathUtils.getAbsoluteOSPath ( file );
    }

/*
    public static void checkIsNotDirectory ( @NotNull File file ) throws IOException {

        if ( file.isDirectory() ) {
            throw new IOException ( fileIsDirectoryErrorMessage ( file ) ) {
                public String toString() {
                    return getMessage();
                }
            };
        }
    }

    private static String fileIsDirectoryErrorMessage ( @NotNull File file ) {
        return "'" + FilePathUtils.getAbsoluteOSPath( file ) + "' is a directory. But a file is required."; }

 */
}
