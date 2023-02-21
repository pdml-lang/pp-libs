package dev.pp.text.inspection.message;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

public class TextWarning extends TextInspectionMessage {

    public static final @NotNull String LABEL = "Warning";

/*
    public TextWarning (
        @NotNull String message,
        @Nullable String id,
        @Nullable String textSegment,
        @Nullable TextLocation location,
        boolean hasBeenHandled ) {

        super ( message, id, textSegment, location, hasBeenHandled );
    }
 */

    public TextWarning (
        @NotNull String message,
        @Nullable String id,
        @Nullable String textSegment,
        @Nullable TextLocation location ) {

        super ( message, id, textSegment, location );
    }

    public TextWarning (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token ) {

        super ( message, id, token );
    }


    public @NotNull String label() { return LABEL; }
}
