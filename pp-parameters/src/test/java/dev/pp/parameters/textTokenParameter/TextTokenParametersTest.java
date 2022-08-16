package dev.pp.parameters.textTokenParameter;

import dev.pp.datatype.CommonDataTypes;
import dev.pp.parameters.cli.token.NameOrValueToken;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.FormalParameters;
import dev.pp.parameters.cli.CLIArguments;
import dev.pp.text.error.TextErrorException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextTokenParametersTest {

    @Test
    void createFromNameOrValueTokens () throws TextErrorException {

        List<NameOrValueToken> nameOrValueTokens = CLIArguments.parseToNameOrValueTokens (
            new String[]{"-n1", "v1", "--n2", "v 2", "---n3", "", "-n4=-v4", "v5", "v 6", "", "-n8"}, 0 );
        assert nameOrValueTokens != null;
        FormalParameters formalParameters = new FormalParameters()
            .add ( FormalParameter.builder ( "n1", CommonDataTypes.STRING ).build() )
            .add ( FormalParameter.builder ( "n2", CommonDataTypes.STRING ).build() )
            .add ( FormalParameter.builder ( "-n3", CommonDataTypes.STRING ).build() )
            .add ( FormalParameter.builder ( "n4", CommonDataTypes.STRING ).build() )
            .add ( FormalParameter.builder ( "n5", CommonDataTypes.STRING ).positionalParameterIndex ( 0 ).build() )
            .add ( FormalParameter.builder ( "n6", CommonDataTypes.STRING ).positionalParameterIndex ( 1 ).build() )
            .add ( FormalParameter.builder ( "n7", CommonDataTypes.STRING ).positionalParameterIndex ( 2 ).build() )
            .add ( FormalParameter.builder ( "n8", CommonDataTypes.STRING ).build() );
        TextTokenParameters textTokenParameters = TextTokenParameters.createFromNameOrValueTokens (
            nameOrValueTokens, null, formalParameters );
        assertEquals ( "{n1=v1, n2=v 2, -n3=null, n4=-v4, n5=v5, n6=v 6, n7=null, n8=true}", textTokenParameters.toString() );

        // errors

        // invalid name
        final List<NameOrValueToken> nameOrValueTokens2 = CLIArguments.parseToNameOrValueTokens (
            new String[]{"-invalid_name", "v1"}, 0 );
        assert nameOrValueTokens2 != null;
        assertThrows ( TextErrorException.class, () -> TextTokenParameters.createFromNameOrValueTokens (
            nameOrValueTokens2, null, formalParameters ) );

        // same parameter defined twice
        final List<NameOrValueToken> nameOrValueTokens3 = CLIArguments.parseToNameOrValueTokens (
            new String[]{"-n1", "v1", "-n1", "v2"}, 0 );
        assert nameOrValueTokens3 != null;
        assertThrows ( TextErrorException.class, () -> TextTokenParameters.createFromNameOrValueTokens (
            nameOrValueTokens3, null, formalParameters ) );
    }
}
