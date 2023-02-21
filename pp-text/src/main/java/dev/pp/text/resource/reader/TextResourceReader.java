package dev.pp.text.resource.reader;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.basics.utilities.os.OSIO;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.Stdin_TextResource;
import dev.pp.text.resource.String_TextResource;
import dev.pp.text.resource.TextResource;

import java.io.*;
import java.nio.file.Path;

public class TextResourceReader implements Closeable {

    private final @NotNull Reader reader;
    @NotNull public Reader getReader () { return reader; }

    private final @Nullable TextResource textResource;
    @Nullable public TextResource getTextResource () { return textResource; }


    public TextResourceReader ( @NotNull Reader reader, @Nullable TextResource textResource ) {
        this.reader = reader;
        this.textResource = textResource;
    }

    public TextResourceReader ( @NotNull Path filePath ) throws IOException {
        this (
            TextFileIO.getUTF8FileReader ( filePath ),
            new File_TextResource ( filePath ) );
    }

    public TextResourceReader ( @NotNull String string ) {
        this (
            new StringReader ( string ),
            new String_TextResource ( string ) );
    }

    public static @NotNull TextResourceReader STDIN = new TextResourceReader (
            OSIO.standardInputUTF8Reader(),
            Stdin_TextResource.INSTANCE );


    public @Nullable Path getResourceAsFilePath () {
        return textResource == null ? null : textResource.getResourceAsFilePath();
    }

    public void close() throws IOException {
        reader.close();
    }

    @Override
    public String toString() { return textResource == null ? "TextResourceReader" : textResource.toString(); }
}
