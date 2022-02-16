package dev.pp.basics.utilities;

import dev.pp.basics.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class URLUtilities {

    public static @NotNull Reader getUTF8URLReader ( @NotNull String URLString ) throws IOException {

        return getUTF8URLReader ( new URL ( URLString ) );
    }

    public static @NotNull Reader getUTF8URLReader ( @NotNull URL URL ) throws IOException {

        return new InputStreamReader ( URL.openStream(), StandardCharsets.UTF_8 );
    }

    public static @NotNull String readUTF8Text ( @NotNull String URLString ) throws IOException {

        return readUTF8Text ( new URL ( URLString ) );
    }

    public static @NotNull String readUTF8Text ( @NotNull URL URL ) throws IOException {

        try ( InputStream is = URL.openStream() ) {
            return new String ( is.readAllBytes(), StandardCharsets.UTF_8 );
        }
    }
}
