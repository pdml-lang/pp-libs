package dev.pp.datatype.utils.parser;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

public class DataParserException extends TextErrorException {

    public DataParserException ( @NotNull TextError textError, @Nullable Throwable cause ) {

        super ( textError, cause );
    }

    public DataParserException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token,
        @Nullable Throwable cause ) {

        super ( id, message, token, cause );
    }

    public DataParserException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token ) {

        super ( id, message, token );
    }

    // needed?
    public DataParserException (
        @NotNull DataValidatorException validatorException,
        @Nullable TextToken token ) {

        super ( "INVALID_DATA", validatorException.getMessage(), token );
    }
}
