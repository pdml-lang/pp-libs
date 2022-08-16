package dev.pp.parameters.cli;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.cli.token.NameOrValueToken;
import dev.pp.parameters.cli.token.NameToken;
import dev.pp.parameters.cli.token.ValueToken;
import dev.pp.parameters.formalParameter.FormalParameters;
import dev.pp.parameters.parameter.Parameters;
import dev.pp.parameters.parameter.ParametersCreator;
import dev.pp.parameters.textTokenParameter.TextTokenParameters;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

import java.util.ArrayList;
import java.util.List;

public class CLIArguments {

    public static @Nullable Parameters parseToParameters (
        @NotNull String[] CLIStrings,
        int CLIStringsStartIndex,
        @Nullable TextToken startToken,
        @NotNull FormalParameters formalParameters ) throws TextErrorException {

        @NotNull TextTokenParameters textTokenParameters = parseToTextTokenParameters (
            CLIStrings, CLIStringsStartIndex, startToken, formalParameters );

        return ParametersCreator.createFromTextParameters ( textTokenParameters, startToken, formalParameters );
    }

    public static @NotNull TextTokenParameters parseToTextTokenParameters (
        @NotNull String[] CLIStrings,
        int CLIStringsStartIndex,
        @Nullable TextToken startToken,
        @NotNull FormalParameters formalParameters ) throws TextErrorException {

        @Nullable List<NameOrValueToken> nameOrValueTokens = parseToNameOrValueTokens ( CLIStrings, CLIStringsStartIndex );
        return
            nameOrValueTokens == null
            ? new TextTokenParameters ( startToken )
            : TextTokenParameters.createFromNameOrValueTokens ( nameOrValueTokens, startToken, formalParameters );
    }

    public static @Nullable List<NameOrValueToken> parseToNameOrValueTokens (
        @NotNull String[] CLIArguments,
        int startIndex ) throws TextErrorException {

        if ( startIndex < 0 || startIndex >= CLIArguments.length ) throw new IllegalArgumentException (
            "startIndex (" + startIndex + ") out of range 0..." + CLIArguments.length + "." );

        List<NameOrValueToken> result = new ArrayList<>();

        boolean previousWasName = false;
        int currentValuePosition = 0;
        boolean parsingOnlyValues = false;

        for ( int i = startIndex; i < CLIArguments.length; i++ ) {
            @Nullable String CLIString = CLIArguments[ i ];
            if ( CLIString == null ) CLIString = "";

            if ( CLIString.equals ( "--" ) ) {
                parsingOnlyValues = true;
                previousWasName = false;
                continue;
            }

            if ( ! parsingOnlyValues && CLIString.startsWith ( "-" ) ) {
                int CLIStringStartIndex = CLIString.startsWith ( "--" ) ? 2 : 1;
                // if ( CLIStringStartIndex >= CLIString.length() ) throw new CLIArgumentsException (
                //    "'" + CLIString + "' is invalid. Name missing after '" + CLIString + "'." );
                assert CLIStringStartIndex < CLIString.length();
                String unprefixedCLIString = CLIString.substring ( CLIStringStartIndex );
                int assignmentIndex = unprefixedCLIString.indexOf ( '=' );
                if ( assignmentIndex == -1 ) {
                    result.add ( new NameToken ( unprefixedCLIString, null ) );
                    previousWasName = true;
                } else{
                    if ( assignmentIndex == 0 ) {
                        // --=value
                        throw new TextErrorException (
                            "INVALID_CLI_ARGUMENTS",
                            "'" + CLIString + "' is invalid. Argument name missing before '='.",
                            new TextToken ( CLIString ) );
                    } else if ( assignmentIndex == unprefixedCLIString.length() - 1 ) {
                        // --name=
                        String name = unprefixedCLIString.substring ( 0, assignmentIndex );
                        result.add ( new NameToken ( name, null ) );
                        result.add ( new ValueToken ( "", null, null ) );
                    } else {
                        // --name=value
                        String name = unprefixedCLIString.substring ( 0, assignmentIndex );
                        String value = unprefixedCLIString.substring ( assignmentIndex + 1 );
                        result.add ( new NameToken ( name, null ) );
                        result.add ( new ValueToken ( value, null, null ) );
                    }
                    previousWasName = false;
                }

            } else {
                result.add ( new ValueToken ( CLIString, null, previousWasName ? null : currentValuePosition ) );
                if ( ! previousWasName ) currentValuePosition++;
                previousWasName = false;
            }
        }

        return result.isEmpty() ? null : result;
    }
}
