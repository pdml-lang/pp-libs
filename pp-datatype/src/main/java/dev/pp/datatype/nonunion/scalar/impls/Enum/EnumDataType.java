package dev.pp.datatype.nonunion.scalar.impls.Enum;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.nonunion.scalar.ScalarDataType;
import dev.pp.datatype.nonunion.scalar.impls.Null.NullDataType;
import dev.pp.datatype.utils.parser.DataParserException;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.location.TextLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/*
enum Size {
    SMALL, MEDIUM, BIG
}
T: Size
 */
public class EnumDataType<T extends Enum<T>> extends ScalarDataType<T> {


    private final @NotNull Class<T> clazz;


    public EnumDataType (
        @NotNull Class<T> clazz,
        @NotNull String name,
        @Nullable DataValidator<T> validator,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        super ( name, validator, documentation );
        this.clazz = clazz;
    }

    public EnumDataType (
        @NotNull Class<T> clazz ) {

        this ( clazz,
            clazz.getSimpleName ().toLowerCase (),
            null,
            null );
    }


    public @Nullable SimpleDocumentation getDocumentation() {

        if ( documentation != null ) {
            return documentation.get();
        } else {
            return new SimpleDocumentation (
                getName(),
                "A fixed set of enumerated values. Valid values are: " + validValuesAsString(),
                validStringValues().get ( 0 ) );
        }
    }


    @NotNull public T parse (
        @Nullable String string,
        @Nullable TextLocation location ) throws DataParserException {

        NullDataType.checkNotNullString ( string, location );
        assert string != null;

        try {
            return Enum.valueOf ( clazz, string.toUpperCase() );
        } catch ( IllegalArgumentException e ) {
            throw new DataParserException (
                "Value '" + string + "' is invalid. Valid values are: " + validValuesAsString() + ".",
                "INVALID_ENUM_VALUE",
                string, location,
                e );
        }
    }

    @Override
    public @NotNull String validValuesAsString ( @NotNull String separator ) {

        return String.join ( separator, validStringValues() ) + " (case-insensitive)";
    }

    @Override
    public @NotNull List<String> validStringValues() {

        return validValues()
            .stream()
            .map ( value -> value.toString().toLowerCase() )
            .collect ( Collectors.toList() );
    }

    @Override
    public @NotNull List<T> validValues() {

        List<T> result = new ArrayList<>();
        T[] values = clazz.getEnumConstants();
        Collections.addAll ( result, values );
        return result;
    }
}
