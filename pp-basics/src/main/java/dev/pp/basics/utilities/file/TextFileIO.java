package dev.pp.basics.utilities.file;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.directory.DirectoryCreator;
import dev.pp.basics.utilities.os.OSIO;
import dev.pp.basics.utilities.string.StringConstants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TextFileIO {

    public static @NotNull FileReader getUTF8FileReader ( @NotNull Path filePath ) throws IOException {

        return new FileReader ( filePath.toFile(), StandardCharsets.UTF_8 );
    }

    public static @NotNull Reader getUTF8FileOrStdinReader ( @Nullable Path filePath ) throws IOException {

        if ( filePath != null ) {
            return getUTF8FileReader ( filePath );
        } else {
            return OSIO.standardInputUTF8Reader ();
        }
    }

    @Deprecated
    public static @NotNull FileReader getUTF8FileReader ( @NotNull File file ) throws IOException {

        // FileCheckUtils.checkIsExistingFile ( file );
        return new FileReader ( file, StandardCharsets.UTF_8 );
    }

    public static @NotNull FileWriter getUTF8FileWriter ( @NotNull Path filePath ) throws IOException {

        // FileCheckUtils.checkIsNotDirectory ( file );
        return new FileWriter ( filePath.toFile(), StandardCharsets.UTF_8 );
    }

    public static @NotNull FileWriter createDirAndGetUTF8FileWriter ( @NotNull Path filePath ) throws IOException {

        DirectoryCreator.createWithParentsIfNotExists ( filePath.getParent () );
        return getUTF8FileWriter ( filePath );
    }

    public static @NotNull Writer getUTF8FileOrStdoutWriter ( @Nullable Path filePath ) throws IOException {

        if ( filePath != null ) {
            return getUTF8FileWriter ( filePath );
        } else {
            return OSIO.standardOutputUTF8Writer ();
        }
    }

    @Deprecated
    public static @NotNull FileWriter getUTF8FileWriter ( @NotNull File file ) throws IOException {

        // FileCheckUtils.checkIsNotDirectory ( file );
        return new FileWriter ( file, StandardCharsets.UTF_8 );
    }


    // read

    @Deprecated
    public static @Nullable String readTextFromUTF8File ( @NotNull File file ) throws IOException {

        return readTextFromUTF8File ( file.toPath() );
    }

    public static @Nullable String readTextFromUTF8File ( @NotNull Path filePath ) throws IOException {

        String result = Files.readString ( filePath );
        return result.isEmpty() ? null : result;
    }

    public static @Nullable List<String> readTextLinesFromUTF8File ( @NotNull Path filePath ) throws IOException {

        List<String> lines = Files.readAllLines ( filePath, StandardCharsets.UTF_8 );
        return lines.isEmpty() ? null : lines;
    }


    // write

    @Deprecated
    public static void writeTextToUTF8File ( @NotNull String text, @NotNull File file ) throws IOException {

        writeTextToUTF8File ( text, file.toPath() );
    }

    public static void writeTextToUTF8File ( @NotNull String text, @NotNull Path filePath ) throws IOException {

        Files.writeString ( filePath, text, StandardCharsets.UTF_8 );
    }

    public static void writeLineOrThrow ( @NotNull Writer writer, @Nullable String line ) {
        try {
            writeLine ( writer, line );
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }
    }

    public static void writeLine ( @NotNull Writer writer, @Nullable String line ) throws IOException {

        if ( line != null ) writer.write ( line );
        writeNewLine ( writer );
    }

    public static void writeNewLine ( @NotNull Writer writer ) throws IOException {

        writer.write ( StringConstants.OS_NEW_LINE );
    }

    public static void closeIfFileReader ( @Nullable Reader reader ) {

        if ( reader instanceof FileReader ) {
            try {
                reader.close ();
            } catch ( IOException e ) {
                throw new RuntimeException ( e );
            }
        }
    }

    public static void closeIfFileWriter ( @Nullable Writer writer ) {

        if ( writer instanceof FileWriter ) {
            try {
                writer.close ();
            } catch ( IOException e ) {
                throw new RuntimeException ( e );
            }
        }
    }
}
