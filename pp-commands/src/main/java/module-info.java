module dev.pp.commands {

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.datatype;
    requires dev.pp.parameters;
    requires dev.pp.texttable;

    requires info.picocli;

    exports dev.pp.commands.cli;
    exports dev.pp.commands.command;
    exports dev.pp.commands.errors;
    exports dev.pp.commands.picocli;
}
