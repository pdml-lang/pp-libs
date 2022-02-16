package dev.pp.scripting;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.utilities.file.TempFileUtils;
import dev.pp.basics.utilities.file.TextFileUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScriptingUtilsTest {

/*
    @Test
    public void testExecuteScriptWithContext() throws IOException {

        String script = "context.write ( 'Hello from script context 2' );";

        Writer writer = new FileWriter ( "C:\\temp\\scriptContextOutput.txt" );
        ScriptingContext context = new WriterScriptingContext ( writer );

        // ScriptUtils.executeScriptWithWriter ( script, new PrintWriter ( System.out ), "js" );
        ScriptingUtils.executeJavascriptWithContext ( script, context );
        writer.close();
    }
*/

    @Test
    public void test() throws IOException {

        String def = """
            const two = "2";
            
            function four() {
                return "4";
            }

            """;

        String exp = "two + four()";

        String result = ScriptingUtils.evaluateJavascriptExpressionToString ( def + exp, null, true );
        assertEquals ( "24", result );

        String languageID = ScriptingConstants.JAVASCRIPT_LANGUAGE_ID;
        Context context = ScriptingUtils.createContext ( true, null, languageID );
        context.eval ( languageID, def );
        @NotNull Value value = context.eval ( languageID, exp );
        assertEquals ( "24", value.toString() );

        String script_1 = """
            const c1 = "c1";
            c1
            """;
        value = context.eval ( languageID, script_1 );
        assertEquals ( "c1", value.toString() );

        String script_2 = """
            (function f2() {
                const c1 = "c12";
                return c1;
            })();
            // f2();
            """;
        value = context.eval ( languageID, script_2 );
        assertEquals ( "c12", value.toString() );

        String script_3 = """
            const c1 = "c13";
            c1
            """;
        // error because 'c1' is defined already in 'script_1'
        assertThrows ( PolyglotException.class, () -> context.eval ( languageID, script_3 ) );
    }

    @Test
    public void testEvaluateJavascriptExpressionToString() throws IOException {

        String expression = "1 + 1";
        String result = ScriptingUtils.evaluateJavascriptExpressionToString ( expression, null, true );
        assertEquals ( "2", result );
    }

    @Test
    public void testExecuteJavascriptScript() throws IOException {

        String script = "writer.write ( 'Hello from script' );";
        File file = TempFileUtils.createEmptyTempFile ( true );
        Writer writer = new FileWriter ( file );

        // ScriptUtils.executeScriptWithWriter ( script, new PrintWriter ( System.out ), "js" );
        // ScriptingUtils.executeJavascriptWithWriter ( script, writer );
        ScriptingUtils.executeJavascriptScript ( script, Map.of ( "writer", writer ), true );
        writer.close();
        assertEquals ( "Hello from script", TextFileUtils.readTextFromUTF8File ( file ) );
    }
}