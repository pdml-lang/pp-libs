package dev.pp.commands.picocli;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.commands.command.FormalCommand;
import dev.pp.commands.command.FormalCommands;
import dev.pp.commands.errors.CLIExceptionHandler;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import picocli.CommandLine;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.Model.CommandSpec;

import java.util.Map;

public class PicocliCommandLineExecutor {

    public static int executeCommand (
        @NotNull String[] CLIArgs,
        @NotNull String versionInfo,
        @NotNull FormalCommands formalCommands ) {

        try {
            CommandSpec mainCommand = CommandSpec.create()
                .mixinStandardHelpOptions ( true )
                .version ( versionInfo );

            CommandLine commandLine = new CommandLine ( mainCommand )
                .setSubcommandsCaseInsensitive ( true )
                .setOptionsCaseInsensitive ( true )
                .setCaseInsensitiveEnumValuesAllowed ( true );

            for ( FormalCommand<?> formalCommand : formalCommands.getAll() ) {
                commandLine.addSubcommand ( CommandToPicocliConverter.convert ( formalCommand ) );
            }

            CommandLine.ParseResult parseResult = commandLine.parseArgs ( CLIArgs );

            if ( ! parseResult.hasSubcommand() ) {
                executeMainCommand ( parseResult );
            } else {
                executeSubCommand ( parseResult, formalCommands );
            }

            return 0;

        } catch ( Throwable e ) {
            return CLIExceptionHandler.handleThrowable ( e );
        }
    }

    private static void executeMainCommand ( ParseResult parseResult ) throws Exception {

        if ( ! CommandLine.printHelpIfRequested ( parseResult ) ) {
            throw new Exception ( "Command line arguments are required. Use option '-h' to get help." );
        }
    }

    private static void executeSubCommand ( ParseResult mainParseResult, @NotNull FormalCommands formalCommands ) throws Exception {

        ParseResult subParseResult = mainParseResult.subcommand();
        CommandSpec commandSpec = subParseResult.commandSpec();
        String commandName = commandSpec.name();

        FormalCommand<?> formalCommand = formalCommands.get ( commandName );
        FormalParameters formalParameters = formalCommand.getInputParameters();

        @Nullable Map<String, String> stringParams =
            formalParameters != null
            ? PicocliHelper.parseResultToStringMap ( subParseResult, formalParameters )
            : null;

        formalCommand.execute ( stringParams );
    }
}
