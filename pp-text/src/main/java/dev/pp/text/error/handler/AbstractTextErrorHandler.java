package dev.pp.text.error.handler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.DebugUtils;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextErrorException;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractTextErrorHandler implements TextErrorHandler {


    // public static final Predicate<TextError> THROW_IF_ABORTING_PREDICATE = TextError::isAborting;
    // public static final Predicate<TextError> NEVER_THROW_PREDICATE = error -> false;


    protected final @NotNull Function<TextError, String> errorToStringConverter;
    // protected final @NotNull Predicate<TextError> throwExceptionPredicate;

    protected @Nullable TextError firstError;
    public TextError firstError() { return firstError; }

    protected @Nullable TextError lastError;
    public TextError lastError() { return lastError; }

    protected @Nullable TextError firstWarning;
    public TextError firstWarning() { return firstWarning; }

    protected @Nullable TextError lastWarning;
    public TextError lastWarning() { return lastWarning; }

    // protected @Nullable TextError firstErrorAfterMark;
    // protected boolean marked;


    public AbstractTextErrorHandler (
        @NotNull Function<TextError, String> errorToStringConverter ) {
        // @NotNull Predicate<TextError> throwExceptionPredicate ) {

        this.errorToStringConverter = errorToStringConverter;
        // this.throwExceptionPredicate = throwExceptionPredicate;

        this.firstError = null;
        this.firstWarning = null;
        // this.firstErrorAfterMark = null;
        // this.marked = false;
    }

    /*
    public AbstractTextErrorHandler ( @NotNull Function<TextError, String> errorToStringConverter ) {

        this ( errorToStringConverter, THROW_IF_ABORTING_PREDICATE );
    }
    */

    public AbstractTextErrorHandler () {

        this ( TextError::toString );
    }


    // public void handleError ( @NotNull TextError error ) throws TextErrorException {
    public void handleError ( @NotNull TextError error ) {

        if ( error.isError() ) {
            if ( firstError == null ) firstError = error;
            lastError = error;
        } else {
            if ( firstWarning == null ) firstWarning = error;
            lastWarning = error;
        }

        /*
        if ( marked ) {
            firstErrorAfterMark = error;
            marked = false;
        }
        */

        // errorCount ++;

        handleError ( error, errorToStringConverter.apply ( error ) );

        error.setHasBeenHandled ( true );

        // if ( throwExceptionPredicate.test ( error ) ) throw new TextErrorException ( error );
    }

    public abstract void handleError ( @NotNull TextError error, @NotNull String message );

    public @Nullable TextError firstErrorOrWarning() {

        if ( firstError != null ) return firstError;
        return firstWarning;
    }

    public @Nullable TextError lastErrorOrWarning() {

        if ( lastError != null ) return lastError;
        return lastWarning;
    }

    public boolean hasNewErrors ( @Nullable TextError initialLastError ) {

        return this.lastError != initialLastError;
    }

    public void throwIfNewErrors ( @Nullable TextError initialLastError ) throws TextErrorException {

        if ( ! hasNewErrors ( initialLastError ) ) return;

        TextError newError = this.lastError;
        assert newError != null;
        throw new TextErrorException ( newError );
    }

    /*
    public void setMark() {

        marked = true;
        firstErrorAfterMark = null;
    }

    public @Nullable TextError firstErrorAfterMark() { return firstErrorAfterMark; }
    */
}
