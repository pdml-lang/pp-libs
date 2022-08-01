package dev.pp.parameters.formalParameter.list;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.parameter.list.Parameters;
import dev.pp.text.token.TextToken;

import java.util.*;
import java.util.stream.Collectors;

public class FormalParameters {

    private final @NotNull List<FormalParameter<?>> list;
    private final @NotNull Map<String, FormalParameter<?>> allNamesMap;

    private @Nullable DataValidator<Parameters> validator;

    // private @Nullable FormalParameter<?> defaultParameter;


    public FormalParameters ( @Nullable DataValidator<Parameters> validator ) {

        this.validator = validator;

        this.list = new ArrayList<>();
        this.allNamesMap = new HashMap<>();
        // this.defaultParameter = null;
    }

    public FormalParameters () {
        this ( null );
    }


    /*
    public MixedFormalParameters addDefaultParameter ( @NotNull FormalParameter<?> formalParameter ) {

        add ( formalParameter );
        setDefaultParameter ( formalParameter );

        return this;
    }
    */

    public FormalParameters add ( @NotNull FormalParameter<?> formalParameter ) {

        // name must be unique
        for ( String name : formalParameter.getAllNames() ) {
            checkNotExists ( name );
            allNamesMap.put ( name, formalParameter );
        }

        // positional index must be unique
        @Nullable Integer positionalIndex = formalParameter.getPositionalParameterIndex();
        if ( positionalIndex != null) {
            for ( FormalParameter<?> existingParameter : list ) {
                @Nullable Integer existingPositionalIndex = existingParameter.getPositionalParameterIndex();
                if ( existingPositionalIndex != null &&
                    existingPositionalIndex.intValue() == positionalIndex.intValue() ) {
                    throw new IllegalArgumentException (
                        "Positional index '" + positionalIndex + "' exists already for parameter '" + existingParameter + "'." );
                }
            }
        }

        list.add ( formalParameter );

        return this;
    }

    public @Nullable FormalParameter<?> getFirstPositionalParameterOrNull() {

        List<FormalParameter<?>> list = getPositionalParametersSortedByPositionalIndex ();
        if ( list != null ) {
            return list.get ( 0 );
        } else {
            return null;
        }
    }

    public @Nullable String getFirstPositionalParameterNameOrNull() {

        FormalParameter<?> parameter = getFirstPositionalParameterOrNull();
        return parameter == null ? null : parameter.getName();
    }

    /*
    public @Nullable FormalParameter<?> getDefaultParameter () { return defaultParameter; }

    public void setDefaultParameter ( @NotNull FormalParameter<?> defaultParameter ) {

        if ( this.defaultParameter != null ) throw new IllegalArgumentException (
            "Default parameter is set already (" + this.defaultParameter + ")" );

        if ( ! containsName ( defaultParameter.getName() ) ) throw new IllegalArgumentException (
            "Parameter '" + defaultParameter.getName() + "' doesn't exist." );

        this.defaultParameter = defaultParameter;
    }
    */

    public @NotNull List<FormalParameter<?>> getAll() { return new ArrayList<> ( list ); }

    public @NotNull List<FormalParameter<?>> getAllSortedByName() {

        return list
            .stream()
            .sorted ( Comparator.comparing ( FormalParameter::getName ) )
            .collect ( Collectors.toList() );
    }

    public @NotNull List<FormalParameter<?>> getAllSortedByIndex () {

        return list
            .stream()
            .sorted ( Comparator.comparingInt ( FormalParameter::getSortIndexOrZero ) )
            .collect ( Collectors.toList() );
    }

    public @NotNull List<FormalParameter<?>> getAllSortedByName_RequiredFirst() {

        return list
            .stream()
            .sorted ( Comparator.comparing ( p -> (p.isRequired() ? "0" : "1") + p.getName() ) )
            .collect ( Collectors.toList() );
    }

    public @Nullable List<FormalParameter<?>> getPositionalParametersSortedByPositionalIndex () {

        List<FormalParameter<?>> result = list
            .stream()
            .filter ( FormalParameter::isPositionalParameter )
            .sorted ( Comparator.comparingInt ( FormalParameter::getPositionalIndexOrZero ) )
            .collect ( Collectors.toList() );

        return result.isEmpty() ? null : result;
    }

    public @NotNull List<FormalParameter<?>> getAllSortedByPositionalIndexThenName() {

        List<FormalParameter<?>> result = new ArrayList<>();

        List<FormalParameter<?>> positionals = getPositionalParametersSortedByPositionalIndex();
        if ( positionals != null ) result.addAll ( positionals );

        List<FormalParameter<?>> named = getNonPositionalParametersSortedByName();
        if ( named != null ) result.addAll ( named );

        return result;
/*
        return list
            .stream()
            .sorted ( Comparator.comparing (
                // p -> p.isPositionalParameter() ? "0" + p.getPositionalIndexOrZero() : "1" + p.getName () ) )
                p -> p.isPositionalParameter() ? String.valueOf ( p.getPositionalIndexOrZero() ) : ( "9999" + p.getName () ) ) )
            .collect ( Collectors.toList() );

 */
    }

    public @Nullable List<FormalParameter<?>> getNonPositionalParametersSortedByName() {

        List<FormalParameter<?>> result = list
            .stream()
            .filter ( p -> ! p.isPositionalParameter() )
            .sorted ( Comparator.comparing ( FormalParameter::getName ) )
            .collect ( Collectors.toList() );

        return result.isEmpty() ? null : result;
    }

    // @NotNull List<FormalParameter<T>> getListSortedByName_firstIsDefault();

    @Nullable public DataValidator<Parameters> getValidator () { return validator; }

    public void setValidator ( @Nullable DataValidator<Parameters> validator ) {
        this.validator = validator;
    }


    public boolean containsName ( @NotNull String name ) { return allNamesMap.containsKey ( name ); }

    /*
    public boolean containsParameter ( @NotNull FormalParameter<T> formalParameter ) {
        return containsName ( formalParameter.getName() ); }
    */

    public @Nullable Set<String> sortedParameterNames() {

        Set<String> set = new LinkedHashSet<>();
        for ( FormalParameter<?> formalParameter : getAllSortedByName () ) {
            set.add ( formalParameter.getName() );
        }

        return set.isEmpty() ? null : set;
    }

    public @Nullable String sortedParameterNamesAsString() {
        return sortedParameterNamesAsString ( ", " );
    }

    public @Nullable String sortedParameterNamesAsString ( @NotNull String separator ) {

        return namesSetToString ( sortedParameterNames(), separator );
    }

    private @Nullable String namesSetToString ( @Nullable Set<String> names, @NotNull String separator ) {

        if ( names == null || names.isEmpty() ) {
            return null;
        } else {
            return String.join ( separator, names );
        }
    }

    public boolean hasRequiredParameters() {

        for ( FormalParameter<?> formalParameter : list ) {
            if ( formalParameter.isRequired() ) return true;
        }

        return false;
    }

    public @Nullable Set<String> sortedRequiredParameterNames () {

        Set<String> set = new LinkedHashSet<>();
        for ( FormalParameter<?> formalParameter : getAllSortedByName () ) {
            if ( formalParameter.isRequired() ) set.add ( formalParameter.getName() );
        }

        return set.isEmpty() ? null : set;
    }

    public @Nullable String sortedRequiredParameterNamesAsString () {

        return namesSetToString ( sortedRequiredParameterNames(), ", " );
    }

    public @NotNull <T> FormalParameter<T> get ( @NotNull String name ) {

        FormalParameter<T> parameter = getOrNull ( name );
        if ( parameter != null ) {
            return parameter;
        } else {
            throw new RuntimeException ( "Formal parameter '" + name + "' does not exist." );
        }
    }

    public @Nullable <T> FormalParameter<T> getOrNull ( @NotNull String name ) {

        if ( containsName ( name ) ) {
            @SuppressWarnings ( "unchecked" )
            FormalParameter<T> parameter = (FormalParameter<T>) allNamesMap.get ( name );
            return parameter;
        } else {
            return null;
        }
    }

    public void validate ( Parameters parameters, TextToken token ) throws DataValidatorException {

        if ( validator == null ) return;

        validator.validate ( parameters, token );
    }


    private void checkExists ( @NotNull String name ) {

        if ( ! containsName ( name ) )
            throw new IllegalArgumentException ( "Parameter '" + name + "' does not exist." );
    }

    private void checkNotExists ( @NotNull String name ) {

        if ( containsName ( name ) )
            throw new IllegalArgumentException ( "Parameter '" + name + "' exists already." );
    }
}
