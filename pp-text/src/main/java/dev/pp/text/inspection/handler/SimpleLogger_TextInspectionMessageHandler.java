package dev.pp.text.inspection.handler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.utilities.SimpleLogger;
import dev.pp.text.inspection.message.TextInspectionMessage;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.inspection.message.TextInfo;
import dev.pp.text.inspection.message.TextWarning;

import java.util.function.Function;

public class SimpleLogger_TextInspectionMessageHandler extends AbstractTextInspectionMessageHandler {


    public SimpleLogger_TextInspectionMessageHandler ( @NotNull Function<TextInspectionMessage, String> inspectionToStringConverter ) {
        super ( inspectionToStringConverter );
    }

    public void handle ( @NotNull TextInspectionMessage message, @NotNull String text ) {

        /* Use this code when this is no more a preview feature in Java
        switch ( message ) {
            case TextError ignored -> SimpleLogger.error ( text );
            case TextWarning ignored -> SimpleLogger.warning ( text );
            case TextInfo ignored -> SimpleLogger.info ( text );
            default -> SimpleLogger.info ( text );
        }
         */

        if ( message instanceof TextError ) {
            SimpleLogger.error ( text );
        } else if ( message instanceof TextWarning ) {
            SimpleLogger.warning ( text );
        } else if ( message instanceof TextInfo ) {
            SimpleLogger.info ( text );
        } else {
            SimpleLogger.info ( text );
        }
    }
}
