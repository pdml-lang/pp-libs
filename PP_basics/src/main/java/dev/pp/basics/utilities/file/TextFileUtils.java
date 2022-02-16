package dev.pp.basics.utilities.file;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TextFileUtils {

    public static FileReader getUTF8FileReader ( @NotNull File file ) throws IOException {

        // FileCheckUtils.checkIsExistingFile ( file );
        return new FileReader ( file, StandardCharsets.UTF_8 );
    }

    public static FileWriter getUTF8FileWriter ( @NotNull File file ) throws IOException {

        // FileCheckUtils.checkIsNotDirectory ( file );
        return new FileWriter ( file, StandardCharsets.UTF_8 );
    }


    // read

    public static @Nullable String readTextFromUTF8File ( @NotNull File file ) throws IOException {

        return readTextFromUTF8File ( file.toPath() );
    }

    public static @Nullable String readTextFromUTF8File ( @NotNull Path filePath ) throws IOException {

        String result = Files.readString ( filePath );
        return result.isEmpty() ? null : result;
    }

    public static @Nullable List<String> readTextLinesFromUTF8File ( @NotNull File file ) throws IOException {

        return readTextLinesFromUTF8File ( file.toPath() );
    }

    public static @Nullable List<String> readTextLinesFromUTF8File ( @NotNull Path filePath ) throws IOException {

        List<String> lines = Files.readAllLines ( filePath, StandardCharsets.UTF_8 );
        return lines.isEmpty() ? null : lines;
    }


    // write

    public static void writeTextToUTF8File ( @NotNull String text, @NotNull File file ) throws IOException {

        writeTextToUTF8File ( text, file.toPath() );
    }

    public static void writeTextToUTF8File ( @NotNull String text, @NotNull Path filePath ) throws IOException {

        Files.writeString ( filePath, text, StandardCharsets.UTF_8 );
    }
}
