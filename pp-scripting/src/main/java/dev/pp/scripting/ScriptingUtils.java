package dev.pp.scripting;

import java.util.Map;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import static dev.pp.scripting.ScriptingConstants.JAVASCRIPT_LANGUAGE_ID;

public class ScriptingUtils {

    // Note: Currently (2021-09) only Javascript is well supported in GraalVM

    public static @Nullable String evaluateJavascriptExpressionToString (
        @NotNull String expression,
        @Nullable Map<String, Object> bindings,
        boolean allowAllAccess ) {

        return evaluateExpressionToString ( JAVASCRIPT_LANGUAGE_ID, expression, bindings, allowAllAccess );
    }

    public static @Nullable String evaluateExpressionToString (
        @NotNull String languageID,
        @NotNull String expression,
        @Nullable Map<String, Object> bindings,
        boolean allowAllAccess ) {

        try ( Context context = createContext ( allowAllAccess, bindings, languageID ) ) {

            // TODO? check exception
            @NotNull Value value = context.eval ( languageID, expression );
            if ( value.isNull() ) {
                return null;
            } else {
                return value.toString();
            }
        }
    }

    public static void executeJavascriptScript (
        @NotNull String script,
        @Nullable Map<String, Object> bindings,
        boolean allowAllAccess ) {

        executeScript ( JAVASCRIPT_LANGUAGE_ID, script, bindings, allowAllAccess );
    }

    public static void executeScript (
        @NotNull String languageID,
        @NotNull String script,
        @Nullable Map<String, Object> bindings,
        boolean allowAllAccess ) {

        try ( Context context = createContext ( allowAllAccess, bindings, languageID ) ) {

            // TODO? check exception
            context.eval ( languageID, script );
        }
    }

    public static void parse (
        @NotNull String languageID,
        @NotNull String code,
        @Nullable Map<String, Object> bindings,
        boolean allowAllAccess ) {

        try ( Context context = createContext ( allowAllAccess, bindings, languageID ) ) {

            // TODO? check exception
            context.parse ( languageID, code );
        }
    }

    public static Context createContext (
        boolean allowAllAccess,
        @Nullable Map<String, Object> bindings,
        @NotNull String languageId ) {

        Context context = createContext ( allowAllAccess );
        setBindings ( context, languageId, bindings );
        return context;
    }

    private static Context createContext ( boolean allowAllAccess ) {

        return Context.newBuilder()
            .option ( "engine.WarnInterpreterOnly", "false" ) // see Warning: Implementation does not support runtime compilation.
            // at https://www.graalvm.org/reference-manual/js/FAQ/#errors
            .allowAllAccess ( allowAllAccess )
            // TODO?
            // .in ( System.in )
            // .out ( System.out )
            // .err ( System.err )
            .build();
    }

    private static void setBindings (
        @NotNull Context context,
        @NotNull String languageId,
        @Nullable Map<String, Object> bindings ) {

        if ( bindings == null ) return;

        Value contextBindings = context.getBindings ( languageId );
        for ( var entry : bindings.entrySet () ) {
            contextBindings.putMember ( entry.getKey(), entry.getValue() );
        }
    }

/*
    public static void executeJavascriptWithBindings (
        @NotNull String script,
        @NotNull Map<String, Object> bindings,
        @NotNull TextErrorHandler errorHandler ) {

        try {
            executeJavascriptScript ( script, bindings, true );
        } catch ( PolyglotException | IllegalStateException | IllegalArgumentException e ) {
            errorHandler.handleAbortingError (
                "SCRIPTING_ERROR",
                "The following error occurred when a script was executed: " + e.getMessage(),
                null );
        }
    }
*/
}
