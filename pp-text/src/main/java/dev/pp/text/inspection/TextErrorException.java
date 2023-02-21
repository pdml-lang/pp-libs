package dev.pp.text.inspection;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

public class TextErrorException extends Exception {

    private final @NotNull TextError textError;
    public @NotNull TextError getTextError() { return textError; }


    public TextErrorException ( @NotNull TextError textError, @Nullable Throwable cause ) {

        super ( textError.getMessage(), cause );
        this.textError = textError;
    }

    public TextErrorException ( @NotNull TextError textError ) {
        this ( textError, null );
    }

    public TextErrorException (
        @NotNull String message,
        @Nullable String id,
        @Nullable String textSegment,
        @Nullable TextLocation location,
        @Nullable Throwable cause ) {

        this ( new TextError ( message, id, textSegment, location ), cause );
    }

    public TextErrorException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token,
        @Nullable Throwable cause ) {

        this ( new TextError ( message, id, token ), cause );
    }

    public TextErrorException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token ) {

        this ( message, id, token, null );
    }


    // public @Nullable String id() { return textError.getId(); }

    // public @Nullable TextToken token() { return textError.token(); }

    public boolean hasBeenHandled() { return textError.hasBeenHandled(); }

    public @NotNull String toString() {
        return textError.toString();
    }
}
