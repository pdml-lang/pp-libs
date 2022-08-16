package dev.pp.parameters.textTokenParameter;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.nonUnion.scalar.impls.Boolean.BooleanDataType;
import dev.pp.parameters.cli.token.NameOrValueToken;
import dev.pp.parameters.cli.token.NameToken;
import dev.pp.parameters.cli.token.ValueToken;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.FormalParameters;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

import java.util.*;

public class TextTokenParameters {


    private static final @NotNull TextToken DEFAULT_VALUE_TOKEN =
        new TextToken ( BooleanDataType.TRUE_VALUE, null );


    public static @NotNull TextTokenParameters createFromNameOrValueTokens (
        @NotNull List<NameOrValueToken> nameOrValueTokens,
        @Nullable TextToken startToken,
        @NotNull FormalParameters formalParameters ) throws TextErrorException {

        TextTokenParameters result = new TextTokenParameters ( startToken );
        @Nullable NameToken pendingNameToken = null;

        for ( NameOrValueToken token : nameOrValueTokens ) {

            if ( token instanceof NameToken nameToken ) {
                if ( pendingNameToken != null ) {
                    // A name without a value is considered to be a boolean flag set to true
                    // e.g. -flag1 -flag2
                    result.add ( pendingNameToken, DEFAULT_VALUE_TOKEN );
                }
                if ( ! formalParameters.containsName ( nameToken.getText() ) ) {
                    throw new TextErrorException (
                        "INVALID_PARAMETER",
                        "Parameter '" + nameToken.getText() + "' doesn't exist.",
                        nameToken );
                }
                pendingNameToken = nameToken;

            } else if ( token instanceof ValueToken valueToken ) {
                @Nullable Integer position = valueToken.getPosition();
                if ( position == null ) {
                    if ( pendingNameToken == null ) {
                        throw new TextErrorException (
                            "PARAMETER_NAME_MISSING",
                            "Parameter name is required for value '" + valueToken.getText() + "'.",
                            valueToken );
                    }
                    @NotNull String name = pendingNameToken.getText();
                    if ( result.containsName ( name ) ) {
                        throw new TextErrorException (
                            "PARAMETER_EXISTS_ALREADY",
                            "Parameter '" + name + "' has already been defined.",
                            pendingNameToken );
                    }
                    result.add ( pendingNameToken, valueToken );

                } else {
                    if ( pendingNameToken != null ) {
                        result.add ( pendingNameToken, DEFAULT_VALUE_TOKEN );
                    }
                    int positionalParametersCount = formalParameters.getPositionalParametersCount();
                    if ( position >= positionalParametersCount ) {
                        throw new TextErrorException (
                            "TOO_MANY_POSITIONAL_PARAMETERS",
                            "Parameter value '" + valueToken.getText() + "' at position " + position + 1 +
                                " is invalid, because there are only " +
                                positionalParametersCount + " positional parameters.",
                            valueToken );
                    }
                    @Nullable FormalParameter<?> formalParameter =
                        formalParameters.getPositionalParameterWithPosition ( position );
                    if ( formalParameter == null ) {
                        throw new TextErrorException (
                            "INVALID_POSITIONAL_PARAMETER",
                            "Parameter value '" + valueToken.getText() +
                                "' is invalid, because there is no positional parameter at position " + position + 1 + ".",
                            valueToken );
                    }
                    TextToken nameToken = new TextToken ( formalParameter.getName(), null );
                    result.add ( nameToken, valueToken );
                }
                pendingNameToken = null;

            } else {
                throw new RuntimeException ( "Unexpected NameOrValueToken " + token );
            }
        }

        if ( pendingNameToken != null ) {
            result.add ( pendingNameToken, DEFAULT_VALUE_TOKEN );
        }

        return result;
    }


    public static @NotNull TextTokenParameters merge (
        @NotNull List<TextTokenParameters> parametersList,
        @NotNull FormalParameters formalParameters ) {

        TextTokenParameters result = new TextTokenParameters ( null );

        for ( TextTokenParameters parameters : parametersList ) {
            for ( TextTokenParameter parameter : parameters.getList() ) {

                // result.addOrReplace ( parameter );

                // use the formal parameter's standard name in all cases
                // to avoid double entries when different alternative names are used
                String parameterName = parameter.getName();
                FormalParameter<?> formalParameter = formalParameters.getOrNull ( parameterName );
                String formalParameterName = formalParameter == null ? null : formalParameter.getName();
                if ( formalParameterName == null || formalParameterName.equals ( parameterName ) ) {
                    result.addOrReplace ( parameter );
                } else {
                    result.addOrReplace (
                        new TextToken (
                            formalParameterName,
                            parameter.getNameToken().getLocation() ),
                        parameter.getValueToken() );
                }
            }
        }

        return result;
    }


    private final @NotNull Map<String, TextTokenParameter> map;
    private final @Nullable TextToken startToken;


    public TextTokenParameters ( @Nullable TextToken startToken ) {

        this.startToken = startToken;
        this.map = new LinkedHashMap<>();
    }

    /*
    public TextTokenParameters() {

        this ( (TextToken) null );
    }
    */

    public TextTokenParameters ( @NotNull Map<String, String> stringMap, @Nullable TextToken startToken ) {

        this.map = new LinkedHashMap<>();
        this.startToken = startToken;

        for ( Map.Entry<String, String> entry : stringMap.entrySet() ) {
            this.add ( entry.getKey(), entry.getValue() );
        }
    }


    public @Nullable TextToken getStartToken() { return startToken; }


    public @NotNull Collection<TextTokenParameter> getList() { return map.values(); }

    public @NotNull Set<String> getNames() { return map.keySet(); }

    public boolean isEmpty() { return map.isEmpty(); }

    public int getCount() { return map.size(); }

    // contains

    public boolean containsName ( @NotNull String name ) { return map.containsKey ( name ); }

    public boolean containsParameter ( @NotNull TextTokenParameter parameter ) { return containsName ( parameter.getName() ); }


    public @NotNull TextTokenParameter get ( String name ) {

        checkExists ( name );
        return map.get ( name );
    }

    public @Nullable TextTokenParameter getOrNull ( String name ) { return map.get ( name ); }

    public @Nullable String getValue ( String name ) { return get ( name ).getValue(); }

    public @Nullable String getValueOrNull ( String name ) {

        TextTokenParameter parameter = getOrNull ( name );
        if ( parameter != null ) {
            return parameter.getValue ();
        } else {
            return null;
        }
    }


    public void add ( @NotNull TextTokenParameter parameter ) {

        checkNotExists ( parameter.getName() );

        addOrReplace ( parameter );
    }

    public void add ( @NotNull TextToken nameToken, @Nullable TextToken valueToken ) {

        add ( new TextTokenParameter ( nameToken, valueToken ) );
    }

    public void add ( @NotNull String name, @Nullable String value ) {

        add (
            new TextToken ( name, null ),
            value == null || value.isEmpty() ? null : new TextToken ( value, null ) );
    }

    public void addOrReplace ( @NotNull TextTokenParameter parameter ) {

        map.put ( parameter.getName(), parameter );
    }

    public void addOrReplace ( @NotNull TextToken nameToken, @Nullable TextToken valueToken ) {

        addOrReplace ( new TextTokenParameter ( nameToken, valueToken ) );
    }


    // Remove

    public void remove ( @NotNull String name ) {

        checkExists ( name );
        map.remove ( name );
    }

    public boolean removeIfExists ( @NotNull String name ) {

        if ( containsName ( name ) ) {
            map.remove ( name );
            return true;
        } else {
            return false;
        }
    }


    // Checks

    private void checkExists ( @NotNull String name ) {

        if ( ! containsName ( name ) ) throw new IllegalArgumentException (
            "Parameter '" + name + "' does not exist." );
    }

    private void checkNotExists ( @NotNull String name ) {

        if ( containsName ( name ) ) throw new IllegalArgumentException (
            "Parameter '" + name + "' exists already." );
    }


    public @Nullable Map<String, String> toMap() {

        Map<String, String> map = new LinkedHashMap<>();
        getList().forEach ( p -> {
            map.put ( p.getName(), p.getValue() );
        });
        return map.isEmpty() ? null : map;
    }

    public @NotNull String toString() {

        Map<String, String> stringMap = toMap();
        if ( stringMap != null ) {
            return stringMap.toString ();
        } else {
            return "null";
        }
    }
}
