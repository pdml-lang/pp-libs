package dev.pp.text.utilities.file;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.FileCheckUtils;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.text.TextLines;

import java.io.*;
import java.nio.file.Path;

import static dev.pp.basics.utilities.file.TextFileIO.getUTF8FileReader;

public class TextFileUtils {

    public static @NotNull String getNthLineInFile ( @NotNull Path filePath, long n ) throws IOException {

        try ( BufferedReader br = new BufferedReader ( getUTF8FileReader ( filePath ) ) ) {
            return TextLines.getNthLineInReader ( br, n );
        }
    }

    public static void checkIsExistingFile ( @NotNull Path filePath, @Nullable TextToken textToken ) throws TextErrorException {

        try {
            FileCheckUtils.checkIsExistingFile ( filePath );
        } catch ( FileNotFoundException e ) {
            throw new TextErrorException (
                "FILE_DOES_NOT_EXIST",
                e.getMessage(),
                textToken );
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
