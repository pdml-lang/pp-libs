package dev.pp.text.inspection.handler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.inspection.message.TextInspectionMessage;

import java.util.function.Function;

public abstract class AbstractTextInspectionMessageHandler implements TextInspectionMessageHandler {


    protected final @NotNull Function<TextInspectionMessage, String> inspectionToStringConverter;

    protected @Nullable TextError firstError;
    public TextError firstError() { return firstError; }

    protected @Nullable TextError lastError;
    public TextError lastError() { return lastError; }

//    protected @Nullable TextError firstErrorAfterMark;
//    protected boolean hasErrorMark;


    protected AbstractTextInspectionMessageHandler (
        @NotNull Function<TextInspectionMessage, String> inspectionToStringConverter ) {

        this.inspectionToStringConverter = inspectionToStringConverter;

        this.firstError = null;
        this.lastError = null;
    }

    protected AbstractTextInspectionMessageHandler () {
        this ( TextInspectionMessage::toString );
    }


    public void handleMessage ( @NotNull TextInspectionMessage message ) {

        if ( message instanceof TextError error ) {
            if ( firstError == null ) firstError = error;
            lastError = error;
            error.setHasBeenHandled ( true );
        }

        handle ( message, inspectionToStringConverter.apply ( message ) );
    }

    public abstract void handle ( @NotNull TextInspectionMessage message, @NotNull String text );

    public void throwIfNewErrors ( @Nullable TextError initialLastError ) throws TextErrorException {

        if ( ! hasNewErrors ( initialLastError ) ) return;

        assert lastError != null;
        throw new TextErrorException ( lastError );
    }

    private boolean hasNewErrors ( @Nullable TextError initialLastError ) {
        return lastError != initialLastError;
    }

/*
    public void handleError ( @NotNull TextError error ) { handleMessage ( error ); }

    public void handleWarning ( @NotNull TextWarning warning ) { handleMessage ( warning ); }

    public void handleInfo ( @NotNull TextInfo info ) { handleMessage ( info ); }

    public void setErrorMark() {

        hasErrorMark = true;
        firstErrorAfterMark = null;
    }

    public @Nullable TextError firstErrorAfterMark() { return firstErrorAfterMark; }

    public @Nullable TextError firstErrorOrWarning() {

        if ( firstError != null ) return firstError;
        return firstWarning;
    }

    public @Nullable TextError lastErrorOrWarning() {

        if ( lastError != null ) return lastError;
        return lastWarning;
    }

    public boolean hasErrors() {
        return this.firstError != null;
    }

    public void throwIfErrors() throws TextErrorException {

        TextError firstError = this.firstError;
        if ( firstError != null )
            throw new TextErrorException ( firstError );
    }

 */
}
