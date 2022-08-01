package dev.pp.parameters.textTokenParameter;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.text.token.TextToken;

import java.util.*;

public class TextTokenParameters {


    public static @NotNull TextTokenParameters merge (
        @NotNull List<TextTokenParameters> parametersList,
        @NotNull FormalParameters formalParameters ) {

        TextTokenParameters result = new TextTokenParameters ( (TextToken) null );

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
                        new TextToken ( formalParameterName, parameter.getNameToken().getLocation() ),
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
            new TextToken ( name ),
            value == null || value.isEmpty() ? null : new TextToken ( value ) );
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

        Map<String, String> map = new HashMap<>();
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
