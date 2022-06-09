package org.ss.smljava.parse;

import com.fasterxml.jackson.databind.JsonNode;
import org.ss.smljava.bo.MutableInt;
import org.ss.smljava.exceptionclz.SmlBug;
import org.ss.smljava.exceptionclz.SmlFormatException;

public class SmlDataParser {

    public static JsonNode parse(String smlStr) {
        try {
            return doParse(smlStr);
        } catch (SmlFormatException e) {
            throw e;
        } catch (Exception e) {
            throw new SmlBug(e);
        }
    }

    public static JsonNode doParse(String smlStr) {
        if (smlStr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        MutableInt indexInt = new MutableInt(0);
        int length = smlStr.length();
        return parseRoot(smlStr, indexInt, length, sb);
    }

    private static JsonNode parseRoot(String smlStr, MutableInt indexInt, int length, StringBuilder sb) {
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

                } else {
                    throw new SmlFormatException("error start");
                }
            }
            if (!Character.isWhitespace(c0)) {
                if (Character.isHighSurrogate(c0)) {
                    char c1 = smlStr.charAt(index++);
                    if (Character.isLowSurrogate(c1)) {
                        if (!Character.isWhitespace(Character.toCodePoint(c0, c1))) {
                            throw new SmlFormatException("error start");
                        }
                    } else {

                    }
                } else {
                    throw new SmlFormatException("error start");
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
