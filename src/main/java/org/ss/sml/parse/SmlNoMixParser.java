package org.ss.sml.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ss.sml.bo.MutableInt;
import org.ss.sml.exceptionclz.SmlBug;
import org.ss.sml.exceptionclz.SmlErrorMessage;
import org.ss.sml.exceptionclz.SmlFormatException;
import org.ss.sml.util.ObjectMappers;

import static org.ss.sml.exceptionclz.SmlErrorMessage.*;
import static org.ss.sml.parse.SmlDelimiter.*;

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
        int model = exceptElementName(smlStr, indexHolder, length, sb);
        ObjectNode rootNode = OBJECT_MAPPER.createObjectNode();
        String elementName = sb.toString();
        sb.delete(0, sb.length());
        if (model < 3) {
            ObjectNode firstNode = OBJECT_MAPPER.createObjectNode();
            rootNode.set(elementName, firstNode);
            if (model == 1) {
                exceptAttribute(smlStr, indexHolder, length, sb, firstNode);
            }
            exceptElementValue(smlStr, indexHolder, length, sb, firstNode);
        } else {
            ArrayNode firstNode = OBJECT_MAPPER.createArrayNode();
            rootNode.set(elementName, firstNode);
            exceptArrayElementValue(smlStr, indexHolder, length, sb, firstNode);
        }
        return rootNode;
    }

    private static void exceptArrayElementValue(String smlStr, MutableInt indexHolder, int length, StringBuilder sb, ArrayNode firstNode) {

    }

    private static void exceptElementValue(String smlStr, MutableInt indexHolder, int length, StringBuilder sb, ObjectNode firstNode) {
    }

    private static int exceptElementName(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        int index = indexHolder.getValue();
        char c0;
        int model = 0;
        while (index < length) {
            c0 = smlStr.charAt(index);
            if (c0 == LEFT_PARENTHESIS) {
                model = 1;
                break;
            }
            if (c0 == LEFT_BRACE) {
                model = 2;
                break;
            }
            if (c0 == LEFT_BRACKET) {
                model = 3;
                break;
            }
            if (!Character.isWhitespace(c0)) {
                sb.append(c0);
            }
            index++;
        }

        if (model == 0) {
            while (index < length) {
                c0 = smlStr.charAt(index);
                if (c0 == LEFT_PARENTHESIS) {
                    model = 1;
                    break;
                }
                if (c0 == LEFT_BRACE) {
                    model = 2;
                    break;
                }
                if (c0 == LEFT_BRACKET) {
                    model = 3;
                    break;
                }
                if (!Character.isWhitespace(c0)) {
                    throw new SmlFormatException(EXCEPT_LEFT);
                }
                index++;
            }
        }
        indexHolder.setValue(index);
        return model;
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
            } else {
                hasEqualSign = false;
            }
            // find attribute value
            valueEnd:
            while (index < length) {
                c0 = smlStr.charAt(index++);
                if (c0 == SINGLE_QUOTE) {
                    while (index < length) {
                        c0 = smlStr.charAt(index++);
                        if (c0 != SINGLE_QUOTE) {
                            sb.append(c0);
                            count++;
                        } else {
                            break valueEnd;
                        }
                    }
                }
                if (c0 == DOUBLE_QUOTE) {
                    while (index < length) {
                        c0 = smlStr.charAt(index++);
                        if (c0 != DOUBLE_QUOTE) {
                            sb.append(c0);
                            count++;
                        } else {
                            break valueEnd;
                        }
                    }
                }
                if (!Character.isWhitespace(c0)) {
                    throw new SmlFormatException(EXCEPT_QUOTE);
                }
            }
            String attributeValue = sb.toString();
            sb.delete(0, count);
            count = 0;
            objectNode.put(attributeName, attributeValue);
        }
    }

}
