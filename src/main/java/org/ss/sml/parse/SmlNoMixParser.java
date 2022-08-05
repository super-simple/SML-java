package org.ss.sml.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ss.sml.bo.MutableInt;
import org.ss.sml.bo.TokenTrait;
import org.ss.sml.exceptionclz.SmlBug;
import org.ss.sml.exceptionclz.SmlFormatException;
import org.ss.sml.util.ObjectMappers;

public class SmlNoMixParser {

    private static final char[] KEYWORD = new char[]{'(', ')', '[', ']', '{', '}', '=', '"', '\'', '`', '\\'};

    private static final ObjectMapper OBJECT_MAPPER = ObjectMappers.getObjectMapper();

    public static JsonNode parse(String smlStr) {
        try {
            return doParse(smlStr);
        } catch (SmlFormatException e) {
            throw e;
        } catch (Exception e) {
            throw new SmlBug(e);
        }
    }

    public static ObjectNode doParse(String smlStr) {
        if (smlStr == null || smlStr.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        MutableInt indexHolder = new MutableInt(0);
        int length = smlStr.length();
        ObjectNode headerAttribute = parseHeader(smlStr, indexHolder, length, sb);
        return parseBody(smlStr, indexHolder, length, sb);
    }

    private static ObjectNode parseHeader(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        return null;
    }

    private static char readContext(String smlStr, MutableInt indexHolder, int length, StringBuilder sb, TokenTrait tokenTrait) {
        int index = indexHolder.getValue();
        char c0 = 0;
        boolean hasSkipped = false;
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (!Character.isWhitespace(c0)) {
                for (char keyword : KEYWORD) {
                    if (c0 == keyword) {
                        return c0;
                    } else {
                        sb.append(c0);
                        if (!hasSkipped) {
                            hasSkipped = true;
                        }
                    }
                }
            } else {
                if (hasSkipped) {
                    sb.append(c0);
                    if (!tokenTrait.isContainWhitespace()) {
                        tokenTrait.setContainWhitespace(true);
                    }
                }
            }
        }
        return c0;
    }

    private static ObjectNode parseBody(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        return null;
    }

}
