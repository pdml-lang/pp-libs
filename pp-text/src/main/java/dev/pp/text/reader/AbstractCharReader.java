package dev.pp.text.reader;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.character.CharChecks;
import dev.pp.basics.utilities.character.CharConstants;
import dev.pp.basics.utilities.character.CharConsumer;
import dev.pp.basics.utilities.character.CharPredicate;

import java.io.IOException;

public abstract class AbstractCharReader implements CharReader {

/*
    protected boolean checkHasChar() {

        if ( hasChar() ) {
            return true;
        } else {
            throw new NoSuchElementException (
                "There are no more characters to read at position" + StringConstants.OS_NEW_LINE + currentLocation () );
        }
    }
 */

    public @Nullable String advanceWhile ( @NotNull CharPredicate predicate ) throws IOException {

        // assert checkHasChar();

        StringBuilder sb = new StringBuilder ();
        if ( appendWhile ( predicate, sb ) ) {
            return sb.toString ();
        } else {
            return null;
        }
    }


    // consume

    public void consumeCurrentChar ( @NotNull CharConsumer consumer ) throws IOException {

        // assert checkHasChar();

        consumer.consume ( currentChar() );
        advance();
    }

    public boolean consumeCurrentCharIf ( @NotNull CharPredicate predicate, @NotNull CharConsumer consumer )
        throws IOException {

        // assert checkHasChar();

        if ( ! predicate.accept ( currentChar() ) ) return false;
        consumeCurrentChar ( consumer );
        return true;
    }

    public boolean consumeWhile ( @NotNull CharPredicate predicate, @NotNull CharConsumer consumer )
        throws IOException {

        // assert checkHasChar();

        boolean charFound = false;
        while ( hasChar() ) {
            if ( ! predicate.accept ( currentChar() ) ) return charFound;
            consumer.consume ( currentChar() );
            charFound = true;
            advance();
        }
        return charFound;
    }

    public boolean consumeWhileNotAtCharOrEnd ( char ch, @NotNull CharConsumer consumer ) throws IOException {

        return consumeWhile ( c -> c != ch, consumer );
    }

    public boolean consumeWhileNotAtStringOrEnd ( @NotNull String string, @NotNull CharConsumer consumer ) throws IOException {

        char firstChar = string.charAt ( 0 );
        boolean charFound = false;
        while ( true ) {
            if ( consumeWhileNotAtCharOrEnd ( firstChar, consumer ) ) charFound = true;
            if ( ! hasChar() || isAtString ( string ) ) {
                return charFound;
            }
            consumer.consume ( currentChar() );
            advance();
        }
    }

    public boolean consumeRemaining ( @NotNull CharConsumer consumer ) throws IOException {

        boolean charFound = hasChar();
        while ( hasChar() ) {
            consumer.consume ( currentChar() );
            advance();
        }
        return charFound;
    }


    // append

    public void appendCurrentCharAndAdvance ( @NotNull StringBuilder sb ) throws IOException {

        // assert checkHasChar();

        sb.append ( currentChar() );
        advance();
    }

    public boolean appendCurrentCharIfAndAdvance ( @NotNull CharPredicate predicate, @NotNull StringBuilder sb )
        throws IOException {

        return consumeCurrentCharIf ( predicate, sb::append );
    }

    public boolean appendWhile ( @NotNull CharPredicate predicate, @NotNull StringBuilder sb )
        throws IOException {

        // assert checkHasChar();

        return consumeWhile ( predicate, sb::append );
    }

    public boolean appendWhileNotAtCharOrEnd ( char ch, @NotNull StringBuilder sb ) throws IOException {

        return consumeWhileNotAtCharOrEnd ( ch, sb::append );
    }

    public boolean appendWhileNotAtStringOrEnd ( @NotNull String string, @NotNull StringBuilder sb ) throws IOException {

        return consumeWhileNotAtStringOrEnd ( string, sb::append );
    }

    public boolean appendRemaining ( @NotNull StringBuilder sb ) throws IOException {

        return consumeRemaining ( sb::append );
    }


    // read

    public @Nullable String readWhile ( @NotNull CharPredicate predicate ) throws IOException {

        StringBuilder sb = new StringBuilder();
        appendWhile ( predicate, sb );
        return sb.length() == 0 ? null : sb.toString();
    }

    public @Nullable String readWhileAtChar ( char c ) throws IOException {

        return readWhile ( currentChar -> currentChar == c );
    }

    public @Nullable String readWhileNotAtCharOrEnd ( char ch ) throws IOException {

        StringBuilder sb = new StringBuilder();
        appendWhileNotAtCharOrEnd ( ch, sb );
        return sb.length() == 0 ? null : sb.toString();
    }

    public @Nullable String readWhileNotAtStringOrEnd ( @NotNull String string ) throws IOException {

        StringBuilder sb = new StringBuilder();
        appendWhileNotAtStringOrEnd ( string, sb );
        return sb.length() == 0 ? null : sb.toString();
    }

    public @Nullable String readMaxNChars ( long n ) throws IOException {

        StringBuilder sb = new StringBuilder();
        for ( long i = 1; i <= n; i++ ) {
            if ( ! hasChar() ) break;
            appendCurrentCharAndAdvance ( sb );
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    public @Nullable String readLine ( boolean includeLineBreak ) throws IOException {

        StringBuilder sb = new StringBuilder();
        appendWhile ( CharChecks::isNotNewLine, sb );
        if ( includeLineBreak ) {
            if ( skipChar ( CharConstants.WINDOWS_NEW_LINE_START ) ) {
                sb.append ( CharConstants.WINDOWS_NEW_LINE_START );
            }
            if ( skipChar ( CharConstants.UNIX_NEW_LINE ) ) {
                sb.append ( CharConstants.UNIX_NEW_LINE );
            }
        } else {
            skipNewLine();
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    public @Nullable String readLine() throws IOException {

        StringBuilder sb = new StringBuilder();
        appendWhile ( CharChecks::isNotNewLine, sb );
        skipNewLine();
        return sb.length() == 0 ? null : sb.toString();
    }

    public @Nullable String readRemaining() throws IOException {

        StringBuilder sb = new StringBuilder();
        appendRemaining ( sb );
        return sb.length() == 0 ? null : sb.toString();
    }


    // isAt

    public boolean isAt ( @NotNull CharPredicate predicate ) {

        // assert checkHasChar();

        return predicate.accept ( currentChar() );
    }

    public boolean isAtChar ( char c ) {

        // assert checkHasChar();

        return currentChar() == c;
    }


    // skip

    public boolean skipIf ( @NotNull CharPredicate predicate ) throws IOException {

        // assert checkHasChar();

        return consumeCurrentCharIf ( predicate, c -> {} );
    }

    public boolean skipChar ( char c ) throws IOException {

        // assert checkHasChar();

        if ( currentChar() == c ) {
            advance();
            return true;
        } else {
            return false;
        }
    }

    public boolean skipNewLines() throws IOException {

        boolean skipped = isAtChar ( CharConstants.UNIX_NEW_LINE ) || isAtChar ( CharConstants.WINDOWS_NEW_LINE_START );
        while ( skipNewLine() ) {}
        return skipped;
    }

    public boolean skipNewLine() throws IOException {

        if ( isAtChar ( CharConstants.UNIX_NEW_LINE ) ) {
            advance();
            return true;

        } else if ( isAtChar ( CharConstants.WINDOWS_NEW_LINE_START ) ) {
            advance();
            assert currentChar() == CharConstants.WINDOWS_NEW_LINE_END;
            advance();
            return true;

        } else {
            return false;
        }
    }
    public boolean skipString ( @NotNull String string ) throws IOException {

        // assert checkHasChar();

        if ( isAtString ( string ) ) {
            skipNChars ( string.length() );
            return true;
        } else {
            return false;
        }
    }

    public boolean skipWhile ( @NotNull CharPredicate predicate ) throws IOException {

        // assert checkHasChar();

        return consumeWhile ( predicate, c -> {} );
    }

    public void skipNChars ( long n ) throws IOException {

        // assert checkHasChar();

        if ( n < 0 ) throw new IllegalArgumentException ( "n cannot be < 0, but is " + n + "." );

        for ( long i = 1; i <= n; i++ ) {
            if ( hasChar() ) {
                advance();
            } else {
                throw new IllegalArgumentException (
                    "Cannot skip " + n + " characters, because the end of input has been reached after " + i + " skips."  );
            }
        }
    }


    // state

    public void stateToOSOut ( @Nullable String label ) {

        System.out.println();
        if ( label != null ) System.out.println ( label );
        System.out.println ( stateToString() );
    }
}
