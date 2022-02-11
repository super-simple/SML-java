package org.ss.smljava.test.basic;

import org.junit.jupiter.api.Test;
import org.ss.smljava.Sml;
import org.ss.smljava.antlr.SmlParser;
import org.ss.smljava.util.IOs;

import java.io.IOException;
import java.io.InputStream;

public class SimpleTest1 {
    @Test
    void test1() throws IOException {
        InputStream inputStream = null;
        SmlParser parser;
        try {
            inputStream = SimpleTest1.class.getResourceAsStream("/basic/sample.sml");
            String source = IOs.toString(inputStream);
            parser = Sml.getParser(source);
        } finally {
            inputStream.close();
        }
        SmlParser.BodyContext body = parser.body();
        System.out.println(body);
    }
}
