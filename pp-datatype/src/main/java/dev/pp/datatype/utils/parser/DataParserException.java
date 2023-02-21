package dev.pp.datatype.utils.parser;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

public class DataParserException extends TextErrorException {

    public DataParserException (
        @NotNull TextError textError,
        @Nullable Throwable cause ) {

        super ( textError, cause );
    }

    public DataParserException (
        @NotNull String message,
        @Nullable String id,
        @Nullable String data,
        @Nullable TextLocation location,
        @Nullable Throwable cause ) {

        super ( message, id, data, location, cause );
    }

    public DataParserException (
        @NotNull String message,
        @Nullable String id,
        @Nullable String data,
        @Nullable TextLocation location ) {

        this ( message, id, data, location, null );
    }

    public DataParserException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token,
        @Nullable Throwable cause ) {

        super ( message, id, token, cause );
    }

    public DataParserException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token ) {

        super ( message, id, token );
    }

    // needed?
    public DataParserException (
        @NotNull DataValidatorException validatorException,
        @Nullable String data,
        @Nullable TextLocation location ) {

        this ( validatorException.getMessage(), "INVALID_DATA", data, location );
    }
}
