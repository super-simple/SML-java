package org.ss.json.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ss.json.bo.MutableInt;
import org.ss.json.exceptionclz.SmlBug;
import org.ss.json.exceptionclz.SmlErrorMessage;
import org.ss.json.exceptionclz.SmlFormatException;
import org.ss.json.util.ObjectMappers;

import static org.ss.json.exceptionclz.SmlErrorMessage.*;
import static org.ss.json.parse.SmlDelimiter.*;

public class SmlNoMixParser {
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
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        int index = indexHolder.getValue();
        char c0;
        int count = 0;
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (!Character.isWhitespace(c0)) {
                sb.append(c0);
                count++;
            } else {
                if (count != 0) {
                    break;
                }
            }
        }
        String sml = sb.toString();
        if (!SmlDelimiter.SML.equals(sml)) {
            throw new SmlFormatException(SmlErrorMessage.NOT_START_WITH_SML);
        }
        sb.delete(0, count);
        indexHolder.setValue(index);
        exceptAttribute(smlStr, indexHolder, length, sb, objectNode);
        return objectNode;
    }

    private static ObjectNode parseBody(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        return null;
    }

    private static void exceptAttribute(String smlStr, MutableInt indexHolder, int length, StringBuilder sb, ObjectNode objectNode) {
        int index = indexHolder.getValue();
        char c0 = 0;
        int count = 0;
        boolean hasEqualSign = false;
        // find (
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (c0 == LEFT_PARENTHESIS) {
                break;
            }
            if (!Character.isWhitespace(c0)) {
                throw new SmlFormatException(EXCEPT_LEFT_PARENTHESIS);
            }
        }
        if (c0 != LEFT_PARENTHESIS) {
            throw new SmlFormatException(EXCEPT_LEFT_PARENTHESIS);
        }
        // loop find attribute
        attributeEnd:
        while (index < length) {
            //find attribute name
            hasEqualSign = false;
            while (index < length) {
                c0 = smlStr.charAt(index++);
                if (c0 == RIGHT_PARENTHESIS) {
                    if (count != 0) {
                        throw new SmlFormatException(ERROR_ATTRIBUTE);
                    }
                    break attributeEnd;
                }
                if (c0 == EQUAL_SIGN) {
                    if (count == 0) {
                        throw new SmlFormatException(EXCEPT_ATTRIBUTE_NAME);
                    }
                    hasEqualSign = true;
                    break;
                }
                if (!Character.isWhitespace(c0)) {
                    sb.append(c0);
                    count++;
                } else {
                    if (count != 0) {
                        break;
                    }
                }
            }
            String attributeName = sb.toString();
            sb.delete(0, count);
            count = 0;
            if (!hasEqualSign) {
                //find equal
                while (index < length) {
                    c0 = smlStr.charAt(index++);
                    if (c0 == EQUAL_SIGN) {
                        break;
                    }
                }
                if (c0 != EQUAL_SIGN) {
                    throw new SmlFormatException(EXCEPT_EQUAL);
                }
            }
            // find attribute value

        }
    }

}
