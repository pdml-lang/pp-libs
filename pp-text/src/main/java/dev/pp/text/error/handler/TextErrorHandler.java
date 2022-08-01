package dev.pp.text.error.handler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

public interface TextErrorHandler {

//    void start();
    
//    void stop();

    void handleError ( @NotNull TextError error );

    default void handleAbortingError (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        handleError ( TextError.createAbortingError ( id, message, token ) );
    }

    default void handleNonAbortingError (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        handleError ( TextError.createNonAbortingError ( id, message, token ) );
    }

    default void handleAbortingWarning (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        handleError ( TextError.createAbortingWarning ( id, message, token ) );
    }

    default void handleNonAbortingWarning (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        handleError ( TextError.createNonAbortingWarning ( id, message, token ) );
    }
/*
    void handleErrorAndThrow ( @NotNull TextError error ) throws TextErrorException;

    default void handleAbortingErrorAndThrow (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) throws TextErrorException {

        handleErrorAndThrow ( TextError.createAbortingError ( id, message, token ) );
    }

    default void handleNonAbortingErrorAndThrow (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) throws TextErrorException {

        handleErrorAndThrow ( TextError.createNonAbortingError ( id, message, token ) );
    }

    default void handleAbortingWarningAndThrow (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) throws TextErrorException {

        handleErrorAndThrow ( TextError.createAbortingWarning ( id, message, token ) );
    }

    default void handleNonAbortingWarningAndThrow (
        @Nullable String id, @NotNull String message, @Nullable TextToken token ) throws TextErrorException {

        handleErrorAndThrowAndThrow ( TextError.createNonAbortingWarning ( id, message, token ) );
    }
*/

    @Nullable TextError firstErrorOrWarning();
    @Nullable TextError lastErrorOrWarning();

    @Nullable TextError firstError();
    @Nullable TextError lastError();

    @Nullable TextError firstWarning();
    @Nullable TextError lastWarning();

    /**
     * check if new errors have been handled, after <code>initialLastError</code>
     * @param initialLastError the last error, after which new errors might have occurred
     * @return <code>true</code> if new errors have been handled after <code>initialLastError</code>
     */
    boolean hasNewErrors ( @Nullable TextError initialLastError );

    /**
     * throw a <code>TextErrorException</code> if new errors have been handled, after <code>initialLastError</code>
     * @param initialLastError the last error, after which new errors might have occurred
     * @throws TextErrorException if new errors have been handled after <code>initialLastError</code>
     */
    void throwIfNewErrors ( @Nullable TextError initialLastError ) throws TextErrorException;

    /*
    void setMark();

    @Nullable TextError firstErrorAfterMark();
    */
}
