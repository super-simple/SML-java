package org.ss.smljava;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.ss.smljava.antlr.SmlLexer;
import org.ss.smljava.antlr.SmlParser;

public class Sml {
    public static SmlParser getParser(String source) {
        CodePointCharStream charStream = CharStreams.fromString(source);
        SmlLexer lexer = new SmlLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        return new SmlParser(tokenStream);
    }
}
