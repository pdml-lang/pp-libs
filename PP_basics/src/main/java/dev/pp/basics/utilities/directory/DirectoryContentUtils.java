package dev.pp.basics.utilities.directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.FileCheckUtils;

public class DirectoryContentUtils {

    public static final String DIRECTORY_NAME_EXCLUDE_PREFIX = "---";
    public static final String FILE_NAME_EXCLUDE_PREFIX = "---";

    public static @Nullable List<File> filesInDirectory ( @NotNull File directory ) throws IOException {

        List<File> files = new ArrayList<>();
        forEachFileInDirectory ( directory, files::add );
        return files.isEmpty() ? null : files;
    }

    public static void forEachFileInDirectory (
        @NotNull File directory,
        @NotNull Consumer<File> fileConsumer ) throws IOException {

        Files.list ( directory.toPath() )
            .filter ( path -> ! path.toFile().isDirectory() )
            .forEach ( path -> fileConsumer.accept ( path.toFile() ) );
    }

    public static @Nullable List<File> filesInTree ( @NotNull File rootDirectory ) throws IOException {

        List<File> files = new ArrayList<>();
        forEachFileInTree ( rootDirectory, files::add );
        return files.isEmpty() ? null : files;
    }

    public static void forEachFileInTree (
        @NotNull File rootDirectory,
        @NotNull Consumer<File> fileConsumer ) throws IOException {

        forEachFileInTree ( rootDirectory, fileConsumer, null, null );
    }

    public static void forEachFileWithExtensionInTree (
        @NotNull File rootDirectory,
        @NotNull String fileNameExtension,
        @NotNull Consumer<File> fileConsumer ) throws IOException {

        forEachFileInTree (
            rootDirectory,
            fileConsumer,
            null,
            file -> FileCheckUtils.hasExtension ( file, fileNameExtension ) );
    }

    public static void forEachNonExcludedFileWithExtensionInTree (
        @NotNull File rootDirectory,
        @NotNull String fileNameExtension,
        @NotNull Consumer<File> fileConsumer ) throws IOException {

        forEachFileInTree (
            rootDirectory,
            fileConsumer,
            directory -> ! directory.getName().startsWith ( DIRECTORY_NAME_EXCLUDE_PREFIX ),
            file -> {
                String fileName = file.getName();
                if ( fileName.startsWith ( FILE_NAME_EXCLUDE_PREFIX ) ) return false;
                return FileCheckUtils.hasExtension ( file, fileNameExtension );
            } );
    }

    public static void forEachFileInTree (
        @NotNull File rootDirectory,
        @NotNull Consumer<File> fileConsumer,
        @Nullable Predicate<File> directoryPredicate,
        @Nullable Predicate<File> filePredicate ) throws IOException {

        Files.walkFileTree ( rootDirectory.toPath(), EnumSet.of ( FileVisitOption.FOLLOW_LINKS ), Integer.MAX_VALUE,
            new SimpleFileVisitor<Path> () {

                @Override
                public FileVisitResult preVisitDirectory ( Path directoryPath, BasicFileAttributes attributes )
                    throws IOException {

                    boolean includeDirectory =
                        directoryPredicate == null || directoryPredicate.test ( directoryPath.toFile() );
                    return includeDirectory ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult visitFile ( Path filePath, BasicFileAttributes attributes ) throws IOException {

                    File file = filePath.toFile();
                    boolean includeFile = filePredicate == null || filePredicate.test ( file );
                    if ( includeFile ) fileConsumer.accept ( file );
                    return FileVisitResult.CONTINUE;
                }
            });
    }
}
