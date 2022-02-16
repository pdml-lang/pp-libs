package dev.pp.datatype.parser;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.reader.exception.InvalidTextException;
import dev.pp.text.token.TextToken;

public class DataParserException extends InvalidTextException {

    public DataParserException (
        @NotNull String id,
        @NotNull String message,
        @Nullable TextToken token,
        @Nullable Exception cause ) {

        super ( id, message, token, cause );
    }
}
