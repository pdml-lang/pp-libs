package dev.pp.commands.picocli;

import dev.pp.basics.annotations.NotNull;
import dev.pp.parameters.formalParameter.FormalParameter;
import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.Model.PositionalParamSpec;

import java.util.*;

public class FormalParameterToPicocliConverter {

    /*
    public static @NotNull Object convertToStringSpec ( @NotNull FormalParameter<?> formalParameter ) {

        @Nullable Integer positionalIndex = formalParameter.getPositionalParameterIndex();
        if ( positionalIndex == null ) {
            return convertToStringOptionSpec ( formalParameter );
        } else {
            return convertToPositionalStringParamSpec ( formalParameter );
        }
    }
    */

    public static @NotNull OptionSpec convertToStringOption ( @NotNull FormalParameter<?> formalParameter ) {

        List<String> names = new ArrayList<>();
        names.add ( "--" + formalParameter.getName() );
        Set<String> alternativeNames = formalParameter.getAlternativeNames();
        if ( alternativeNames != null ) {
            for ( String alternativeName : alternativeNames ) {
                names.add ( "-" + alternativeName );
            }
        }

        return OptionSpec.builder ( names.toArray ( new String[0] ) )
            .type ( String.class )
            .description ( formalParameter.getDocumentationDescription() )
            .required ( formalParameter.isRequired() )
            .build();
    }

    public static @NotNull PositionalParamSpec convertToStringPositional (
        @NotNull FormalParameter<?> formalParameter ) {

        Integer index = formalParameter.getPositionalParameterIndex();
        assert index != null;

        return PositionalParamSpec.builder()
            .type ( String.class )
            .description ( formalParameter.getDocumentationDescription () )
            .required ( formalParameter.isRequired () )
            .paramLabel ( formalParameter.getName ().toUpperCase () )
            .index ( String.valueOf ( index - 1 ) )
            .arity ( "1" )
            .build ();
    }
}
