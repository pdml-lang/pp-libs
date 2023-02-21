package dev.pp.commands.command;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.*;

public class CommandSpecs<I,O> {


    private final @NotNull Map<String, CommandSpec<I,O>> allNamesMap;
    private final @NotNull List<CommandSpec<I,O>> list;


    public CommandSpecs () {

        this.list = new ArrayList<> ();
        this.allNamesMap = new HashMap<>();
    }


    public CommandSpecs add ( @NotNull CommandSpec<I,O> commandSpec ) {

        // name must be unique
        for ( String name : commandSpec.allNames () ) {
            checkNotExists ( name );
            allNamesMap.put ( name, commandSpec );
        }

        list.add ( commandSpec );

        return this;
    }


    // Getters

    public @NotNull CommandSpec<I,O> get ( @NotNull String name ) {

        CommandSpec<I,O> command = getOrNull ( name );
        if ( command != null ) {
            return command;
        } else {
            throw new IllegalArgumentException ( "Command '" + name + "' does not exist." );
        }
    }

    public @Nullable CommandSpec<I,O> getOrNull ( @NotNull String name ) {

        if ( containsName ( name ) ) {
            return allNamesMap.get ( name );
        } else {
            return null;
        }
    }

    public @NotNull List<CommandSpec<I,O>> list() { return new ArrayList<> ( list ); }

    public @NotNull List<CommandSpec<I,O>> listSortedByName() {

        return list
            .stream()
            .sorted ( Comparator.comparing ( CommandSpec::getName ) )
            .toList();
    }

    public @NotNull List<CommandSpec<I,O>> listSortedByIndex() {

        return list
            .stream()
            .sorted ( Comparator.comparingInt ( CommandSpec::sortIndexOrZero ) )
            .toList();
    }


    // Names

    public boolean containsName ( @NotNull String name ) { return allNamesMap.containsKey ( name ); }

    public @Nullable Set<String> sortedNames() {

        Set<String> set = new LinkedHashSet<>();
        for ( CommandSpec<I,O> commandSpec : listSortedByName () ) {
            set.add ( commandSpec.getName() );
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
