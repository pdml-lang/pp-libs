package dev.pp.text.inspection.message;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

public class TextError extends TextInspectionMessage {


    public static final @NotNull String LABEL = "Error";


    private boolean hasBeenHandled = false;
    public boolean hasBeenHandled() { return hasBeenHandled; }
    public void setHasBeenHandled ( boolean hasBeenHandled ) { this.hasBeenHandled = hasBeenHandled; }


    public TextError (
        @NotNull String message,
        @Nullable String id,
        @Nullable String textSegment,
        @Nullable TextLocation location ) {

        super ( message, id, textSegment, location );
    }

    public TextError (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token ) {

        super ( message, id, token );
    }


    public @NotNull String label() { return LABEL; }
}
