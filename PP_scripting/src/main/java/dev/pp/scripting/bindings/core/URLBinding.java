package dev.pp.scripting.bindings.core;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.URLUtilities;
import dev.pp.scripting.bindings.ScriptingBinding;

import java.io.IOException;

public class URLBinding implements ScriptingBinding {

    public URLBinding () {}


    public @NotNull String bindingName () { return "URLUtils"; }

    public @Nullable String readText ( @NotNull String URL ) throws IOException {

        return URLUtilities.readUTF8Text ( URL );
    }
}
