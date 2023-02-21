package dev.pp.commands.cli;

import dev.pp.basics.annotations.NotNull;
import dev.pp.commands.command.CommandSpec;
import dev.pp.commands.command.CommandSpecs;
import dev.pp.commands.errors.CLIExceptionHandler;
import dev.pp.parameters.cli.CLIArguments;
import dev.pp.parameters.parameters.Parameters;

public class CLICommands {

    public static <I,O> void runAndExit (
        @NotNull String[] arguments,
        @NotNull CommandSpecs<I,O> commandSpecs ) {

        System.exit ( runAndReturnExitStatus ( arguments, commandSpecs ) );
    }

    public static <I,O> int runAndReturnExitStatus (
        @NotNull String[] arguments,
        @NotNull CommandSpecs<I,O> commandSpecs ) {

        try {
            run ( arguments, commandSpecs );
            return 0;
        } catch ( Throwable e ) {
            return CLIExceptionHandler.handleThrowable ( e );
        }
    }

    public static <I,O> O run (
        @NotNull String[] arguments,
        @NotNull CommandSpecs<I,O> commandSpecs ) throws Exception {

        @NotNull CommandSpec<I,O> commandSpec = getCommandSpec ( arguments, commandSpecs );
        Parameters<I> parameters = CLIArguments.parseToParameters (
            arguments, 1, null, commandSpec.getInputParameters() );
        return commandSpec.execute ( parameters );
    }

    private static <I,O> CommandSpec<I,O> getCommandSpec (
        @NotNull String[] CLIArguments,
        @NotNull CommandSpecs<I,O> commandSpecs ) throws InvalidCLIArguments {

        if ( CLIArguments.length < 1 ) {
            throw new InvalidCLIArguments ( "A command name must be specified. Valid command names are: " +
                commandSpecs.sortedNamesAsString(),
                CLIArguments );
        }

        String commandName = CLIArguments[0];
        if ( commandName == null || commandName.isEmpty() ) {
            throw new InvalidCLIArguments ( "The command name cannot be empty. Valid command names are: " +
                commandSpecs.sortedNamesAsString(),
                CLIArguments );
        }

        if ( ! commandSpecs.containsName ( commandName ) ) {
            throw new InvalidCLIArguments ( "Command '" + commandName + "' does not exist. Valid command names are: " +
                commandSpecs.sortedNamesAsString(),
                CLIArguments );
        }

        return commandSpecs.get ( commandName );
    }
}
