package dev.pp.basics.utilities.os.process;

import dev.pp.basics.utilities.file.TempFileUtils;
import dev.pp.basics.utilities.string.StringConstants;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OSProcessTest {

    @Test
    public void testStartOSCommandAndContinue() throws IOException {

        File file = TempFileUtils.createNonEmptyTempTextFile (
        "line 1" + StringConstants.OS_NEW_LINE + "line 2", false );

        // open file in VSCode at line 2, column 4
        String command = "cmd.exe /c code --goto \"" + file.getCanonicalPath() + ":2:4\"";
        // DebugUtils.printNameValue ( "OSCommand", OSCommand );

        if ( false ) OSCommand.startAndContinue ( command );
    }

    @Test
    public void testGetOSCommandOutput() throws IOException, InterruptedException {

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

        String output = WindowsCmd.getInstructionsOutput ( code, new String[]{"a1", "a2"}, "il1\r\n\r\n" );
        assertNotNull ( output );
        assertEquals ( expected.length(), output.length() );
        assertEquals ( expected, output );
    }
}