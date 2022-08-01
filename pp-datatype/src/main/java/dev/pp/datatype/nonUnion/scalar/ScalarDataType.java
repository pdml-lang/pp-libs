package dev.pp.datatype.nonUnion.scalar;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.nonUnion.NonUnionDataType;
import dev.pp.datatype.utils.parser.DataParserException;
import dev.pp.datatype.utils.validator.DataValidator;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.token.TextToken;

import java.util.function.Supplier;

public abstract class ScalarDataType<T> implements NonUnionDataType<T> {


    protected final @NotNull String name;
    public @NotNull String getName() { return name; }

    protected final @Nullable DataValidator<T> validator;
    public @Nullable DataValidator<T> getValidator() { return validator; }

    protected final @Nullable Supplier<SimpleDocumentation> documentation;
    public @Nullable SimpleDocumentation getDocumentation() { return documentation != null ? documentation.get() : null; }


    public ScalarDataType (
        @NotNull String name,
        @Nullable DataValidator<T> validator,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        this.name = name;
        this.validator = validator;
        this.documentation = documentation;
    }


    // @Nullable -> @NotNull
    @Override
    public abstract @NotNull T parse ( @Nullable String string, @Nullable TextToken token ) throws DataParserException;

    // only NullDataType returns true
    @Override
    public boolean isNullable() { return false; }
}
