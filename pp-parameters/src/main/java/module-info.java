module dev.pp.parameters {

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.datatype;

    exports dev.pp.parameters.formalParameter;
    exports dev.pp.parameters.parameter;
    exports dev.pp.parameters.textTokenParameter;
    exports dev.pp.parameters.utilities;
    exports dev.pp.parameters.cli;
}
