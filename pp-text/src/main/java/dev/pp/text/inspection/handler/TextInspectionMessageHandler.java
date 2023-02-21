package dev.pp.text.inspection.handler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.inspection.message.TextInspectionMessage;

public interface TextInspectionMessageHandler {

//    void start();

//    void stop();

    void handleMessage ( @NotNull TextInspectionMessage message );

/*
    void handleError ( @NotNull TextError error );
    void handleWarning ( @NotNull TextWarning warning );
    void handleInfo ( @NotNull TextInfo info );

    void setErrorMark();
    @Nullable TextError firstErrorAfterMark();
 */

    @Nullable TextError firstError();
    @Nullable TextError lastError();

/*
    @Nullable TextError firstErrorOrWarning();
    @Nullable TextError lastErrorOrWarning();

    @Nullable TextError firstWarning();
    @Nullable TextError lastWarning();

    boolean hasErrors();

    boolean hasNewErrors ( @Nullable TextError initialLastError );

    void throwIfErrors() throws TextErrorException;

*/
    void throwIfNewErrors ( @Nullable TextError initialLastError ) throws TextErrorException;

    /*
    void setMark();

    @Nullable TextError firstErrorAfterMark();
    */
}
