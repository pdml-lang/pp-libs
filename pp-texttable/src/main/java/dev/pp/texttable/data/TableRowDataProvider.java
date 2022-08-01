package dev.pp.texttable.data;

import dev.pp.basics.annotations.NotNull;

import java.util.Iterator;

public interface TableRowDataProvider<T> {

    @NotNull Iterator<T> cells();
}
