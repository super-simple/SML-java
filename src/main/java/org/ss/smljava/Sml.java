package org.ss.smljava;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.ss.smljava.antlrgenerate.SmlLexer;
import org.ss.smljava.antlrgenerate.SmlParser;
import org.ss.smljava.parse.SmlListener;

public class Sml {
    public static SmlLexer getLexer(String source) {
        CodePointCharStream charStream = CharStreams.fromString(source);
        SmlLexer lexer = new SmlLexer(charStream);
        return lexer;
    }

    public static SmlParser getParser(String source) {
        CodePointCharStream charStream = CharStreams.fromString(source);
        SmlLexer lexer = new SmlLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SmlParser smlParser = new SmlParser(tokenStream);
        smlParser.addParseListener(new SmlListener());
        smlParser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
        return smlParser;
    }
}
