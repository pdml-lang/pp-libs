package dev.pp.text.reader.stack;

import dev.pp.basics.annotations.NotNull;
import dev.pp.text.reader.CharReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface CharReaderWithInserts extends CharReader {

    void insert ( @NotNull CharReader iterator );

    void insert ( @NotNull File file ) throws IOException;

    void insert ( @NotNull URL url ) throws IOException;

    void insert ( @NotNull String string );
}
