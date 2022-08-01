package dev.pp.datatype.nonUnion.scalar.impls.Null;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.datatype.nonUnion.scalar.ScalarDataType;
import dev.pp.datatype.utils.parser.DataParserException;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.token.TextToken;

import java.util.function.Supplier;

public class NullDataType  extends ScalarDataType<Void> {

    public static final @NotNull String NULL_STRING = StringConstants.NULL_AS_STRING;
    // public static final @NotNull String NULL_STRING_2 = "#null#";

    public static final @NotNull NullDataType INSTANCE = new NullDataType (
        "null",
        null,
        () -> new SimpleDocumentation (
            "Null", "No value available.", "\"\"" ) );

    public static boolean isNullString ( @Nullable String string ) {

        return string == null
            || string.isEmpty();
            // || string.equalsIgnoreCase ( NULL_STRING );
    }

    public static void checkNotNullString (
        @Nullable String string,
        @Nullable TextToken token ) throws DataParserException {

        if ( isNullString ( string ) ) throw new DataParserException (
            "NULL_NOT_ALLOWED",
            "An empty value or null is not allowed.",
            token );
    }


    private NullDataType (
        @NotNull String name,
        @Nullable DataValidator<Void> validator,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        super ( name, validator, documentation );
    }


    @Override
    public boolean isNullable() { return true; }

    @NotNull public Void parse (
        @Nullable String string,
        @Nullable TextToken token ) throws DataParserException {

        if ( isNullString ( string ) ) {
            return null;
        } else {
            throw new DataParserException (
                "INVALID_NULL_VALUE",
                "'" + string + "' is not a null value.",
                token );
        }
    }

    @Override
    public @NotNull String objectToString ( @Nullable Void object ) { return NULL_STRING; }
}
