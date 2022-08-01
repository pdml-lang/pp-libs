package dev.pp.text.error.handler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.os.OSIO;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.text.error.TextError;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Function;

public class Write_TextErrorHandler extends AbstractTextErrorHandler {


    private final @NotNull Writer writer;
    private final @Nullable String separator;


    public Write_TextErrorHandler (
        @NotNull Writer writer,
        @NotNull Function<TextError, String> errorToStringConverter,
        @Nullable String separator ) {

        super ( errorToStringConverter );

        this.writer = writer;
        this.separator = separator;
    }

    public Write_TextErrorHandler ( @NotNull Writer writer ) {

        this ( writer, TextError::toString, StringConstants.OS_NEW_LINE );
    }

    public Write_TextErrorHandler () {

        this ( OSIO.standardErrorUTF8Writer() );
    }


    public void handleError ( @NotNull TextError error, @NotNull String message ) {

        write ( message );
        if ( separator != null ) write ( separator );
        flush();
    }

    private void write ( String string ) {

        try {
            writer.write ( string );
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }
    }

    private void flush() {

        try {
            writer.flush();
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }
    }
}
