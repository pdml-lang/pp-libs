package dev.pp.basics.utilities.string;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.utilities.character.CharPredicate;

public class StringFinder {

    public static int findFirstIndex ( @NotNull String string, CharPredicate predicate ) {

        for ( int i = 0; i < string.length(); i++ ) {
            char c = string.charAt ( i );
            if ( predicate.accept ( c ) ) {
                return i;
            }
        }
        return -1;
    }
}
