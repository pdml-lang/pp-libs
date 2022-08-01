package dev.pp.text.error.handler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.utilities.DebugUtils;
import dev.pp.basics.utilities.SimpleLogger;
import dev.pp.text.error.TextError;

import java.util.function.Function;

public class Log_TextErrorHandler extends AbstractTextErrorHandler {


    public Log_TextErrorHandler ( @NotNull Function<TextError, String> errorToStringConverter ) {

        super ( errorToStringConverter );
    }


    public void handleError ( @NotNull TextError error, @NotNull String message ) {

        if ( error.isError() ) {
            SimpleLogger.error ( message );
        } else {
            SimpleLogger.warning ( message );
        }
    }
}
