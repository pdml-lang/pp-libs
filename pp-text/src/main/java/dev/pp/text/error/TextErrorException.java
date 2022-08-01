package dev.pp.text.error;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class TextErrorException extends Exception {

    private final @NotNull TextError textError;
    public @NotNull TextError getTextError () { return textError; }


    public TextErrorException ( @NotNull TextError textError, @Nullable Throwable cause ) {

        super ( textError.getMessage(), cause );
        this.textError = textError;
    }

    public TextErrorException ( @NotNull TextError textError ) {

        this ( textError, null );
    }

    public TextErrorException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token,
        @Nullable Throwable cause ) {

        this ( new TextError ( id, message, token ), cause );
    }

    public TextErrorException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token ) {

        this ( id, message, token, null );
    }


    public @Nullable String getId() { return textError.getId(); }

    public @Nullable TextToken getToken() { return textError.getToken(); }

    public boolean isWarning() { return textError.isWarning(); }

    public boolean isNonAborting() { return textError.isNonAborting(); }

    public boolean hasBeenHandled() { return textError.hasBeenHandled(); }

    public @NotNull String toString() {

        return textError.toString();
    }
/*

    private final @Nullable String id;
    private final @Nullable TextToken token;


    protected TextErrorException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token,
        @Nullable Exception cause ) {

        super ( message, cause );

        this.id = id;
        this.token = token;
    }

    public TextErrorException ( @NotNull TextErrorOld textError ) {
        this ( textError.getId(), textError.getMessage(), textError.getToken(), null );
    }

    public @Nullable String getId() { return id; }

    public @Nullable TextToken getToken() { return token; }

    public @NotNull TextErrorOld toTextError() { return new TextErrorOld ( id, getMessage(), token ); }

    public @NotNull String toString() {

        return TextErrorOrWarningOld.toString ( "Error", id, getMessage(), token );
    }

 */
}
