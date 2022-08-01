package dev.pp.basics.utilities.os.process;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TempFileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WindowsScript {

    public static final @NotNull String CMD_FILE_EXTENSIONS = "cmd";
    public static final @NotNull String BATCH_FILE_EXTENSIONS = "bat";
    public static final @NotNull String POWERSHELL_FILE_EXTENSIONS = "ps1";


    // run and wait

    public static int runScriptFileAndWait (
        @NotNull Path scriptFile,
        @Nullable String[] arguments ) throws IOException, InterruptedException {

        return OSCommand.runAndWait ( OSCommand.commandAndArgsToArray ( scriptFile.toString(), arguments ) );
    }

    public static int runScriptAndWait (
        @NotNull String script,
        @NotNull String scriptFileExtension,
        @Nullable String[] arguments ) throws IOException, InterruptedException {

        return runScriptFileAndWait ( createScriptFile ( script, scriptFileExtension ), arguments );
    }

    public static int runCmdScriptAndWait (
        @NotNull String script,
        @Nullable String[] arguments ) throws IOException, InterruptedException {

        return runScriptAndWait ( script, CMD_FILE_EXTENSIONS, arguments );
    }


    // start and continue

    public static void startScriptFileAndContinue (
        @NotNull Path scriptFile,
        @Nullable String[] arguments ) throws IOException, InterruptedException {

        OSCommand.startAndContinue ( OSCommand.commandAndArgsToArray ( scriptFile.toString(), arguments ) );
    }

    public static void startScriptAndContinue (
        @NotNull String script,
        @NotNull String scriptFileExtension,
        @Nullable String[] arguments ) throws IOException, InterruptedException {

        startScriptFileAndContinue ( createScriptFile ( script, scriptFileExtension ), arguments );
    }

    public static void startCmdScriptAndContinue (
        @NotNull String script,
        @Nullable String[] arguments ) throws IOException, InterruptedException {

        startScriptAndContinue ( script, CMD_FILE_EXTENSIONS, arguments );
    }


    // text pipe

    public static @Nullable String textPipeWithScriptFile (
        @NotNull Path scriptFile,
        @Nullable String[] arguments,
        @Nullable String input ) throws IOException, InterruptedException {

        return OSCommand.textPipe ( OSCommand.commandAndArgsToArray ( scriptFile.toString(), arguments ), input );
    }

    public static @Nullable String textPipeWithScript (
        @NotNull String script,
        @NotNull String scriptFileExtension,
        @Nullable String[] arguments,
        @Nullable String input ) throws IOException, InterruptedException {

        return textPipeWithScriptFile ( createScriptFile ( script, scriptFileExtension ), arguments, input );
    }

    public static @Nullable String textPipeWithCmdScript (
        @NotNull String script,
        @Nullable String[] arguments,
        @Nullable String input ) throws IOException, InterruptedException {

        return textPipeWithScript ( script, CMD_FILE_EXTENSIONS, arguments, input );
    }


    // run in new window and wait

    // https://stackoverflow.com/questions/28318643/getting-the-exit-code-of-an-application-started-with-the-cmd-and-start-comma

    public static int runCmdScriptFileInNewWindowAndWait (
        @NotNull Path cmdScriptFile,
        @Nullable Path workingDirectory,
        @Nullable List<String> arguments,
        @Nullable String windowTitle ) throws IOException, InterruptedException {

        return runScriptFileInNewWindowAndWait ( cmdScriptFile, workingDirectory, arguments, windowTitle, true );
    }

    public static int runShScriptFileInNewWindowAndWait (
        @NotNull Path cmdScriptFile,
        @Nullable Path workingDirectory,
        @Nullable List<String> arguments,
        @Nullable String windowTitle ) throws IOException, InterruptedException {

        return runScriptFileInNewWindowAndWait ( cmdScriptFile, workingDirectory, arguments, windowTitle, false );
    }

    private static int runScriptFileInNewWindowAndWait (
        @NotNull Path scriptFile,
        @Nullable Path workingDirectory,
        @Nullable List<String> arguments,
        @Nullable String windowTitle,
        boolean useCmdExe ) throws IOException, InterruptedException {

        /*
            @echo off
            start "title" /d "work_dir" /wait cmd.exe /c "path/name.cmd" "arg1" "arg2"
            if errorlevel 1 (exit 1) else (exit 0)
         */

        StringBuilder script = new StringBuilder ();

        script.append ( "@echo off\r\n" );

        script.append ( "start \"" );
        script.append ( windowTitle != null ? windowTitle : "Script" );
        script.append ( "\"" );

        if ( workingDirectory != null ) {
            script.append ( " /d " );
            script.append ( "\"" );
            script.append ( workingDirectory );
            script.append ( "\"" );
        }

        if ( useCmdExe ) {
            script.append ( " /wait cmd.exe /c \"" );
        } else {
            script.append ( " /wait \"" );
        }
        script.append ( scriptFile );
        script.append ( "\"" );

        if ( arguments != null ) {
            for ( String argument : arguments ) {
                script.append ( " \"" );
                script.append ( argument );
                script.append ( "\"" );
            }
        }

        script.append ( "\r\nif errorlevel 1 (exit 1) else (exit 0)\r\n" );

        return runCmdScriptAndWait ( script.toString(), null );
    }

    @Deprecated
    public static int runCmdScriptFileInNewWindowAndWaitDeprecated (
        @NotNull Path scriptFile,
        @Nullable Path workingDirectory,
        @Nullable List<String> arguments,
        @Nullable String windowTitle ) throws IOException, InterruptedException {

        // syntax:
        // cmd.exe /v /e /c start "title" /d work_dir /wait scriptFilePath arg1 arg2 ... & exit !errorlevel!
        // Many thanks to https://stackoverflow.com/questions/42597703/how-to-get-return-exit-code-of-a-batch-file-started-with-start-command

        List<String> tokens = new ArrayList<> ();
        tokens.add ( "cmd.exe" );
        tokens.add ( "/v" );
        tokens.add ( "/e" );
        tokens.add ( "/c" );

        tokens.add ( "start" );
        tokens.add ( "\"" + (windowTitle == null ? "Script" : windowTitle) + "\"" );

        if ( workingDirectory != null ) {
            tokens.add ( "/d" );
            tokens.add ( workingDirectory.toString() );
        }

        tokens.add ( "/wait" );

        tokens.add ( scriptFile.toString() );

        if ( arguments != null ) {
            tokens.addAll ( arguments );
        }

        tokens.add ( "&" );
        tokens.add ( "exit" );
        // tokens.add ( "^!errorlevel^!" );
        tokens.add ( "!errorlevel!" );

        return OSCommand.runAndWait ( tokens );
    }

/*
    public static int runWindowsScriptFileInNewWindowAndWait (
        @NotNull Path scriptFile,
        @Nullable Path workingDirectory,
        @Nullable List<String> arguments,
        @Nullable String windowTitle ) throws IOException, InterruptedException {

        // syntax:
        // start "Test" /d work_dir /wait cmd.exe /c exit_on_first_error.cmd

        List<String> tokens = new ArrayList<>();

        tokens.add ( "start" );
        tokens.add ( "\"" + (windowTitle == null ? "Script" : windowTitle) + "\"" );

        if ( workingDirectory != null ) {
            tokens.add ( "/d" );
            tokens.add ( workingDirectory.toString() );
        }

        tokens.add ( "/wait" );

        tokens.add ( "cmd.exe" );
        tokens.add ( "/c" );

        tokens.add ( scriptFile.toString() );

        if ( arguments != null ) {
            tokens.addAll ( arguments );
        }

        return OSCommand.runAndWait ( tokens );
    }
*/

    public static int runCmdScriptInNewWindowAndWait (
        @NotNull String script,
        @NotNull String scriptFileExtension,
        @Nullable Path workingDirectory,
        @Nullable List<String> arguments,
        @Nullable String windowTitle ) throws IOException, InterruptedException {

        return runCmdScriptFileInNewWindowAndWait (
            createScriptFile ( script, scriptFileExtension ), workingDirectory, arguments, windowTitle );
    }


    private static @NotNull Path createScriptFile (
        @NotNull String script,
        @NotNull String fileExtension ) throws IOException {

        return TempFileUtils.createNonEmptyTempTextFile ( script, null, fileExtension, true );
    }
}
