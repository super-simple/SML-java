package org.ss.smljava.parse;

import org.ss.smljava.antlrgenerate.SmlBaseListener;
import org.ss.smljava.antlrgenerate.SmlParser;

public class SmlListener extends SmlBaseListener {
    @Override
    public void enterNode(SmlParser.NodeContext ctx) {
        System.out.println(ctx.getChildCount());
    }
}
