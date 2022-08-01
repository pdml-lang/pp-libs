package dev.pp.commands.command;

import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.parameters.parameter.list.Parameters;
import dev.pp.parameters.parameter.list.ParametersCreator;

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
