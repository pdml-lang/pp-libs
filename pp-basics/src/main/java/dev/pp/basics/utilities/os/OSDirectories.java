package dev.pp.basics.utilities.os;

import dev.pp.basics.annotations.NotNull;

import java.nio.file.Path;

public class OSDirectories {

    public static final @NotNull String DIRECTORY_SEPARATOR = System.getProperty ( "file.separator" );

    public static final @NotNull Path USER_HOME_DIRECTORY = Path.of ( System.getProperty ( "user.home" ) );

    public static final @NotNull Path TEMPORARY_FILES_DIRECTORY = Path.of ( System.getProperty ( "java.io.tmpdir" ) );

    public static @NotNull Path currentWorkingDirectory() {
        return Path.of ( System.getProperty ( "user.dir" ) );
    }
}
