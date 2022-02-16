package dev.pp.text.utilities.file;

import dev.pp.basics.annotations.NotNull;
import dev.pp.text.utilities.text.TextLines;

import java.io.*;

import static dev.pp.basics.utilities.file.TextFileUtils.getUTF8FileReader;

public class TextFileUtils {

    public static @NotNull String getNthLineInFile ( @NotNull File file, long n ) throws IOException {

        try ( BufferedReader br = new BufferedReader ( getUTF8FileReader ( file ) ) ) {
            return TextLines.getNthLineInReader ( br, n );
        }
    }

/*
    public static @Nullable String getLineWithMarkerInFile (
        @NotNull File file, long lineNumber, int columnNumber, @NotNull String marker ) throws IOException {

        String line = getNthLineInFile ( file, lineNumber );
        if ( line == null ) return null;

        if ( columnNumber <= 1 ) {
            return marker + line;
        } else if ( columnNumber > line.length() ) {
            return line + marker;
        } else {
            return line.substring ( 0, columnNumber - 1 ) + marker + line.substring ( columnNumber - 1 );
        }
    }
*/
}
