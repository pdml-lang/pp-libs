package dev.pp.datatype.nonunion.scalar.impls.Boolean;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.nonunion.scalar.ScalarDataType;
import dev.pp.datatype.nonunion.scalar.impls.Null.NullDataType;
import dev.pp.datatype.utils.parser.DataParserException;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.location.TextLocation;

import java.util.List;
import java.util.function.Supplier;

public class BooleanDataType extends ScalarDataType<Boolean> {

    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";
    public static final String YES_VALUE = "yes";
    public static final String NO_VALUE = "no";

    public static @NotNull List<String> validStringValues_() {
        return List.of ( YES_VALUE, NO_VALUE, TRUE_VALUE, FALSE_VALUE );
    }

    public static @NotNull String validValuesAsString_ ( @NotNull String separator ) {
        return String.join ( separator, validStringValues_() ) + " (case-insensitive)";
    }

    public static final @NotNull BooleanDataType INSTANCE = new BooleanDataType (
        "boolean",
        null,
        () -> new SimpleDocumentation (
            "Boolean",
            "A boolean value. Valid values are: " + validValuesAsString_ ( "," ) + ".",
            "true" ) );


    public BooleanDataType (
        @NotNull String name,
        @Nullable DataValidator<Boolean> validator,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        super ( name, validator, documentation );
    }


    @NotNull public Boolean parse (
        @Nullable String string,
        @Nullable TextLocation location ) throws DataParserException {

        NullDataType.checkNotNullString ( string, location );

        assert string != null;
        String lstring = string.toLowerCase();

        if ( lstring.equals ( YES_VALUE ) || lstring.equals ( TRUE_VALUE ) ) {
            return true;

        } else if ( lstring.equals ( NO_VALUE ) || lstring.equals ( FALSE_VALUE ) ) {
            return false;

        } else {
            throw new DataParserException (
                "'" + string + "' is an invalid boolean value. Valid values are: " + validValuesAsString() + ".",
                "ILLEGAL_BOOLEAN_VALUE",
                string, location );
        }
    }


    @Override
    public @Nullable String validValuesAsString ( @NotNull String separator ) {
        return validValuesAsString_ ( separator );
    }

    @Override
    public @NotNull List<String> validStringValues() { return validStringValues_(); }

    @Override
    public @NotNull List<Boolean> validValues() { return List.of ( true, false ); }
}
