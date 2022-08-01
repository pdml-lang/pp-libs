package dev.pp.datatype.nonUnion.scalar.impls.regex;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.nonUnion.scalar.ScalarDataType;
import dev.pp.datatype.nonUnion.scalar.impls.Null.NullDataType;
import dev.pp.datatype.utils.parser.DataParserException;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.token.TextToken;

import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexDataType extends ScalarDataType<Pattern> {


    public static final @NotNull RegexDataType DEFAULT = new RegexDataType (
        "regex",
        null,
        () -> new SimpleDocumentation (
            "Regular Expression", "A regular expression (regex).", "[a-zA-Z_][a-zA-Z0-9_\\.-]*" ));


    public RegexDataType (
        @NotNull String name,
        @Nullable DataValidator<Pattern> validator,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        super ( name, validator, documentation );
    }


    @NotNull public Pattern parse (
        @Nullable String string,
        @Nullable TextToken token ) throws DataParserException {

        NullDataType.checkNotNullString ( string, token );
        assert string != null;

        try {
            return Pattern.compile ( string );
        } catch ( PatternSyntaxException e ) {
            throw new DataParserException (
                "ILLEGAL_REGEX_VALUE",
                "'" + string + "' is an invalid regular expression. Reason: " + e.getMessage(),
                token );
        }
    }
}
