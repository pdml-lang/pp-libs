package dev.pp.texttable.writer.pretty.utilities;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.os.OSIO;
import dev.pp.text.error.TextError;
import dev.pp.text.error.handler.Log_TextErrorHandler;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.error.handler.Write_TextErrorHandler;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.utilities.string.HTextAlign;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.text.utilities.text.TextMarker;
import dev.pp.texttable.data.impls.FormField;
import dev.pp.texttable.data.impls.FormFields;
import dev.pp.texttable.data.impls.FormFields_TableDataProvider;
import dev.pp.texttable.writer.pretty.PrettyTableDataTextWriterImpl;
import dev.pp.texttable.writer.pretty.config.PrettyTableDataTextWriterColumnConfig;
import dev.pp.texttable.writer.pretty.config.PrettyTableDataTextWriterConfig;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

public class TextErrorOrWarning_FormWriter {

    private static final int DEFAULT_LABEL_WIDTH = 10;
    private static final int DEFAULT_VALUE_WIDTH = 108;
    private static final int DEFAULT_WIDTH = DEFAULT_LABEL_WIDTH + 1 +DEFAULT_VALUE_WIDTH;


    public static TextErrorHandler createLogErrorHandler() {

        return new Log_TextErrorHandler ( TextErrorOrWarning_FormWriter::errorToString );
    }

    public static TextErrorHandler createWritingErrorHandler (
        @NotNull Writer writer,
        @Nullable String separator ) {

        return new Write_TextErrorHandler (
            writer,
            TextErrorOrWarning_FormWriter::errorToString,
            separator );
    }

    public static TextErrorHandler createStdoutErrorHandler() {

        return createWritingErrorHandler (
            OSIO.standardErrorUTF8Writer(),
            "-".repeat ( DEFAULT_WIDTH ) + StringConstants.OS_NEW_LINE );
    }


    public static @NotNull String errorToString ( @NotNull TextError error ) {

        StringWriter sw = new StringWriter();
        try {
            write ( error, sw );
        } catch ( Exception e ) {
            throw new RuntimeException ( e );
        }
        return sw.toString();
    }

    public static void write ( @NotNull TextError error, @NotNull Writer writer ) throws Exception {

        FormFields<String> formFields = createFormFields ( error );

        FormFields_TableDataProvider<String> data = new FormFields_TableDataProvider<String> ( formFields );

        List<PrettyTableDataTextWriterColumnConfig<String>> columnConfigs = List.of (
            new PrettyTableDataTextWriterColumnConfig<> ( DEFAULT_LABEL_WIDTH, HTextAlign.LEFT, null ),
            new PrettyTableDataTextWriterColumnConfig<> ( DEFAULT_VALUE_WIDTH, HTextAlign.LEFT, null ) );

        new PrettyTableDataTextWriterImpl<FormField<String>, String>().write (
            data,
            writer,
            new PrettyTableDataTextWriterConfig<> ( columnConfigs ) );
    }

    private static FormFields<String> createFormFields ( @NotNull TextError error ) throws Exception {

        FormFields<String> formFields = new FormFields<String>();

        // String label = ew.getLabel();
        // formFields.add ( label, ew.getMessage() );
        formFields.add ( "Message", error.getMessage() );

        @Nullable TextLocation location = error.getLocation();
        if ( location != null ) {

            @Nullable String code  = error.getTextLine();
            if ( code != null ) {
                int markerLength = error.getToken() == null ? 1 : error.getToken().getText().length();

                code = TextMarker.breakSingleTextLineAndInsertMarkerLine (
                    code, (int) location.getColumnNumber() - 1, TextMarker.DEFAULT_MARKER_IN_MARKER_LINE,
                    markerLength, true, 105 );
                formFields.add ( "Code", code );
            }

            formFields.add ( "Location", location.toString ( false, false ) );

            String parentLocations = location.parentLocationsToString ( false );
            if ( parentLocations != null ) {
                formFields.add ( "Call stack", parentLocations );
            }
        }

        String id = error.getId();
        if ( id != null ) {
            formFields.add ( error.getLabel() + " id", id );
        }

        return formFields;
    }
}
