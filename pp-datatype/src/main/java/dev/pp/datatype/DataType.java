package dev.pp.datatype;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.nonUnion.scalar.impls.Null.NullDataType;
import dev.pp.datatype.utils.parser.DataParserException;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface DataType<T> {

    @NotNull String getName();

    @Nullable SimpleDocumentation getDocumentation();


    // validation

    boolean isNullable();

    @Nullable DataValidator<T> getValidator();

    default void validate ( @Nullable T object, @Nullable TextToken token ) throws DataValidatorException {

        validateNull ( object, token );
        if ( object == null ) return;

        DataValidator<T> validator = getValidator();
        if ( validator != null ) {
            validator.validate ( object, token );
        }
    }

    default boolean isValid ( @Nullable T object ) {

        try {
            validate ( object, null );
            return true;
        } catch ( DataValidatorException e ) {
            return false;
        }
    }

    default void validateNull ( @Nullable T object, @Nullable TextToken token ) throws DataValidatorException {

        if ( object == null && ! isNullable() ) throw new DataValidatorException (
            "NULL_NOT_ALLOWED",
            "A value must be provided. 'null' (no value) is not allowed.",
            token,
            null );
    }

    default @Nullable String validValuesAsString() {

        return validValuesAsString ( ", " );
    }

    default @Nullable String validValuesAsString ( @NotNull String separator ) {

        @Nullable List<String> validStringValues = validStringValues();
        return validStringValues == null ? null : String.join ( separator, validStringValues );
    }

    default @Nullable List<String> validStringValues() {

        @Nullable List<T> validValues = validValues();
        return validValues == null ? null : validValues
            .stream()
            .map ( Object::toString )
            .collect ( Collectors.toList() );
    }

    default @Nullable List<T> validValues() { return null; }

    // TODO? default @Nullable List<T> exampleValues() { return null; }


    // parse

    @Nullable T parse ( @Nullable String string, @Nullable TextToken token ) throws DataParserException;

    default @Nullable T parseAndValidate ( @Nullable String string, @Nullable TextToken token )
        throws DataParserException, DataValidatorException {

        T object = parse ( string, token );
        validate ( object, token );
        return object;
    }


    // write

    default @NotNull String objectToString ( @Nullable T object ) {
        return object != null ? object.toString() : NullDataType.NULL_STRING;
    }

    default void writeObject ( @Nullable T object, @NotNull Writer writer ) throws IOException {
        writer.write ( objectToString ( object ) );
    }


    // TODO: compare
}
