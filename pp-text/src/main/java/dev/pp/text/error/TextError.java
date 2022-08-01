package dev.pp.text.error;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.SimpleLogger;
import dev.pp.basics.utilities.file.FilePathUtils;
import dev.pp.basics.utilities.os.process.OSCommand;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.basics.utilities.string.StringUtils;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.nio.file.Path;

public class TextError {


    public static @NotNull TextError createAbortingError (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        return new TextError ( id, message, token, false, false );
    }

    public static @NotNull TextError createNonAbortingError (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        return new TextError ( id, message, token, false, true );
    }

    public static @NotNull TextError createAbortingWarning (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        return new TextError ( id, message, token, true, false );
    }

    public static @NotNull TextError createNonAbortingWarning (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        return new TextError ( id, message, token, true, true );
    }


    /**
     * An optional unique identifier denoting the kind of error.
     */
    protected final @Nullable String id;
    public @Nullable String getId() { return id; }

    protected final @NotNull String message;
    public @NotNull String getMessage() { return message; }

    /**
     * An optional token denoting where the error was detected.
     */
    protected final @Nullable TextToken token;
    public @Nullable TextToken getToken() { return token; }

    /**
     * Set to true if this error denotes a warning.
     */
    protected final boolean isWarning;
    public boolean isWarning() { return isWarning; }
    public boolean isError() { return ! isWarning; }

    /**
     * Set to true if the operation that generated the error can continue despite this error.
     */
    protected final boolean isNonAborting;
    public boolean isNonAborting() { return isNonAborting; }
    public boolean isAborting() { return ! isNonAborting; }

    /**
     * Set to true after the error has been handled by an error handler.
     * Can be used to avoid handling the error twice (e.g. displaying it in the error handler, and again in a main error/exception handler.)
     */
    protected boolean hasBeenHandled;
    public boolean hasBeenHandled() { return hasBeenHandled; }
    public void setHasBeenHandled ( boolean hasBeenHandled ) { this.hasBeenHandled = hasBeenHandled; }


    public TextError (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token,
        boolean isWarning,
        boolean isNonAborting,
        boolean hasBeenHandled ) {

        this.id = id;
        this.message = message;
        this.token = token;
        this.isWarning = isWarning;
        this.isNonAborting = isNonAborting;
        this.hasBeenHandled = hasBeenHandled;
    }

    public TextError (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token,
        boolean isWarning,
        boolean isNonAborting ) {

        this ( id, message, token, isWarning, isNonAborting, false );
    }

    public TextError (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token ) {

        this ( id, message, token, false, false );
    }


    public @Nullable String getTokenText() { return token != null ? token.getText() : null; }

    public @Nullable TextLocation getLocation() { return token != null ? token.getLocation() : null; }

    public @Nullable Path getFilePathOfLocation() {

        TextLocation location = getLocation();
        if ( location == null ) {
            return null;
        } else {
            return location.getResourceAsFilePath ();
        }
    }

    public @Nullable String getTextLine() throws Exception {

        TextLocation location = getLocation ();
        if ( location == null ) {
            return null;
        } else {
            return location.getTextLine();
        }
    }

    public @Nullable String getTextLineWithInlineMarker () throws Exception {

        TextLocation location = getLocation ();
        if ( location == null ) {
            return null;
        } else {
            return location.getTextLineWithInlineMarker();
        }
    }

    public @NotNull String getLabel() {

        return isWarning ? "Warning" : "Error";
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append ( getLabel() );
        sb.append ( ":" );
        sb.append ( StringConstants.OS_NEW_LINE );

        sb.append ( message );

        if ( id != null ) {
            sb.append ( " [" );
            sb.append ( id );
            sb.append ( "]" );
        }

        sb.append ( StringConstants.OS_NEW_LINE );

        @Nullable TextLocation location = getLocation();
        if ( location != null ) sb.append ( location );

        return sb.toString();
    }

    public @NotNull String toLogString() {

        StringBuilder sb = new StringBuilder();

        @Nullable TextLocation location = getLocation();
        if ( location == null ) location = TextLocation.UNKNOWN_LOCATION;
        sb.append ( location.toLogString() );

        sb.append ( "," );
        sb.append ( getLabel().charAt ( 0 ) );

        sb.append ( ",id " );
        sb.append ( id );

        sb.append ( ",\"" );
        sb.append ( StringUtils.replaceQuoteWith2Quotes ( message ) );
        sb.append ( '"' );

        return sb.toString();
    }
}
