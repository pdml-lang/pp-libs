package dev.pp.text.reader.stack;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.character.CharConsumer;
import dev.pp.basics.utilities.character.CharPredicate;
import dev.pp.text.reader.AbstractCharReader;
import dev.pp.text.reader.CharReader;
import dev.pp.text.reader.CharReaderImpl;
import dev.pp.text.location.TextLocation;
import dev.pp.text.resource.TextResource;
import dev.pp.basics.utilities.string.StringConstants;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;
import java.util.Stack;


public class CharReaderWithInsertsImpl extends AbstractCharReader implements CharReaderWithInserts {


    private final @NotNull Stack<CharReader> readers;

    private @NotNull CharReader currentReader;
    // public @NotNull CharReader getCurrentReader() { return currentReader; }

    // Can possibly be uncommented when 'mark' and 'readahead' functions are fixed (see comment below)
//    private boolean hasChar;
//    private char currentChar;


    // constructors

    public CharReaderWithInsertsImpl ( @NotNull CharReader initialReader ) {

        this.readers = new Stack<>();

//        this.hasChar = initialReader.hasChar();
//        this.currentChar = initialReader.currentChar();

        insert ( initialReader );
    }

    public CharReaderWithInsertsImpl ( @NotNull Reader reader, @Nullable TextResource resource ) throws IOException {

        this ( new CharReaderImpl ( reader, resource ) );
    }

    /*
    public CharReaderWithInsertsImpl ( @NotNull File file ) throws IOException {

        this ( new CharReaderImpl ( file ) );
    }

    public CharReaderWithInsertsImpl ( @NotNull URL url ) throws IOException {

        this ( new CharReaderImpl ( url ) );
    }
    */

    public CharReaderWithInsertsImpl ( @NotNull String string ) {

        this ( CharReaderImpl.createForString ( string ) );
    }


    // push

    public void insert ( @NotNull CharReader reader ) {

        readers.push ( reader );
        currentReader = reader;
    }

    public void insert ( @NotNull Path filePath ) throws IOException {

        insert ( new CharReaderImpl ( filePath ) );
    }

    public void insert ( @NotNull URL url ) throws IOException {

        insert ( new CharReaderImpl ( url ) );
    }

    public void insert ( @NotNull String string ) {

        insert ( CharReaderImpl.createForString ( string ) );
    }


    // iteration

    public boolean hasChar() { return currentReader.hasChar(); }
//    public boolean hasChar() { return hasChar; }

    public char currentChar () { return currentReader.currentChar(); }
//    public char currentChar () { return currentChar; }


    // advance

    public void advance() throws IOException {

        currentReader.advance();

        while ( true ) {

            if ( currentReader.hasChar() ) {
//                this.hasChar = true;
//                this.currentChar = currentReader.currentChar();
                return;
            }

            readers.pop();

            if ( readers.empty() ) {
//                this.hasChar = false;
//                this.currentChar = 0;
                return;
            }

            currentReader = readers.peek();
        }
    }


    // location

    public @NotNull TextLocation currentLocation () {

        if ( readers.isEmpty() ) return currentReader.currentLocation();

        TextLocation result = null;
        for ( CharReader reader : readers ) {
            result = new TextLocation (
                reader.resource(), reader.currentLineNumber (), reader.currentColumnNumber (), result );
        }
        return result;
    }

    public @Nullable TextResource resource() { return currentReader.resource(); }

    public int currentLineNumber () { return currentReader.currentLineNumber(); }

    public int currentColumnNumber () { return currentReader.currentColumnNumber(); }


    // isAt

    // TODO does not work if part of the end of the string is on stacked reader
    // can be fixed after 'mark' functions are fixed (see comment below)
    public boolean isAtString ( @NotNull String s ) throws IOException { return currentReader.isAtString ( s ); }



    /*
        TODO !!!
        The following mark and peek functions do not work correctly if the current reader is finished
        and more non-finished readers are on the stack.
        Instead of delegating to currentReader, these functions should have their own implementation that considers
        all readers on the stack
    */

    // read-ahead

    public void setMark ( int readAheadLimit ) { currentReader.setMark ( readAheadLimit ); }

    public void goBackToMark() { currentReader.goBackToMark(); }


    // peek

    public @Nullable Character peekNextChar() throws IOException {
        return currentReader.peekNextChar(); }

    public boolean isNextChar ( char c ) throws IOException {
        return currentReader.isNextChar ( c ); }

    public @Nullable String peekNextMaxNChars ( int n ) throws IOException {
        return currentReader.peekNextMaxNChars ( n ); }

    public @Nullable Character peekCharAfterRequired (
        @NotNull CharPredicate predicate, int lookAhead ) throws IOException {

        return currentReader.peekCharAfterRequired ( predicate, lookAhead );
    }

    public @Nullable Character peekCharAfterOptional (
        @NotNull CharPredicate predicate, int lookAhead ) throws IOException {

        return currentReader.peekCharAfterOptional ( predicate, lookAhead );
    }


    // debugging

    public @NotNull String stateToString() {

        StringBuilder sb = new StringBuilder();

        sb.append ( "Readers on stack: " );
        sb.append ( readers.size() );
        sb.append ( StringConstants.OS_NEW_LINE );

        int i = 1;
        for ( CharReader reader : readers ) {
            sb.append ( "#" + i + ": " + reader.stateToString() );
            sb.append ( StringConstants.OS_NEW_LINE );
            i++;
        }
        sb.append ( "current reader: " );
        sb.append ( currentReader.stateToString() );

        return sb.toString();
    }
}
