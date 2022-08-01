package dev.pp.basics.utilities.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileNameUtilsTest {

    @Test
    void getExtension() {

        assertEquals ( "txt", FileNameUtils.getExtension ( "foo.txt" ) );
        assertNull ( FileNameUtils.getExtension ( "foo" ) );
        assertEquals ( "txt", FileNameUtils.getExtension ( "foo.bar.txt" ) );
    }

    @Test
    void changeExtension() {

        assertEquals ( "foo.html", FileNameUtils.changeExtension ( "foo.txt", "html" ) );
        assertEquals ( "foo.html", FileNameUtils.changeExtension ( "foo", "html" ) );
        assertEquals ( "foo.bar.html", FileNameUtils.changeExtension ( "foo.bar.txt", "html" ) );
    }
}