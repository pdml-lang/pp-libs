package dev.pp.basics.utilities;

import dev.pp.basics.annotations.NotNull;

public class HTMLUtils {

    public static @NotNull String escapeHTMLText ( @NotNull String unescapedText ) {

        StringBuilder r = new StringBuilder ( unescapedText.length() );

        for ( int i = 0; i < unescapedText.length(); i++) {
            char currentChar = unescapedText.charAt(i);

            switch ( currentChar ) {
                case '"' -> r.append ( "&quot;" );
                case '<' -> r.append ( "&lt;" );
                case '>' -> r.append ( "&gt;" );
                case '&' -> r.append ( "&amp;" );
                default -> r.append ( currentChar );
            }
        }
        return r.toString();
    }
}
