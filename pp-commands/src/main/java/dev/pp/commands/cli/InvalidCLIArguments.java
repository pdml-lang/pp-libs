package dev.pp.commands.cli;

import dev.pp.basics.annotations.NotNull;

public class InvalidCLIArguments extends Exception {

    private final @NotNull String[] CLIArguments;
    public @NotNull String[] getCLIArguments() { return CLIArguments; }

    public InvalidCLIArguments ( @NotNull String message, @NotNull String[] CLIArguments ) {

        super ( message );
        this.CLIArguments = CLIArguments;
    }
}
