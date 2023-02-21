package dev.pp.basics.utilities;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.utilities.directory.DirectoryCreator;
import dev.pp.basics.utilities.file.TextFileIO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Consumer;

public class ResourcesUtils {

    /* Not used
    public static @NotNull String readTextResource (
        @NotNull String resourcePath,
        @NotNull Class<?> clazz ) throws IOException {

        StringBuilder sb = new StringBuilder();
        consumeLinesInTextResource ( resourcePath, clazz, line -> StringBuilderUtils.appendLine ( sb, line ) );
        return sb.toString();
    }
    */

    public static void copyResourceToFile (
        @NotNull Path resourcePath,
        @NotNull Class<?> clazz,
        @NotNull Path targetFilePath,
        boolean isTextFile,
        boolean createTargetDirectoryIfNotExists ) throws IOException {

        if ( createTargetDirectoryIfNotExists ) {
            DirectoryCreator.createWithParentsIfNotExists ( targetFilePath.getParent() );
        }

        copyResourceToFile ( resourcePath, clazz, targetFilePath, isTextFile );
    }

    public static void copyResourceToFile (
        @NotNull Path resourcePath,
        @NotNull Class<?> clazz,
        @NotNull Path targetFilePath,
        boolean isTextFile ) throws IOException {

        if ( isTextFile ) {
            copyTextResourceToFile ( resourcePath, clazz, targetFilePath );
        } else {
            copyBinaryResourceToFile ( resourcePath, clazz, targetFilePath );
        }
    }

    private static void copyTextResourceToFile (
        @NotNull Path resourcePath,
        @NotNull Class<?> clazz,
        @NotNull Path targetFilePath ) throws IOException {

        try ( @NotNull FileWriter fileWriter = TextFileIO.getUTF8FileWriter ( targetFilePath ) ) {
            consumeLinesInTextResource ( resourcePath, clazz, line -> TextFileIO.writeLineOrThrow ( fileWriter, line ) );
        }
    }

    private static void copyBinaryResourceToFile (
        @NotNull Path resourcePath,
        @NotNull Class<?> clazz,
        @NotNull Path targetFilePath ) throws IOException {

        try ( @NotNull OutputStream outputStream = new FileOutputStream ( targetFilePath.toFile() ) ) {
            copyBinaryResource ( resourcePath, clazz, outputStream );
        }
    }

    private static void consumeLinesInTextResource (
        @NotNull Path resourcePath,
        @NotNull Class<?> clazz,
        @NotNull Consumer<String> lineConsumer ) throws IOException {

        try ( InputStream is = getInputStream ( resourcePath, clazz );
            InputStreamReader isr = new InputStreamReader ( is, StandardCharsets.UTF_8 );
            BufferedReader reader = new BufferedReader ( isr ) ) {
            String line;
            while ( ( line = reader.readLine() ) != null ) {
                lineConsumer.accept ( line );
            }
        }
    }

    private static void copyBinaryResource (
        @NotNull Path resourcePath,
        @NotNull Class<?> clazz,
        @NotNull OutputStream outputStream ) throws IOException {

        try ( @NotNull InputStream is = getInputStream ( resourcePath, clazz ) ) {
            is.transferTo ( outputStream );
        }
    }

    private static @NotNull InputStream getInputStream (
        @NotNull Path resourcePath,
        @NotNull Class<?> clazz ) throws IOException {

        InputStream inputStream = clazz.getClassLoader().getResourceAsStream ( sanitizePath ( resourcePath.toString() ) );
        if ( inputStream != null ) {
            return inputStream;
        } else {
            throw new IOException ( "Resource '" + resourcePath + "' not found." );
        }
    }

    private static @NotNull String sanitizePath ( @NotNull String path ) {

        /* IMPORTANT:
            - '/' MUST be used as path separator. '\' does not work!
            - the path MUST be relative (e.g. /foo/file.txt does not work!)
        */
        path = path.replace ( '\\', '/' );
        if ( path.charAt ( 0 ) == '/' ) {
            path = path.substring ( 1 ); // remove leading '/'
        }
        return path;
    }
}
