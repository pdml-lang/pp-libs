package dev.pp.commands.command;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class FormalCommands {


    private final @NotNull Map<String, FormalCommand<?>> allNamesMap;
    private final @NotNull List<FormalCommand<?>> list;


    public FormalCommands() {

        this.list = new ArrayList<> ();
        this.allNamesMap = new HashMap<>();
    }


    public FormalCommands add ( @NotNull FormalCommand<?> formalCommand ) {

        // name must be unique
        for ( String name : formalCommand.getAllNames() ) {
            checkNotExists ( name );
            allNamesMap.put ( name, formalCommand );
        }

        list.add ( formalCommand );

        return this;
    }


    // Getters

    public @NotNull <T> FormalCommand<T> get ( @NotNull String name ) {

        FormalCommand<T> command = getOrNull ( name );
        if ( command != null ) {
            return command;
        } else {
            throw new RuntimeException ( "Formal command '" + name + "' does not exist." );
        }
    }

    public @Nullable <T> FormalCommand<T> getOrNull ( @NotNull String name ) {

        if ( containsName ( name ) ) {
            @SuppressWarnings ( "unchecked" )
            FormalCommand<T> command = (FormalCommand<T>) allNamesMap.get ( name );
            return command;
        } else {
            return null;
        }
    }

    public @NotNull List<FormalCommand<?>> getAll() { return new ArrayList<> ( list ); }

    public @NotNull List<FormalCommand<?>> getAllSortedByName() {

        return list
            .stream()
            .sorted ( Comparator.comparing ( FormalCommand::getName ) )
            .collect ( Collectors.toList() );
    }

    public @NotNull List<FormalCommand<?>> getAllSortedByIndex() {

        return list
            .stream()
            .sorted ( Comparator.comparingInt ( FormalCommand::getSortIndexOrZero ) )
            .collect ( Collectors.toList() );
    }


    // Names

    public boolean containsName ( @NotNull String name ) { return allNamesMap.containsKey ( name ); }

    public @Nullable Set<String> sortedNames() {

        Set<String> set = new LinkedHashSet<>();
        for ( FormalCommand<?> formalCommand : getAllSortedByName() ) {
            set.add ( formalCommand.getName() );
        }

        return set.isEmpty() ? null : set;
    }

    public @Nullable String sortedNamesAsString() {
        return sortedNamesAsString ( ", " );
    }

    public @Nullable String sortedNamesAsString ( @NotNull String separator ) {

        return namesSetToString ( sortedNames(), separator );
    }

    private @Nullable String namesSetToString ( @Nullable Set<String> names, @NotNull String separator ) {

        if ( names == null || names.isEmpty() ) {
            return null;
        } else {
            return String.join ( separator, names );
        }
    }



    private void checkExists ( @NotNull String name ) {

        if ( ! containsName ( name ) )
            throw new IllegalArgumentException ( "Command '" + name + "' does not exist." );
    }

    private void checkNotExists ( @NotNull String name ) {

        if ( containsName ( name ) )
            throw new IllegalArgumentException ( "Command '" + name + "' exists already." );
    }
}
