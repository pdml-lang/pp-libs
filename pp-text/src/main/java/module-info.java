module dev.pp.text {

    requires dev.pp.basics;

    exports dev.pp.text.documentation;
    exports dev.pp.text.error;
    exports dev.pp.text.error.handler;
    exports dev.pp.text.location;
    exports dev.pp.text.reader;
    exports dev.pp.text.reader.stack;
    exports dev.pp.text.resource;
    exports dev.pp.text.token;
    exports dev.pp.text.utilities.html;
    exports dev.pp.text.utilities.json;
    exports dev.pp.text.utilities.file;
    exports dev.pp.text.utilities.text;
}