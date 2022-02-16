package dev.pp.datatype.common.date;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.DataType;
import dev.pp.datatype.parser.DataParser;
import dev.pp.datatype.parser.DataParserException;
import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.writer.DataWriter;
import dev.pp.text.documentation.SimpleDocumentation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Supplier;

public class Date_DataType {

    public static final String DEFAULT_NAME = "date";

    public static DataParser<LocalDate> DEFAULT_PARSER = ( string, token ) -> {

        try {
            return LocalDate.parse ( string );
        } catch ( DateTimeParseException e ) {
            throw new DataParserException (
                "ILLEGAL_DATE_VALUE",
                "'" + string + "' is an invalid date value. Reason: " + e.getMessage(),
                token, null );
        }
    };

    public static DataWriter<LocalDate> DEFAULT_WRITER = ( value, writer, nullString ) -> {

        if ( value != null ) {
            writer.write ( value.toString() );
        } else {
            writer.write ( nullString );
        }
    };

    public static SimpleDocumentation DEFAULT_DOCUMENTATION = new SimpleDocumentation (
    "Date", "A date value.", "1999-12-31" );

    public static DataType<LocalDate> create (
        @NotNull String name,
        boolean isNullable,
        @Nullable DataParser<LocalDate> parser,
        @Nullable DataWriter<LocalDate> writer,
        @Nullable DataValidator<LocalDate> validator,
        @Nullable Supplier<LocalDate> defaultValueSupplier,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        return new DataType<> (
            name, isNullable, parser, writer, validator, defaultValueSupplier, documentation );
    }

    public static DataType<LocalDate> createNonNullable ( LocalDate defaultValue ) {

        return create (
            DEFAULT_NAME, false, DEFAULT_PARSER, DEFAULT_WRITER,
            null, () -> defaultValue, () -> DEFAULT_DOCUMENTATION );
    }

    public static DataType<LocalDate> createNullable ( @Nullable LocalDate defaultValue ) {

        return create (
            DEFAULT_NAME, true, DEFAULT_PARSER, DEFAULT_WRITER,
            null, () -> defaultValue, () -> DEFAULT_DOCUMENTATION );
    }
}
