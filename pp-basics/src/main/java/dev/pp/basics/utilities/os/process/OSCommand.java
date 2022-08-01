package dev.pp.basics.utilities.os.process;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class OSCommand {

    public static record OSCommandResult (
        @Nullable String stdout,
        @Nullable String stderr,
        int exitCode ) {

        public boolean hasOutput() {
            return stdout != null && ! stdout.isEmpty();
        }

        public boolean hasError() {
            return exitCode != 0 ||
                ( stderr != null && ! stderr.isEmpty() );
        }
    }


    // runAndWait

    public static int runAndWait ( @NotNull List<String> OSCommandTokens ) throws IOException, InterruptedException {

        return runAndWait ( OSProcess.createProcess ( OSCommandTokens ) );
    }

    public static int runAndWait ( @NotNull String[] OSCommandTokens ) throws IOException, InterruptedException {

        return runAndWait ( OSProcess.createProcess ( OSCommandTokens ) );
    }

    public static int runAndWait ( @NotNull String OSCommand ) throws IOException, InterruptedException {

        return runAndWait ( OSProcess.createProcess ( OSCommand ) );
    }

    private static int runAndWait ( @NotNull Process process ) throws InterruptedException {

        redirectAll ( process );
        return process.waitFor();
    }


    // startAndContinue

    public static @NotNull Process startAndContinue ( @NotNull List<String> OSCommandTokens ) throws IOException {

        return startAndContinue ( OSProcess.createProcess ( OSCommandTokens ) );
    }

    public static @NotNull Process startAndContinue ( @NotNull String[] OSCommandTokens ) throws IOException {

        return startAndContinue ( OSProcess.createProcess ( OSCommandTokens ) );
    }

    public static @NotNull Process startAndContinue ( @NotNull String OSCommand ) throws IOException {

        return startAndContinue ( OSProcess.createProcess ( OSCommand ) );
    }

    private static @NotNull Process startAndContinue ( @NotNull Process process ) {

        redirectAll ( process );
        return process;
    }


    public static @NotNull OSCommandResult runWithStrings (
        @NotNull String[] OSCommandTokens,
        @Nullable String input ) throws IOException, InterruptedException {

        Process process = OSProcess.createProcess ( OSCommandTokens );

        if ( input != null ) {
            OSProcessRedirector.stdinFromString ( process, input, StandardCharsets.UTF_8 );
        }

        final var outWriter = new StringWriter();
        OSProcessRedirector.stdOutToStringWriter ( process, outWriter, StandardCharsets.UTF_8 );

        final var errWriter = new StringWriter();
        OSProcessRedirector.stdErrToStringWriter ( process, errWriter, StandardCharsets.UTF_8 );

        int result = process.waitFor();

        String stdout = outWriter.toString();
        if ( stdout.isEmpty() ) stdout = null;
        String sterr = errWriter.toString();
        if ( sterr.isEmpty() ) sterr = null;
        return new OSCommandResult ( stdout, sterr, result );
    }

    public static int runWithFiles (
        @NotNull String[] OSCommandTokens,
        @Nullable Path inputFile,
        @Nullable Path outputFile,
        @Nullable Path errorFile ) throws IOException, InterruptedException {

        Process process = OSProcess.createProcess ( OSCommandTokens );

        if ( inputFile != null ) {
            OSProcessRedirector.stdInFromFile ( process, inputFile, StandardCharsets.UTF_8 );
        }
        if ( outputFile != null ) {
            OSProcessRedirector.stdOutToFile ( process, outputFile, StandardCharsets.UTF_8 );
        }
        if ( errorFile != null ) {
            OSProcessRedirector.stdErrToFile ( process, errorFile, StandardCharsets.UTF_8 );
        }

        return process.waitFor();
    }

    public static @Nullable String textPipe (
        @NotNull String[] OSCommandTokens,
        @Nullable String input ) throws IOException, InterruptedException {

        Process process = OSProcess.createProcess ( OSCommandTokens );

        if ( input != null ) {
            OSProcessRedirector.stdinFromString ( process, input, StandardCharsets.UTF_8 );
        }

        // final StringBuilder sb = new StringBuilder();
        // OSProcessRedirector.stdOutToStringBuilder ( process, sb, StandardCharsets.UTF_8 );
        final var writer = new StringWriter();
        OSProcessRedirector.stdOutToStringWriter ( process, writer, StandardCharsets.UTF_8 );

        OSProcessRedirector.stdErrToThisErr ( process );

        int result = process.waitFor();
        if ( result != 0 ) {
            throw new IOException ( "Command '" + Arrays.toString ( OSCommandTokens ) + "' returned with error code " + result + ".");
        }

        // return sb.isEmpty() ? null : sb.toString();
        var r = writer.toString();
        return r.isEmpty() ? null : r;
    }

    public static @NotNull String[] commandAndArgsToArray ( @NotNull String command, @Nullable String[] arguments ) {

        if ( arguments == null ) {
            return new String[]{command};
        } else {
            String[] result = new String[arguments.length + 1];
            result[0] = command;
            System.arraycopy ( arguments, 0, result, 1, arguments.length );
            return result;
        }
    }


    private static void redirectAll ( Process process ) {

        OSProcessRedirector.allFromThis ( process, StandardCharsets.UTF_8 );
    }
}
