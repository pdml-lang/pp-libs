module dev.pp.datatype {

    requires dev.pp.basics;
    requires dev.pp.text;

    exports dev.pp.datatype;
    exports dev.pp.datatype.nonUnion;
    exports dev.pp.datatype.nonUnion.collection;
    exports dev.pp.datatype.nonUnion.scalar.impls.Boolean;
    exports dev.pp.datatype.nonUnion.scalar.impls.date;
    exports dev.pp.datatype.nonUnion.scalar.impls.Enum;
    exports dev.pp.datatype.nonUnion.scalar.impls.file;
    exports dev.pp.datatype.nonUnion.scalar.impls.filesystempath;
    exports dev.pp.datatype.nonUnion.scalar.impls.integer;
    exports dev.pp.datatype.nonUnion.scalar.impls.regex;
    exports dev.pp.datatype.nonUnion.scalar.impls.string;
    exports dev.pp.datatype.union;
    exports dev.pp.datatype.utils.parser;
    exports dev.pp.datatype.utils.validator;
    exports dev.pp.datatype.utils.writer;
    exports dev.pp.datatype.nonUnion.scalar.impls.Null;
}