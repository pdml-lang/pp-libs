package dev.pp.basics.utilities.directory;

import java.io.File;

import dev.pp.basics.annotations.NotNull;

public class DirectoryUtils {

    public static @NotNull File currentWorkingDirectory() {
        return new File ( System.getProperty ( "user.dir" ) ); }
}
