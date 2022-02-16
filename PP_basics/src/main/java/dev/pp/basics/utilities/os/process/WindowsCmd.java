package dev.pp.basics.utilities.os.process;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TempFileUtils;

import java.io.File;
import java.io.IOException;

public class WindowsCmd {

    public static @Nullable String getInstructionsOutput (
        @NotNull String instructions,
        @Nullable String[] arguments,
        @Nullable String input ) throws IOException, InterruptedException {

        File cmdFile = TempFileUtils.createNonEmptyTempTextFile ( instructions, null, "bat",true );

        return getCmdFileOutput ( cmdFile, arguments, input );
    }

    public static @Nullable String getCmdFileOutput (
        @NotNull File cmdFile,
        @Nullable String[] arguments,
        @Nullable String input ) throws IOException, InterruptedException {

        return OSCommand.textPipe ( OSCommand.commandAndArgsToArray ( cmdFile.toString(), arguments ), input );
    }

}
