package dev.pp.datatype.nonunion.scalar.impls.integer;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.nonunion.scalar.ScalarDataType;
import dev.pp.datatype.nonunion.scalar.impls.Null.NullDataType;
import dev.pp.datatype.utils.parser.DataParserException;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.location.TextLocation;

import java.util.function.Supplier;

public class Integer32DataType extends ScalarDataType<Integer> {


    public static final @NotNull Integer32DataType DEFAULT = new Integer32DataType (
        "integer32",
        null,
        () -> new SimpleDocumentation (
            "32 Bits Integer", "A 32 bits signed integer number", "42" ));


    public Integer32DataType (
        @NotNull String name,
        @Nullable DataValidator<Integer> validator,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        super ( name, validator, documentation );
    }


    @NotNull public Integer parse (
        @Nullable String string,
        @Nullable TextLocation location ) throws DataParserException {

        NullDataType.checkNotNullString ( string, location );
        assert string != null;

        try {
            return Integer.parseInt ( string );
        } catch ( NumberFormatException e ) {
            throw new DataParserException (
                "'" + string + "' is an invalid integer number. Reason: " + e.getMessage(),
                "ILLEGAL_INTEGER_VALUE",
                string, location, e );
        }
    }
}
