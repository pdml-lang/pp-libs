package dev.pp.datatype.nonUnion.collection;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.utils.parser.DataParserException;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.token.TextToken;

import java.util.ArrayList;
import java.util.List;

public interface ListDataType<E> extends CollectionDataType<List<E>, E> { // extends CollectionDataType<T> {

    default @NotNull List<E> parseElements (
        @NotNull List<String> elements,
        @Nullable TextToken token ) throws DataParserException {

        boolean nullElementsAllowed = isNullElementsAllowed();

        List<E> list = new ArrayList<> ();
        for ( String element : elements ) {
            if ( element != null && ! element.isEmpty() ) {
                try {
                    list.add ( parseAndValidateElement ( element, token ) );
                } catch ( DataValidatorException e ) {
                    throw new DataParserException ( e,token );
                }
            } else {
                if ( nullElementsAllowed ) {
                    list.add ( null );
                } else {
                    throw new DataParserException (
                        "INVALID_EMPTY_ELEMENT",
                        "An empty (null) element is not allowed.",
                        token );
                }
            }
        }

        if ( ! list.isEmpty() ) {
            return list;
        } else {
            throw new DataParserException (
                "INVALID_EMPTY_COLLECTION",
                "An empty (null) collection is not allowed.",
                token );
        }
    }
}
