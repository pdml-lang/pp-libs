package dev.pp.basics.utilities.file;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class TempFileUtils {

    public static @NotNull File createEmptyTempFile (
        @Nullable String fileName,
        @Nullable String fileNameExtension,
        boolean deleteOnExit ) throws IOException {

        File file = Files.createTempFile (
            fileName,
            fileNameExtension == null ? null : "." + fileNameExtension ).toFile();

        if ( deleteOnExit ) file.deleteOnExit();

        return file;
    }

    public static @NotNull File createEmptyTempFile ( boolean deleteOnExit ) throws IOException {

        return createEmptyTempFile ( null, null, deleteOnExit );
    }

    public static @NotNull File createNonEmptyTempTextFile (
        @NotNull String content,
        @Nullable String fileName,
        @Nullable String fileNameExtension,
        boolean deleteOnExit ) throws IOException {

        File file = createEmptyTempFile ( fileName, fileNameExtension, deleteOnExit );

        try ( FileWriter writer = TextFileUtils.getUTF8FileWriter ( file ) ) {
            writer.append ( content );
            writer.flush();
        }

        return file;
    }

    public static @NotNull File createNonEmptyTempTextFile (
        @NotNull String content,
        boolean deleteOnExit ) throws IOException {

        return createNonEmptyTempTextFile ( content, null, null, deleteOnExit );
    }
}
