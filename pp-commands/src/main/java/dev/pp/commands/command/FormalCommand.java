package dev.pp.commands.command;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.commands.errors.CLIExceptionHandler;
import dev.pp.commands.picocli.PicocliHelper;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.parameters.parameter.list.Parameters;
import dev.pp.parameters.parameter.list.ParametersCreator;
import dev.pp.text.documentation.SimpleDocumentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class FormalCommand<T> {


    public static @NotNull <T> Builder<T> builder (
        @NotNull String name,
        @NotNull CommandExecutor<T> executor ) {

        return new Builder<> ( name, executor );
    }


    public static class Builder<T> {

        private final @NotNull String name;

        private @Nullable Set<String> alternativeNames;

        private @Nullable FormalParameters inputParameters;

        private final @NotNull CommandExecutor<T> executor;

        private @Nullable Integer sortIndex;

        private @Nullable Supplier<SimpleDocumentation> documentationSupplier;


        public Builder (
            @NotNull String name,
            @NotNull CommandExecutor<T> executor ) {

            this.name = name;
            this.alternativeNames = null;
            this.inputParameters = null;
            this.executor = executor;
            this.sortIndex = null;
            this.documentationSupplier = null;
        }


        public Builder<T> alternativeNames ( @Nullable Set<String> alternativeNames ) {
            this.alternativeNames = alternativeNames;
            return this;
        }

        public Builder<T> alternativeName ( @NotNull String alternativeName ) {
            this.alternativeNames = Set.of ( alternativeName );
            return this;
        }

        public Builder<T> inputParameters ( @Nullable FormalParameters inputParameters ) {
            this.inputParameters = inputParameters;
            return this;
        }

        public Builder<T> addInputParameter ( @NotNull FormalParameter<?> parameter ) {
            if ( inputParameters == null ) inputParameters = new FormalParameters();
            inputParameters.add ( parameter );
            return this;
        }

        public Builder<T> sortIndex ( @Nullable Integer sortIndex ) {
            this.sortIndex = sortIndex;
            return this;
        }

        public Builder<T> documentationSupplier ( @Nullable Supplier<SimpleDocumentation> documentationSupplier ) {
            this.documentationSupplier = documentationSupplier;
            return this;
        }

        public Builder<T> documentation ( @Nullable SimpleDocumentation documentation ) {
            this.documentationSupplier = () -> documentation;
            return this;
        }

        public Builder<T> documentation (
            @NotNull String title,
            @Nullable String description,
            @Nullable String examples ) {

            return documentation ( new SimpleDocumentation ( title, description, examples ) );
        }


        public FormalCommand<T> build() {

            return new FormalCommand<> (
                name,
                alternativeNames,
                inputParameters,
                executor,
                sortIndex,
                documentationSupplier );
        }
    }


    private final @NotNull String name;
    public @NotNull String getName() { return name; }

    private final @Nullable Set<String> alternativeNames;
    public @Nullable Set<String> getAlternativeNames() { return alternativeNames; }

    private final @Nullable FormalParameters inputParameters;
    public @Nullable FormalParameters getInputParameters() { return inputParameters; }

    private final @NotNull CommandExecutor<T> executor;
    public @Nullable CommandExecutor<T> getExecutor() { return executor; }

    private final @Nullable Integer sortIndex;
    public @Nullable Integer getSortIndex() { return sortIndex; }

    private final @Nullable Supplier<SimpleDocumentation> documentationSupplier;
    public @Nullable Supplier<SimpleDocumentation> getDocumentationSupplier() { return documentationSupplier; }

    /* TODO?
		boolean allowProcessExitAfterExecution
		@Nullable String menuP
        boolean obsolete
     */


    public FormalCommand (
        @NotNull String name,
        @Nullable Set<String> alternativeNames,
        @Nullable FormalParameters inputParameters,
        @NotNull CommandExecutor<T> executor,
        @Nullable Integer sortIndex,
        @Nullable Supplier<SimpleDocumentation> documentationSupplier ) {

        if ( alternativeNames != null && alternativeNames.contains ( name ) ) throw new IllegalArgumentException (
            "Name '" + name + "' cannot be re-used in the list of alternative names." );

        this.name = name;
        this.alternativeNames = alternativeNames;
        this.inputParameters = inputParameters;
        this.executor = executor;
        this.sortIndex = sortIndex;
        this.documentationSupplier = documentationSupplier;
    }

    public T execute ( @Nullable Map<String, String> stringParameters ) throws Exception {

        return executor.execute ( stringParameters, inputParameters );
    }

    public T execute (
        @Nullable Map<String, String> stringParameters,
        @Nullable Parameters parameters ) throws Exception {

        return executor.execute ( stringParameters, parameters );
    }

    public @NotNull List<String> getAllNames() {

        ArrayList<String> list = new ArrayList<>();
        list.add ( name );
        if ( alternativeNames != null ) list.addAll ( alternativeNames );
        return list;
    }

    public int getSortIndexOrZero() { return sortIndex == null ? 0 : sortIndex; }



    // Documentation

    public @Nullable SimpleDocumentation getDocumentation() {
        return documentationSupplier != null ? documentationSupplier.get() : null;
    }

    public @Nullable String getDocumentationTitle() {
        SimpleDocumentation documentation = getDocumentation();
        return documentation != null ? documentation.getTitle() : null;
    }

    public @Nullable String getDocumentationDescription() {
        SimpleDocumentation documentation = getDocumentation();
        return documentation != null ? documentation.getDescription() : null;
    }

    public @Nullable String getDocumentationExamples() {
        SimpleDocumentation documentation = getDocumentation();
        return documentation != null ? documentation.getExamples() : null;
    }


    @Override public String toString() { return name; }
}
