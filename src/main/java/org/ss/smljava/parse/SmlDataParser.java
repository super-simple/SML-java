package org.ss.smljava.parse;

import com.fasterxml.jackson.databind.JsonNode;
import org.ss.smljava.bo.MutableInt;

public class SmlDataParser {


    public static JsonNode parse(String smlStr) {
        if (smlStr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        MutableInt indexInt = new MutableInt(0);
        int length = smlStr.length();
        return parseSml(smlStr, indexInt, length, sb);
    }

    private static JsonNode parseSml(String smlStr, MutableInt indexInt, int length, StringBuilder sb) {
        int index = indexInt.getValue();
        while (index < length) {
            char c0 = smlStr.charAt(index++);
            if (c0 == SmlDelimiter.LEFT_BRACE) {
                break;
            }
            if (c0 == SmlDelimiter.LEFT_BRACKET) {
                break;
            }
            if (c0 == SmlDelimiter.SLASH) {
                char c1 = smlStr.charAt(index++);
                if (c1 == SmlDelimiter.SLASH) {

                } else if (c1 == SmlDelimiter.asterisk) {

                }
            }
        }
    }

    private static JsonNode parseObject(String smlStr, MutableInt indexInt, int length, StringBuilder sb) {

    }

    private static JsonNode parseArray(String smlStr, MutableInt indexInt, int length, StringBuilder sb) {

    }

    private static void singleLineComment(String smlStr, MutableInt indexInt, int length) {

    }

    private static void multiLineComment(String smlStr, MutableInt indexInt, int length) {

    }
}
