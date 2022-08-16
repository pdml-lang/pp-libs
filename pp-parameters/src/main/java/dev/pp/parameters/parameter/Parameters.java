package dev.pp.parameters.parameter;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.textTokenParameter.TextTokenParameter;
import dev.pp.parameters.textTokenParameter.TextTokenParameters;
import dev.pp.text.token.TextToken;

import java.util.*;

public class Parameters {


    private final @NotNull Map<String, Object> map;
    private final @Nullable TextTokenParameters textTokenParameters;


    public Parameters ( boolean maintainOrder, @Nullable TextTokenParameters textTokenParameters ) {

        if ( maintainOrder ) {
            this.map = new LinkedHashMap<>();
        } else {
            this.map = new HashMap<>();
        }

        this.textTokenParameters = textTokenParameters;
    }

    public Parameters ( @Nullable TextTokenParameters textTokenParameters ) {
        this ( true, textTokenParameters );
    }


    // lists

    public Collection<Object> getValues() { return map.values(); }

    // TODO public Collection<Parameter<T>> getListSortedByName() { return map.values(); }

    // TODO public Collection<Parameter<T>> getListSortedByFormalParameterOrder() { return map.values(); }

    public Set<String> getNames() { return map.keySet(); }

    // TODO public Set<String> getSortedNames() { return map.keySet(); }


    // contains

    public boolean containsName ( @NotNull String name ) { return map.containsKey ( name ); }

    // public boolean containsParameter ( @NotNull Parameter<T> parameter ) { return containsName ( parameter.getName() ); }


    // Value getters

    public @Nullable <V> V getNullable ( @NotNull String name ) {

        checkExists ( name );
        @SuppressWarnings ( "unchecked" )
        V value = (V) map.get ( name );
        return value;
    }

    public @Nullable <V> V getNullable ( @NotNull FormalParameter<?> formalParameter ) {

        return getNullable ( formalParameter.getName() );
    }

    public @NotNull <V> V getNonNull ( @NotNull String name ) {

        V value = getNullable ( name );
        if ( value == null )
            throw new IllegalArgumentException ( "'" + name + "' is null, but supposed to be non-null.");
        return value;
    }

    public @NotNull <V> V getNonNull ( @NotNull FormalParameter<?> formalParameter ) {

        return getNonNull ( formalParameter.getName() );
    }

    public @Nullable <V> V getOrNull ( @NotNull String name ) {

        @SuppressWarnings ( "unchecked" )
        V value = (V) map.get ( name );
        return value;
    }

    public @NotNull <V> V getNonNullOrDefault ( @NotNull String name, @NotNull V defaultValue ) {

        if ( containsName ( name ) ) {
            return getNonNull ( name );
        } else {
            return defaultValue;
        }
    }

    public @Nullable <V> V getNullableOrDefault ( @NotNull String name, @Nullable V defaultValue ) {

        if ( containsName ( name ) ) {
            return getNullable ( name );
        } else {
            return defaultValue;
        }
    }

    public @Nullable TextToken getStartToken() {

        return textTokenParameters == null ? null : textTokenParameters.getStartToken();
    }

    public @Nullable TextToken getNameToken ( @NotNull String name ) {

        if ( textTokenParameters == null ) return null;
        @Nullable TextTokenParameter parameter = textTokenParameters.getOrNull ( name );
        return parameter == null ? null : parameter.getNameToken();
    }

    public @Nullable TextToken getValueToken ( @NotNull String name ) {

        if ( textTokenParameters == null ) return null;
        @Nullable TextTokenParameter parameter = textTokenParameters.getOrNull ( name );
        return parameter == null ? null : parameter.getValueToken();
    }



        // add

    /*
    public MixedParameters<T> add ( @NotNull Parameter<T> parameter ) {

        String name = parameter.getName();
        checkNotExists ( name );
        map.put ( name, parameter );

        return this;
    }
    */

    public Parameters add ( @NotNull String name, @Nullable Object value ) {

        checkNotExists ( name );
        map.put ( name, value );

        return this;
    }

    /*
    public void addOrReplace ( @NotNull Parameter<T> parameter ) {

        map.put ( parameter.getName(), parameter );
    }
    */

    public Parameters addOrReplace ( @NotNull String name, @Nullable Object value ) {

        map.put ( name, value );

        return this;
    }


    // replace

    /*
    public MixedParameters<T> replace ( @NotNull Parameter<T> parameter ) {

        String name = parameter.getName();
        checkExists ( name );
        map.put ( name, parameter );

        return this;
    }

    public MixedParameters<T> replace ( @NotNull String name, T value ) {

        checkExists ( name );
        map.put ( name, new Parameter<> ( name, value ) );

        return this;
    }

    public boolean replaceIfExists ( @NotNull Parameter<T> parameter ) {

        String name = parameter.getName();
        if ( containsName ( name ) ) {
            map.put ( name, parameter );
            return true;
        } else {
            return false;
        }
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
    */


    private void checkExists ( @NotNull String name ) {

       if ( ! containsName ( name ) ) throw new IllegalArgumentException (
           "Parameter '" + name + "' does not exist." );
    }

    private void checkNotExists ( @NotNull String name ) {

        if ( containsName ( name ) ) throw new IllegalArgumentException (
            "Parameter '" + name + "' exists already." );
    }

    @Override
    public String toString() { return map.toString(); }
}
