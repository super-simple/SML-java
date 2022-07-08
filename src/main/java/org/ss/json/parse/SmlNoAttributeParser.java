package org.ss.json.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ss.json.bo.MutableInt;
import org.ss.json.exceptionclz.SmlBug;
import org.ss.json.exceptionclz.SmlFormatException;
import org.ss.json.util.ObjectMappers;

import static org.ss.json.exceptionclz.SmlErrorMessage.ERROR_START;
import static org.ss.json.exceptionclz.SmlErrorMessage.ILLEGAL_CHARACTER_FORMAT;

public class SmlNoAttributeParser {

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
        ObjectNode headerObjectNode = parseHeader(smlStr, indexHolder, length, sb);
        return parseBody(smlStr, indexHolder, length, sb);
    }

    private static ObjectNode parseHeader(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        int index = indexHolder.getValue();
        char c0, c1;
        int count = 0;
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (!Character.isWhitespace(c0)) {
                if (Character.isHighSurrogate(c0)) {
                    c1 = smlStr.charAt(index++);
                    if (Character.isLowSurrogate(c1)) {
                        if (!Character.isWhitespace(Character.toCodePoint(c0, c1))) {
                            throw new SmlFormatException(ERROR_START);
                        }
                    } else {
                        throw new SmlFormatException(ILLEGAL_CHARACTER_FORMAT);
                    }
                } else {
                    sb.append(c0);
                }
            }
        }
        sb.delete(0, count);
        indexHolder.setValue(index);
        return objectNode;
    }

    private static void parseAttribute(String smlStr,, MutableInt indexHolder, int length, StringBuilder sb) {

    }

}
