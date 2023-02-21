package dev.pp.datatype.utils.validator;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.token.TextToken;

public class DataValidatorException extends TextErrorException {

    public DataValidatorException (
        @NotNull TextError textError,
        @Nullable Throwable cause ) {

        super ( textError, cause );
    }

    public DataValidatorException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken textToken,
        @Nullable Throwable cause ) {

        super ( message, id, textToken, cause );
    }

    public DataValidatorException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken textToken ) {

        super ( message, id, textToken );
    }
}

/*
public class DataValidatorException extends Exception {

    private final @Nullable String id;
    public @Nullable String getId() { return id; }

    private final @Nullable TextToken textToken;
    public @Nullable TextToken getTextToken() { return textToken; }


    public DataValidatorException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken textToken,
        @Nullable Throwable cause ) {

        super ( message, cause );
        this.id = id;
        this.textToken = textToken;
    }

    public DataValidatorException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken textToken ) {

        this ( id, message, textToken, null );
    }
}
*/
