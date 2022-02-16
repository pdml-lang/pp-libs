package dev.pp.scripting.env;

import dev.pp.scripting.ScriptingConstants;
import dev.pp.scripting.ScriptingUtils;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.scripting.bindings.builder.BindingsBuilder;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.Map;

public class ScriptingEnvironmentImpl implements ScriptingEnvironment {

    private final @NotNull String languageID;
    // private @Nullable Map<String, Object> bindings;
    // boolean allowAllAccess;
    int scriptFunctionCount;

    private final Context sharedContext;


    public ScriptingEnvironmentImpl (
        @NotNull String languageID,
        @Nullable Map<String, Object> bindings,
        boolean allowAllAccess ) {

        this.languageID = languageID;
        // this.bindings = bindings;
        // this.allowAllAccess = allowAllAccess;
        this.sharedContext = ScriptingUtils.createContext ( allowAllAccess, bindings, languageID );
        this.scriptFunctionCount = 1;
    }

    public ScriptingEnvironmentImpl ( boolean allowAllAccess ) {
        this ( ScriptingConstants.JAVASCRIPT_LANGUAGE_ID, BindingsBuilder.createWithCoreBindings(), allowAllAccess );
    }
/*
    public ScriptingEnvironmentImpl() {

        this ( ScriptingConstants.JAVASCRIPT_LANGUAGE_ID, null, false );
    }

    public ScriptingEnvironmentImpl ( @Nullable Map<String, Object> bindings ) {

        this ( ScriptingConstants.JAVASCRIPT_LANGUAGE_ID, bindings, true );
    }
*/


    public void addBinding ( @NotNull String identifier, @NotNull Object object ) {
        // TODO error if exists already
        sharedContext.getBindings ( languageID ).putMember ( identifier, object );
    }

    public void addDefinitions ( @NotNull String definitions ) {

        evaluate ( definitions );
    }

    public void executeScript ( @NotNull String script ) {

        // embed script in a function to create a local scope for constants and variables
        StringBuilder sb = new StringBuilder();
        sb.append ( "(function " )
            .append ( "script__" )
            .append ( scriptFunctionCount )
            .append ( "() {" )
            .append ( StringConstants.OS_NEW_LINE )
            .append ( script )
            .append ( StringConstants.OS_NEW_LINE )
            .append ( "})();" );

        scriptFunctionCount = scriptFunctionCount + 1;

        evaluate ( sb.toString() );
    }

    public @Nullable String evaluateExpressionToString ( @NotNull String expression ) {

        @NotNull Value value = evaluate ( expression );
        if ( value.isNull() ) {
            return null;
        } else {
            return value.toString();
        }
    }

    public void close() { sharedContext.close(); }

    private Value evaluate ( String code ) {
        return sharedContext.eval ( languageID, code );
    }
}
