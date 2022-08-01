package dev.pp.commands.picocli;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.commands.command.FormalCommand;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;

public class CommandToPicocliConverter {

    public static @NotNull CommandSpec convert ( @NotNull FormalCommand<?> command ) {

        CommandSpec commandSpec = CommandSpec.create()
            .name ( command.getName() );

        if ( command.getAlternativeNames() != null ) {
            commandSpec.aliases ( command.getAlternativeNames().toArray ( new String[0] ) );
        }

        @Nullable String title = command.getDocumentationTitle();
        if ( title != null ) {
            commandSpec.usageMessage ( new CommandLine.Model.UsageMessageSpec().description ( title ) );
        }

        FormalParameters inputParameters = command.getInputParameters();
        if ( inputParameters != null ) {
            addFormalParametersToCommandSpec ( inputParameters, commandSpec );
        }

        return commandSpec;
    }

    public static void addFormalParametersToCommandSpec (
        @NotNull FormalParameters formalParameters,
        @NotNull CommandSpec commandSpec ) {

        for ( FormalParameter<?> formalParameter : formalParameters.getAllSortedByIndex () ) {
            @Nullable Integer positionalIndex = formalParameter.getPositionalParameterIndex();
            if ( positionalIndex == null ) {
                commandSpec.addOption ( FormalParameterToPicocliConverter.convertToStringOption ( formalParameter ) );
            } else {
                commandSpec.addPositional ( FormalParameterToPicocliConverter.convertToStringPositional ( formalParameter ) );
            }
        }
    }
}
