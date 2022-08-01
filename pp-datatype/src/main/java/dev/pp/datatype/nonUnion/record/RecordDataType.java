package dev.pp.datatype.nonUnion.record;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.nonUnion.NonUnionDataType;
import dev.pp.datatype.utils.parser.DataParser;
import dev.pp.datatype.utils.writer.DataWriter;

public interface RecordDataType<T, F> extends NonUnionDataType<T> {

    // this creates a circular dependency. Solution?: move dev.pp.datatype to dev.pp.parameters
    // @NotNull FormalParameters<F> getFields();
    boolean hasField ( @NotNull String fieldName );
    // @NotNull FormalParameter<F> getField ( @NotNull String fieldName );

    @Nullable DataParser<T> getParser();

    @Nullable DataWriter<T> getWriter();
}
