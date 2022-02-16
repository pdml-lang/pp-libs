package dev.pp.scripting.docletParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.tools.DocumentationTool;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.directory.DirectoryContentUtils;

public class JavaDocletParser {

    public static boolean parseDirectory (
        @NotNull File directory,
        boolean includeSubDirs,
        @NotNull Class<?> docletClass,
        @Nullable List<String> options ) throws IOException {

        List<File> files =
            includeSubDirs ?
            DirectoryContentUtils.filesInTree ( directory ) :
            DirectoryContentUtils.filesInDirectory ( directory );

        return parseFiles ( files, docletClass, options );
    }

    public static boolean parseFiles (
        @NotNull List<File> files,
        @NotNull Class<?> docletClass,
        @Nullable List<String> options ) {

        DocumentationTool documentationTool = ToolProvider.getSystemDocumentationTool();
        StandardJavaFileManager fileManager = documentationTool.getStandardFileManager (null, null, null );
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjectsFromFiles ( files );

        DocumentationTool.DocumentationTask task = documentationTool.getTask (
            null, fileManager, null, docletClass, options, javaFileObjects );
        return task.call();
    }

    /*
    List<String> options = new ArrayList<> ();
    String modulePath = "C:\\aa\\work\\PDML\\dev\\current\\dev.pdml.ext\\build\\install\\dev.pdml.ext\\lib";
    options.add ( "--module-path" );
    // options.add ( "--class-path" );
    options.add ( modulePath );

    options.add ( "--add-modules" );
    options.add ( "ALL-MODULE-PATH" );
    */
}
