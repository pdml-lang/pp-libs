package dev.pp.commands.command;

import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.formalParameter.FormalParameters;
import dev.pp.parameters.parameter.Parameters;
import dev.pp.parameters.parameter.ParametersCreator;

import java.util.Map;

public interface CommandExecutor<T> {

    default T execute (
        @Nullable Map<String, String> stringParameters,
        @Nullable FormalParameters formalParameters ) throws Exception {

        Parameters parameters = ParametersCreator.createFromStringMap ( stringParameters, null, formalParameters );
        return execute ( stringParameters, parameters );
    }

    T execute (
        @Nullable Map<String, String> stringParameters,
        @Nullable Parameters parameters ) throws Exception;
}
