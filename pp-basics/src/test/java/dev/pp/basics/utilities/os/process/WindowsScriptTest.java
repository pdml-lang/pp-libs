package dev.pp.basics.utilities.os.process;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WindowsScriptTest {

    @Test
    public void testTextPipeWithCmdScript() throws IOException, InterruptedException {

        String code = """
            @echo off
            echo %1 %2
            echo line 1
            echo line 2
            
            rem read first line of stdin into variable 'input'
            set /p input=
            echo input %input%
            """;
        /* New lines are all \n (and not \r\n\) on Windows!!!
        String expected = """
            a1 a2
            line 1
            line 2
            input il1
            """;
        */
        String expected = "a1 a2\r\nline 1\r\nline 2\r\ninput il1\r\n";

        String output = WindowsScript.textPipeWithCmdScript ( code, new String[]{"a1", "a2"}, "il1\r\nil2\r\n" );
        assertNotNull ( output );
        assertEquals ( expected.length(), output.length() );
        assertEquals ( expected, output );
    }

    @Test
    public void testRunScriptFileInNewWindowAndWait() throws IOException, InterruptedException {

        /*
        Path file = Path.of ( "C:\\aa\\work\\PML\\docs\\User_Manual\\bin\\create_HTML_with_dev_version.bat" );
        Path dir = file.getParent();
        int result = OSScript.runWindowsScriptFileInNewWindowAndWait ( file, dir, null, "Test" );
        assertEquals ( 0, result );

         */
    }
}
