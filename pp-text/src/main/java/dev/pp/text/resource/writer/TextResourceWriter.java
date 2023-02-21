package dev.pp.text.resource.writer;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.basics.utilities.os.OSIO;
import dev.pp.text.resource.*;

import java.io.*;
import java.nio.file.Path;

public class TextResourceWriter implements Closeable {

    private final @NotNull Writer writer;
    @NotNull public Writer getWriter() { return writer; }

    private final @Nullable TextResource textResource;
    @Nullable public TextResource getTextResource() { return textResource; }


    public TextResourceWriter ( @NotNull Writer writer, @Nullable TextResource textResource ) {
        this.writer = writer;
        this.textResource = textResource;
    }

    public TextResourceWriter ( @NotNull Path filePath ) throws IOException {
        this ( TextFileIO.getUTF8FileWriter ( filePath ), new File_TextResource ( filePath ) );
    }

    public static @NotNull TextResourceWriter forString() {
        return new TextResourceWriter ( new StringWriter(), new String_TextResource ( "" ) );
    }

    public static @NotNull TextResourceWriter STDOUT = new TextResourceWriter (
        OSIO.standardOutputUTF8Writer(),
        Stdout_TextResource.INSTANCE );

    public static @NotNull TextResourceWriter STDERR = new TextResourceWriter (
        OSIO.standardErrorUTF8Writer(),
        Stderr_TextResource.INSTANCE );


    public @Nullable Path getResourceAsFilePath () {
        return textResource == null ? null : textResource.getResourceAsFilePath();
    }

    public void close() throws IOException {
        writer.close();
    }

    @Override
    public String toString() { return textResource == null ? "TextResourceWriter" : textResource.toString(); }
}
