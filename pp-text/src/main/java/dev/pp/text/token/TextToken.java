package dev.pp.text.token;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.utilities.string.StringTruncator;

public class TextToken {


    private final @NotNull String text;
    public @NotNull String getText() { return text; }

    private final @Nullable TextLocation location;
    public @Nullable TextLocation getLocation() { return location; }


    public TextToken ( @NotNull String text, @Nullable TextLocation location ) {
        // An empty string represents a null value
        // if ( text.isEmpty() ) throw new IllegalArgumentException ( "'text' cannot be empty." );

        this.text = text;
        this.location = location;
    }

    public TextToken ( char c, @Nullable TextLocation location ) { this ( String.valueOf ( c ), location ); }

    public TextToken ( @NotNull String text ) { this ( text, null ); }


    public boolean isNullText() { return text.isEmpty(); }

    public @Nullable String getTextOrNullIfEmpty() { return text.isEmpty() ? null : text; }

    public @Nullable String getResourceName() { return location == null ? null : location.getResourceName(); }

    @Override public String toString() {
        return StringTruncator.truncateWithEllipses ( text ) +
        ( location != null ? " at " + location : "" ); }

    /* TODO
        public @Nullable TextLocation getEndLocation () {
            // compute from text and location
        }
    */
}
