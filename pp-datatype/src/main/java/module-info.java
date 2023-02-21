module dev.pp.datatype {

    requires dev.pp.basics;
    requires dev.pp.text;

    exports dev.pp.datatype;
    exports dev.pp.datatype.nonunion;
    exports dev.pp.datatype.nonunion.collection;
    exports dev.pp.datatype.nonunion.scalar.impls.Boolean;
    exports dev.pp.datatype.nonunion.scalar.impls.date;
    exports dev.pp.datatype.nonunion.scalar.impls.Enum;
    exports dev.pp.datatype.nonunion.scalar.impls.file;
    exports dev.pp.datatype.nonunion.scalar.impls.filesystempath;
    exports dev.pp.datatype.nonunion.scalar.impls.integer;
    exports dev.pp.datatype.nonunion.scalar.impls.regex;
    exports dev.pp.datatype.nonunion.scalar.impls.string;
    exports dev.pp.datatype.union;
    exports dev.pp.datatype.utils.parser;
    exports dev.pp.datatype.utils.validator;
    exports dev.pp.datatype.utils.writer;
    exports dev.pp.datatype.nonunion.scalar.impls.Null;
}
