package dev.pp.basics.utilities.os.process;

import dev.pp.basics.annotations.NotNull;

import java.io.*;
import java.util.List;

public class OSProcess {

    public static Process createProcess ( @NotNull List<String> OSCommandTokens ) throws IOException {

        return createProcess ( OSCommandTokens.toArray ( new String[0] ) );
    }

    public static Process createProcess ( @NotNull String[] OSCommandTokens ) throws IOException {

        return new ProcessBuilder ( OSCommandTokens ).start();
    }

    public static Process createProcess ( @NotNull String OSCommand ) throws IOException {

        // return new ProcessBuilder ( OSCommandAndArgs ).start();
        return Runtime.getRuntime().exec ( OSCommand );
    }

/*
    public static Process createProcess ( @NotNull String command, @Nullable List<String> arguments ) throws IOException {

        return createProcess ( command, arguments == null ? null : arguments.toArray ( new String[0] ) );
    }

    public static Process createProcess ( @NotNull String command, @Nullable String[] arguments ) throws IOException {

        return createProcess ( commandAndArgsToArray ( command, arguments ) );
    }


    private static @NotNull String[] commandAndArgsToArray ( @NotNull String command, @Nullable String[] arguments ) {

        if ( arguments == null ) {
            return new String[]{command};
        } else {
            String[] result = new String[arguments.length + 1];
            result[0] = command;
            System.arraycopy ( arguments, 0, result, 1, arguments.length );
            return result;
        }
    }
*/
}
