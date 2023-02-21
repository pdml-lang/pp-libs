package dev.pp.parameters.parameterspec;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.CommonDataTypes;
import dev.pp.datatype.DataType;
import dev.pp.datatype.utils.parser.DataParserException;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ParameterSpec<T> {


    public static <T> Builder<T> builder (
        @NotNull String name,
        @NotNull DataType<T> type ) {

        return new Builder<> ( name, type );
    }


    public static class Builder<T> {

        private final @NotNull String name;

        private @Nullable Set<String> alternativeNames;

        private final @NotNull DataType<T> type;

        private @Nullable Supplier<T> defaultValueSupplier;

        private @Nullable Integer sortIndex;

        /**
         * The position of the parameter. 'null' means it's a named parameter. Positions start at 0 (not 1).
         */
        private @Nullable Integer positionalParameterIndex;

        private @Nullable Supplier<SimpleDocumentation> documentationSupplier;


        public Builder (
            @NotNull String name,
            @NotNull DataType<T> type ) {

            this.name = name;
            this.type = type;
            this.alternativeNames = null;
            this.defaultValueSupplier = null;
            this.sortIndex = null;
            this.positionalParameterIndex = null;
            this.documentationSupplier = null;
        }


        public Builder<T> alternativeNames ( @Nullable Set<String> alternativeNames ) {
            this.alternativeNames = alternativeNames;
            return this;
        }

        public Builder<T> alternativeName ( @NotNull String alternativeName ) {
            this.alternativeNames = Set.of ( alternativeName );
            return this;
        }

        public Builder<T> defaultValueSupplier ( @Nullable Supplier<T> defaultValueSupplier ) {
            this.defaultValueSupplier = defaultValueSupplier;
            return this;
        }

        public Builder<T> defaultValue ( @Nullable T defaultValue ) {
            this.defaultValueSupplier = () -> defaultValue;
            return this;
        }

        public Builder<T> sortIndex ( @Nullable Integer sortIndex ) {
            this.sortIndex = sortIndex;
            return this;
        }

        public Builder<T> positionalParameterIndex ( @Nullable Integer positionalParameterIndex ) {
            this.positionalParameterIndex = positionalParameterIndex;
            return this;
        }

        public Builder<T> documentationSupplier ( @Nullable Supplier<SimpleDocumentation> documentationSupplier ) {
            this.documentationSupplier = documentationSupplier;
            return this;
        }

        public Builder<T> documentation ( @Nullable SimpleDocumentation documentation ) {
            this.documentationSupplier = () -> documentation;
            return this;
        }

        public Builder<T> documentation (
            @NotNull String title,
            @Nullable String description,
            @Nullable String examples ) {

            return documentation ( new SimpleDocumentation ( title, description, examples ) );
        }


        public ParameterSpec<T> build() {

            return new ParameterSpec<> (
                name,
                alternativeNames,
                type,
                defaultValueSupplier,
                sortIndex,
                positionalParameterIndex,
                documentationSupplier );
        }
    }


    private final @NotNull String name;
    public @NotNull String getName() { return name; }

    private final @Nullable Set<String> alternativeNames;
    public @Nullable Set<String> getAlternativeNames() { return alternativeNames; }

    private final @NotNull DataType<T> type;
    public @NotNull DataType<T> getType() { return type; }

    private final @Nullable Supplier<T> defaultValueSupplier;
    public @Nullable Supplier<T> getDefaultValueSupplier() { return defaultValueSupplier; }

    private final @Nullable Integer sortIndex;
    public @Nullable Integer getSortIndex() { return sortIndex; }

    private final @Nullable Integer positionalParameterIndex; // first is 1 !!!
    public @Nullable Integer getPositionalParameterIndex() { return positionalParameterIndex; }

    private final @Nullable Supplier<SimpleDocumentation> documentationSupplier;
    public @Nullable Supplier<SimpleDocumentation> getDocumentationSupplier() { return documentationSupplier; }


    public ParameterSpec (
        @NotNull String name,
        @Nullable Set<String> alternativeNames,
        @NotNull DataType<T> type,
        @Nullable Supplier<T> defaultValueSupplier,
        @Nullable Integer sortIndex,
        @Nullable Integer positionalParameterIndex,
        @Nullable Supplier<SimpleDocumentation> documentationSupplier ) {

        if ( alternativeNames != null && alternativeNames.contains ( name ) ) throw new IllegalArgumentException (
            "Name '" + name + "' cannot be re-used in the list of alternative names." );

        this.name = name;
        this.alternativeNames = alternativeNames;
        this.type = type;
        this.defaultValueSupplier = defaultValueSupplier;
        this.sortIndex = sortIndex;
        this.positionalParameterIndex = positionalParameterIndex;
        this.documentationSupplier = documentationSupplier;
    }

    public static @NotNull ParameterSpec<String> ofString (
        @NotNull String name,
        @Nullable Supplier<String> defaultValueSupplier ) {

        return new ParameterSpec<> (
            name, null, CommonDataTypes.STRING, defaultValueSupplier, null, null, null );
    }


    // Name

    public @NotNull Set<String> allNames () {

        var list = new HashSet<String>();
        list.add ( name );
        if ( alternativeNames != null ) list.addAll ( alternativeNames );
        return list;
    }

    public int sortIndexOrZero () { return sortIndex == null ? 0 : sortIndex; }

    public int positionalIndexOrZero () { return positionalParameterIndex == null ? 0 : positionalParameterIndex; }


    // Type

    public @NotNull String typeName () { return type.getName(); }

    public boolean isNullable() { return type.isNullable(); }

    public @Nullable DataValidator<T> validator () { return type.getValidator(); }

    public void validate ( @Nullable T object, @Nullable TextToken token ) throws DataValidatorException {
        type.validate ( object, token );
    }


    // Default value

    public boolean hasDefaultValue() { return defaultValueSupplier != null; }

    public boolean isRequired() { return defaultValueSupplier == null; }

    public T defaultValue () {

        if ( defaultValueSupplier != null ) {
            return defaultValueSupplier.get ();
        } else {
            throw new IllegalCallerException ( "No default value is provided for parameter '" + name + "'." );
        }
    }

    public T defaultValueOrElse ( T elseValue ) {

        if ( defaultValueSupplier != null ) {
            return defaultValueSupplier.get();
        } else {
            return elseValue;
        }
    }

    public T defaultValueOrNull() {

        if ( defaultValueSupplier != null ) {
            return defaultValueSupplier.get();
        } else {
            return null;
        }
    }

    public @Nullable String defaultValueAsString () {

        if ( ! hasDefaultValue() ) return null;

        T defaultValue = defaultValue ();
        return defaultValue == null ? "null" : defaultValue.toString();
    }


    // Positional

    public boolean isPositionalParameter() { return positionalParameterIndex != null; }


    // Documentation

    public @Nullable SimpleDocumentation documentation () {
        return documentationSupplier != null ? documentationSupplier.get() : null;
    }

    public @Nullable String documentationTitle () {
        SimpleDocumentation documentation = documentation ();
        return documentation != null ? documentation.getTitle() : null;
    }

    public @Nullable String documentationDescription () {
        SimpleDocumentation documentation = documentation ();
        return documentation != null ? documentation.getDescription() : null;
    }

    public @Nullable String documentationExamples () {
        SimpleDocumentation documentation = documentation ();
        return documentation != null ? documentation.getExamples() : null;
    }


    // Parse

    public @Nullable T parse ( @Nullable String string, @Nullable TextLocation location )
        throws DataParserException, DataValidatorException {

        return type.parseAndValidate ( string, location );
    }

    public @Nullable T parse ( @NotNull TextToken token )
        throws DataParserException, DataValidatorException {

        return type.parseAndValidate ( token );
    }


    // Write

    public @NotNull String objectToString ( @Nullable T object ) {
        return type.objectToString ( object );
    }

    public void writeObject ( @Nullable T object, @NotNull Writer writer ) throws IOException {
        type.writeObject ( object, writer );
    }


    @Override public String toString() { return name; }
}
