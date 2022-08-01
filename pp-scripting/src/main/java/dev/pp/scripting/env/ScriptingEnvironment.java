package dev.pp.scripting.env;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.Map;

public interface ScriptingEnvironment {

    void addBinding ( @NotNull String identifier, @NotNull Object object );

    void addDefinitions ( @NotNull String definitions );

    void executeScript ( @NotNull String script );

    @Nullable String evaluateExpressionToString ( @NotNull String expression );

    void close();
}
