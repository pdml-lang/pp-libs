package dev.pp.text.resource;

import java.io.File;
import java.io.IOException;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.utilities.file.FilePathUtils;
import dev.pp.text.utilities.file.TextFileUtils;

public class File_TextResource implements TextResource {

    private final @NotNull File file;


    public File_TextResource ( @NotNull File file ) {

        this.file = file;
    }


    public @NotNull File getResource() { return file; }

    public @NotNull String getName() { return FilePathUtils.getAbsoluteOSPath ( file ); }

    public @NotNull String getTextLine ( long lineNumber ) throws IOException {

        return TextFileUtils.getNthLineInFile ( file, lineNumber );
    }

    @Override
    public String toString() { return "File: " + FilePathUtils.getAbsoluteOSPath ( file ); }
}
