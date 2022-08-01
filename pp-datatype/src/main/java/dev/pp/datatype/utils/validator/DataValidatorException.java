package dev.pp.datatype.utils.validator;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

public class DataValidatorException extends TextErrorException {

    public DataValidatorException ( @NotNull TextError textError, @Nullable Throwable cause ) {

        super ( textError, cause );
    }

    public DataValidatorException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken textToken,
        @Nullable Throwable cause ) {

        super ( id, message, textToken, cause );
    }

    public DataValidatorException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken textToken ) {

        super ( id, message, textToken );
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
