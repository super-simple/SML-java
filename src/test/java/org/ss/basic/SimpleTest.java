package org.ss.basic;

import org.junit.jupiter.api.Test;
import org.ss.smljava.model.SmlDocument;
import org.ss.smljava.parse.SmlParser;
import org.ss.smljava.util.IOs;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SimpleTest {

    @Test
    void simple1() {
        InputStream resourceAsStream = SimpleTest.class.getResourceAsStream("/basic/simple.sml");
        String documentStr = IOs.toString(resourceAsStream, StandardCharsets.UTF_8);
        SmlParser smlParser = new SmlParser();
        SmlDocument smlDocument = smlParser.parseDocument(documentStr);
        System.out.println(smlDocument);
    }

}
