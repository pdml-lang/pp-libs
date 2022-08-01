module dev.pp.scripting {

    requires java.desktop;

    requires jdk.javadoc;

    requires org.graalvm.sdk;

    requires dev.pp.basics;

    exports dev.pp.scripting;
    exports dev.pp.scripting.env;
    exports dev.pp.scripting.bindings;
    exports dev.pp.scripting.docletParser;
    exports dev.pp.scripting.bindings.core;
    exports dev.pp.scripting.bindings.builder;
}