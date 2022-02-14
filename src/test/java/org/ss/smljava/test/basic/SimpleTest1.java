package org.ss.smljava.test.basic;

import org.junit.jupiter.api.Test;
import org.ss.smljava.Sml;
import org.ss.smljava.antlrgenerate.SmlLexer;
import org.ss.smljava.antlrgenerate.SmlParser;
import org.ss.smljava.util.IOs;

import java.io.IOException;
import java.io.InputStream;

public class SimpleTest1 {
    @Test
    void testLexer() throws IOException {
        InputStream inputStream = null;
        SmlLexer lexer;
        try {
            inputStream = SimpleTest1.class.getResourceAsStream("/basic/sample.sml");
            String source = IOs.toString(inputStream);
            lexer = Sml.getLexer(source);
        } finally {
            inputStream.close();
        }
        System.out.println(lexer.getAllTokens());
    }

    @Test
    void testParser() throws IOException {
        InputStream inputStream = null;
        SmlParser parser;
        try {
            inputStream = SimpleTest1.class.getResourceAsStream("/basic/sample.sml");
            String source = IOs.toString(inputStream);
            parser = Sml.getParser(source);
        } finally {
            inputStream.close();
        }
        SmlParser.ContentContext content = parser.content();
        System.out.println(content.getText());
    }
}
