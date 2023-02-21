package dev.pp.parameters.utilities;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.parameters.MutableOrImmutableParameters;
import dev.pp.parameters.parameterspec.ParameterSpec;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.inspection.TextErrorUtils;

import java.io.IOException;

public class TextErrorUtils2 {

    public static void showInEditor (
        @NotNull TextError error,
        @NotNull MutableOrImmutableParameters<?> parameters,
        @NotNull ParameterSpec<?> openFileOSCommandTemplateParameterSpec ) throws IOException {

        @Nullable String template = parameters.castedValue ( openFileOSCommandTemplateParameterSpec );
        if ( template == null ) return;

        TextErrorUtils.showInEditor ( error, template );
    }
}
