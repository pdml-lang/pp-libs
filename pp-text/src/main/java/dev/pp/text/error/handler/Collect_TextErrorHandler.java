package dev.pp.text.error.handler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.text.error.TextError;

import java.util.ArrayList;
import java.util.List;

public class Collect_TextErrorHandler extends AbstractTextErrorHandler {


    private final @NotNull List<TextError> errors;


    public Collect_TextErrorHandler () {

        this.errors = new ArrayList<>();
    }


    public void handleError ( @NotNull TextError error, @NotNull String message ) { errors.add ( error ); }


    // queries

/* TODO
    public @Nullable List<TextError> errorsAndWarnings() {

        if ( hasErrors() || hasWarnings() ) {
            List<TextErrorOrWarningOld> list = new ArrayList<>();
            list.addAll ( errors );
            list.addAll ( warnings );
            return list;
        } else {
            return null;
        }
    }

    public @Nullable List<TextErrorOld> errors() {

        if ( hasErrors() ) {
            return Collections.unmodifiableList ( errors );
        } else {
            return null;
        }
    }

    public @Nullable List<TextWarningOld> warnings() {

        if ( hasWarnings() ) {
            return Collections.unmodifiableList ( warnings );
        } else {
            return null;
        }
    }


    public TextErrorOrWarningOld firstErrorOrWarning() {

        if ( hasErrors() ) {
            return errors.get ( 0 );
        } else if ( hasWarnings() ) {
            return warnings.get ( 0 );
        } else {
            return null;
        }
    }

    @Override
    public TextErrorOld firstError() {

        if ( hasErrors() ) {
            return errors.get ( 0 );
        } else {
            return null;
        }
    }

    public TextWarningOld firstWarning() {

        if ( hasWarnings() ) {
            return warnings.get ( 0 );
        } else {
            return null;
        }
    }



    public boolean hasErrorsOrWarnings() { return hasErrors() || hasWarnings(); }

    public boolean hasErrors() { return ! errors.isEmpty(); }

    public boolean hasWarnings() { return ! warnings.isEmpty(); }


    public int errorOrWarningCount() { return errorCount() + warningCount(); }

    public int errorCount() { return errors.size(); }

    public int warningCount() { return warnings.size(); }
*/
}
