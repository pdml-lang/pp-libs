package dev.pp.scripting.env;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.scripting.ScriptingUtils;

import java.util.Map;

@Deprecated
public class ScriptingEnvironmentImplOld implements ScriptingEnvironmentOld {


    // Hack:
    // Sharing code between different contexts seems not to work in GraalVM version 21.3.0
    // This implementation includes all definitions in scripts and expressions -> much slower, probably
    private String definitions = "";


    public ScriptingEnvironmentImplOld () {}


    public void addDefinitions (
        @NotNull String languageID,
        @NotNull String code,
        @Nullable Map<String, Object> bindings,
        boolean allowAllAccess ) {

        ScriptingUtils.parse ( languageID, code, bindings, allowAllAccess );

        definitions = definitions + StringConstants.OS_NEW_LINE + code;
    }

    public void executeScript (
        @NotNull String languageID,
        @NotNull String script,
        @Nullable Map<String, Object> bindings,
        boolean allowAllAccess ) {

        ScriptingUtils.executeScript (
            languageID, definitions + StringConstants.OS_NEW_LINE + script, bindings, allowAllAccess );
    }

    public @Nullable String evaluateExpressionToString (
        @NotNull String languageID,
        @NotNull String expression,
        @Nullable Map<String, Object> bindings,
        boolean allowAllAccess ) {

        return ScriptingUtils.evaluateExpressionToString (
            languageID, definitions + StringConstants.OS_NEW_LINE + expression, bindings, allowAllAccess );
    }
}
