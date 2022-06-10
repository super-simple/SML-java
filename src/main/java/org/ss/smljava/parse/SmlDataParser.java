package org.ss.smljava.parse;

import com.fasterxml.jackson.databind.JsonNode;
import org.ss.smljava.bo.MutableInt;
import org.ss.smljava.exceptionclz.SmlBug;
import org.ss.smljava.exceptionclz.SmlFormatException;

import static org.ss.smljava.exceptionclz.SmlErrorMessage.ERROR_START;
import static org.ss.smljava.parse.SmlDelimiter.*;

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
            if (c0 == LEFT_BRACE) {
                break;
            }
            if (c0 == LEFT_BRACKET) {
                break;
            }
            if (c0 == SLASH) {
                char c1 = smlStr.charAt(index++);
                if (c1 == SLASH) {
                    indexInt.setValue(index);
                    singleLineComment(smlStr, indexInt, length);
                    index = indexInt.getValue();
                } else if (c1 == ASTERISK) {
                    indexInt.setValue(index);
                    multiLineComment(smlStr, indexInt, length);
                    index = indexInt.getValue();
                } else {
                    throw new SmlFormatException(ERROR_START);
                }
            }
            if (!Character.isWhitespace(c0)) {
                if (Character.isHighSurrogate(c0)) {
                    char c1 = smlStr.charAt(index++);
                    if (Character.isLowSurrogate(c1)) {
                        if (!Character.isWhitespace(Character.toCodePoint(c0, c1))) {
                            throw new SmlFormatException(ERROR_START);
                        }
                    } else {
                        throw new SmlFormatException(ERROR_START);
                    }
                } else {
                    throw new SmlFormatException(ERROR_START);
                }
            }
        }
        return null;
    }

    private static JsonNode parseObject(String smlStr, MutableInt indexInt, int length, StringBuilder sb) {

    }

    private static JsonNode parseArray(String smlStr, MutableInt indexInt, int length, StringBuilder sb) {

    }

    private static void singleLineComment(String smlStr, MutableInt indexInt, int length) {
        int index = indexInt.getValue();
        while (index < length) {
            char c0 = smlStr.charAt(index++);
            if (c0 == LF) {
                break;
            }
            if (c0 == CR) {
                char c1 = smlStr.charAt(index++);
                if (c1 == LF) {
                    break;
                } else {
                    index--;
                    break;
                }
            }
        }
        indexInt.setValue(index);
    }

    private static void multiLineComment(String smlStr, MutableInt indexInt, int length) {
        int index = indexInt.getValue();
        while (index < length) {
            char c0 = smlStr.charAt(index++);
            if (c0 == ASTERISK) {
                char c1 = smlStr.charAt(index++);
                if (c1 == SLASH) {
                    break;
                }
            }
        }
        indexInt.setValue(index);
    }

}
