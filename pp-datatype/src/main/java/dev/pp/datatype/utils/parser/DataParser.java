package dev.pp.datatype.utils.parser;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

public interface DataParser<T> {

    @NotNull T parse ( @NotNull String string, @Nullable TextToken token ) throws DataParserException;
}
