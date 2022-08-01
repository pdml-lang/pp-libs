package dev.pp.datatype.nonUnion.scalar.impls.date;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.nonUnion.scalar.ScalarDataType;
import dev.pp.datatype.nonUnion.scalar.impls.Null.NullDataType;
import dev.pp.datatype.utils.parser.DataParserException;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.token.TextToken;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Supplier;

public class DateDataType extends ScalarDataType<LocalDate> {


    public static final @NotNull DateDataType DEFAULT = new DateDataType (
        "date",
        null,
        () -> new SimpleDocumentation (
            "Date", "A local date, like 2022-02-22.", "1999-12-31" ));


    public DateDataType (
        @NotNull String name,
        @Nullable DataValidator<LocalDate> validator,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        super ( name, validator, documentation );
    }


    @NotNull public LocalDate parse (
        @Nullable String string,
        @Nullable TextToken token ) throws DataParserException {

        NullDataType.checkNotNullString ( string, token );
        assert string != null;

        try {
            return LocalDate.parse ( string );
        } catch ( DateTimeParseException e ) {
            throw new DataParserException (
                "ILLEGAL_DATE_VALUE",
                "'" + string + "' is an invalid date. Reason: " + e.getMessage(),
                token );
        }
    }
}
