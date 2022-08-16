package dev.pp.parameters.utilities;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.parameter.Parameters;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextErrorUtils;

import java.io.IOException;

public class TextErrorUtils2 {

    public static void showInEditor (
        @NotNull TextError error,
        @NotNull Parameters parameters,
        @NotNull FormalParameter<?> openFileOSCommandTemplateFormalParameter ) throws IOException {

        @Nullable String openFileOSCommandTemplate = parameters.getNullable (
            openFileOSCommandTemplateFormalParameter );
        if ( openFileOSCommandTemplate == null ) return;

        TextErrorUtils.showInEditor ( error, openFileOSCommandTemplate );
    }
}
