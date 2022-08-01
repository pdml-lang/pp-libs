package dev.pp.parameters.parameter.list;

import dev.pp.parameters.parameter.Parameter;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.*;

@Deprecated
public class TypedParameters<T> {


    private final @NotNull Map<String, Parameter<T>> map;


    public TypedParameters ( boolean maintainOrder ) {

        if ( maintainOrder ) {
            this.map = new LinkedHashMap<>();
        } else {
            this.map = new HashMap<> ();
        }
    }

    public TypedParameters () { this ( true ); }


    // lists

    public Collection<Parameter<T>> getList() { return map.values(); }

    // TODO public Collection<Parameter<T>> getListSortedByName() { return map.values(); }

    // TODO public Collection<Parameter<T>> getListSortedByFormalParameterOrder() { return map.values(); }

    public Set<String> getNames() { return map.keySet(); }

    // TODO public Set<String> getSortedNames() { return map.keySet(); }


    // contains

    public boolean containsName ( @NotNull String name ) { return map.containsKey ( name ); }

    // public boolean containsParameter ( @NotNull Parameter<T> parameter ) { return containsName ( parameter.getName() ); }


    // Parameter getters

    /* TODO?
    public @NotNull Parameter<T> getParameter ( @NotNull String name ) {

        checkExists ( name );
        return map.get ( name );
    }
    */


    // Value getters

    public @Nullable T getNullableValue ( @NotNull String name ) {

        checkExists ( name );
        return map.get ( name ).getValue();
    }

    public @Nullable <V> V getCastedNullableValue ( @NotNull String name ) {

        @SuppressWarnings ( "unchecked" )
        V value = (V) getNullableValue ( name );
        return value;
    }

    public @NotNull T getNonNullValue ( @NotNull String name ) {

        T value = getNullableValue ( name );
        if ( value == null )
            throw new IllegalArgumentException ( "'" + name + "' is null, but supposed to be non-null.");
        return value;
    }

    public @NotNull <V> V getCastedNonNullValue ( @NotNull String name ) {

        @SuppressWarnings ( "unchecked" )
        V value = (V) getNonNullValue ( name );
        return value;
    }

    public @Nullable T getValueOrNull ( @NotNull String name ) {

        if ( containsName ( name ) ) {
            return map.get ( name ).getValue ();
        } else {
            return null;
        }
    }

    public @Nullable <V> V getCastedValueOrNull ( @NotNull String name ) {

        @SuppressWarnings ( "unchecked" )
        V value = (V) getValueOrNull ( name );
        return value;
    }

    public @NotNull T getNonNullValueOrDefault ( @NotNull String name, @NotNull T defaultValue ) {

        if ( containsName ( name ) ) {
            return getNonNullValue ( name );
        } else {
            return defaultValue;
        }
    }

    public @NotNull <V> V getCastedNonNullValueOrDefault ( @NotNull String name, @NotNull V defaultValue ) {

        if ( containsName ( name ) ) {
            return getCastedNonNullValue ( name );
        } else {
            return defaultValue;
        }
    }

    public @Nullable T getNullableValueOrDefault ( @NotNull String name, @Nullable T defaultValue ) {

        if ( containsName ( name ) ) {
            return getNullableValue ( name );
        } else {
            return defaultValue;
        }
    }

    public @Nullable <V> V getCastedNullableValueOrDefault ( @NotNull String name, @Nullable V defaultValue ) {

        if ( containsName ( name ) ) {
            return getCastedNullableValue ( name );
        } else {
            return defaultValue;
        }
    }


    // add

    public TypedParameters<T> add ( @NotNull Parameter<T> parameter ) {

        String name = parameter.getName();
        checkNotExists ( name );
        map.put ( name, parameter );

        return this;
    }

    public TypedParameters<T> add ( @NotNull String name, @Nullable T value ) {

        add ( new Parameter<T> ( name, value ) );

        return this;
    }

    public void addOrReplace ( @NotNull Parameter<T> parameter ) {

        map.put ( parameter.getName(), parameter );
    }

    public TypedParameters<T> addOrReplace ( @NotNull String name, @Nullable T value ) {

        map.put ( name, new Parameter<> ( name, value ) );

        return this;
    }


    // replace

    public TypedParameters<T> replace ( @NotNull Parameter<T> parameter ) {

        String name = parameter.getName();
        checkExists ( name );
        map.put ( name, parameter );

        return this;
    }

    public TypedParameters<T> replace ( @NotNull String name, T value ) {

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


    private void checkExists ( @NotNull String name ) {

        if ( ! containsName ( name ) ) throw new IllegalArgumentException (
            "Parameter '" + name + "' does not exist." );
    }

    private void checkNotExists ( @NotNull String name ) {

        if ( containsName ( name ) ) throw new IllegalArgumentException (
            "Parameter '" + name + "' exists already." );
    }
}
