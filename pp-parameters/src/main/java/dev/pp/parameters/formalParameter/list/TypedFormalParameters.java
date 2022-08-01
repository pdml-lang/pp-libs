package dev.pp.parameters.formalParameter.list;

import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.parameter.Parameter;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.util.*;

@Deprecated
public class TypedFormalParameters<T> {

    // TODO see class Parameters<T>

    private final @NotNull List<FormalParameter<T>> list;
    private final @NotNull Map<String, FormalParameter<T>> allNamesMap;

    private @Nullable DataValidator<List<Parameter<T>>> validator;

    // private @Nullable FormalParameter<T> defaultParameter;


    public TypedFormalParameters ( @Nullable DataValidator<List<Parameter<T>>> validator ) {

        this.validator = validator;

        this.list = new ArrayList<>();
        this.allNamesMap = new HashMap<> ();
        // this.defaultParameter = null;
    }

    public TypedFormalParameters () {
        this ( null );
    }

    public TypedFormalParameters<T> add ( @NotNull FormalParameter<T> formalParameter ) {

        for ( String name : formalParameter.getAllNames() ) {
            checkNotExists ( name );
            allNamesMap.put ( name, formalParameter );
        }

        list.add ( formalParameter );

        return this;
    }

    /*
    public TypedFormalParameters<T> addDefaultParameter ( @NotNull FormalParameter<T> formalParameter ) {

        add ( formalParameter );
        setDefaultParameter ( formalParameter );

        return this;
    }

    public @Nullable FormalParameter<T> getDefaultParameter () { return defaultParameter; }

    private void setDefaultParameter ( @NotNull FormalParameter<T> defaultParameter ) {

        if ( this.defaultParameter != null ) throw new IllegalArgumentException (
            "Default parameter is set already (" + this.defaultParameter + ")" );

        if ( ! containsName ( defaultParameter.getName() ) ) throw new IllegalArgumentException (
            "Parameter '" + defaultParameter.getName() + "' doesn't exist." );

        this.defaultParameter = defaultParameter;
    }
    */

    public @NotNull List<FormalParameter<T>> getList() { return list; }

    public @NotNull List<FormalParameter<T>> getListSortedByOrder() {

        List<FormalParameter<T>> sortedList = new ArrayList<> ( list );
        sortedList.sort ( Comparator.comparingInt ( FormalParameter::getSortIndex ) );
        return sortedList;
    }

    @NotNull List<FormalParameter<T>> getListSortedByName() {

        List<FormalParameter<T>> sortedList = new ArrayList<> ( list );
        sortedList.sort ( Comparator.comparing ( FormalParameter::getName ) );
        return sortedList;
    }

    // @NotNull List<FormalParameter<T>> getListSortedByName_firstIsDefault();

    @Nullable public DataValidator<List<Parameter<T>>> getValidator () { return validator; }

    public void setValidator ( @Nullable DataValidator<List<Parameter<T>>> validator ) {
        this.validator = validator;
    }


    public boolean containsName ( @NotNull String name ) { return allNamesMap.containsKey ( name ); }

    public boolean containsParameter ( @NotNull FormalParameter<T> formalParameter ) {
        return containsName ( formalParameter.getName() ); }

    public @Nullable Set<String> sortedParameterNames() {

        Set<String> set = new LinkedHashSet<>();
        for ( FormalParameter<T> formalParameter : getListSortedByName() ) {
            set.add ( formalParameter.getName() );
        }

        return set.isEmpty() ? null : set;
    }

    public @Nullable String sortedParameterNamesAsString() {

        return namesSetToString ( sortedParameterNames() );
    }

    private @Nullable String namesSetToString ( @Nullable Set<String> names ) {

        if ( names == null || names.isEmpty() ) {
            return null;
        } else {
            return String.join ( ", ", names );
        }
    }

    public boolean hasRequiredParameters() {

        for ( FormalParameter<T> formalParameter : list ) {
            if ( formalParameter.isRequired() ) return true;
        }

        return false;
    }

    public @Nullable Set<String> sortedRequiredParameterNames () {

        Set<String> set = new LinkedHashSet<>();
        for ( FormalParameter<T> formalParameter : getListSortedByName() ) {
            if ( formalParameter.isRequired() ) set.add ( formalParameter.getName() );
        }

        return set.isEmpty() ? null : set;
    }

    public @Nullable String sortedRequiredParameterNamesAsString () {

        return namesSetToString ( sortedRequiredParameterNames () );
    }

    @NotNull FormalParameter<T> get ( @NotNull String name ) {

        if ( containsName ( name ) ) {
            return allNamesMap.get ( name );
        } else {
            throw new RuntimeException ( "Formal parameter '" + name + "' does not exist." );
        }
    }

    @Nullable FormalParameter<T> getIfContained ( @NotNull String name ) {

        if ( containsName ( name ) ) {
            return allNamesMap.get ( name );
        } else {
            return null;
        }
    }

    public void validate ( List<Parameter<T>> parameters, TextToken token ) throws DataValidatorException {

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
