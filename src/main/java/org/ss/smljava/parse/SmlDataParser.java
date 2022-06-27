package org.ss.smljava.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ss.smljava.bo.MutableInt;
import org.ss.smljava.exceptionclz.SmlBug;
import org.ss.smljava.exceptionclz.SmlCharacterException;
import org.ss.smljava.exceptionclz.SmlFormatException;

import static org.ss.smljava.exceptionclz.SmlErrorMessage.ERROR_START;
import static org.ss.smljava.parse.SmlDelimiter.*;

public class SmlDataParser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
        MutableInt indexHolder = new MutableInt(0);
        int length = smlStr.length();
        return parseRoot(smlStr, indexHolder, length, sb);
    }

    private static JsonNode parseRoot(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        JsonNode jsonNode = null;
        int index = indexHolder.getValue();
        while (index < length) {
            char c0 = smlStr.charAt(index++);
            if (c0 == LEFT_BRACE) {
                jsonNode = parseObject(smlStr, indexHolder, length, sb);
                break;
            }
            if (c0 == LEFT_BRACKET) {
                break;
            }
            if (c0 == SLASH) {
                char c1 = smlStr.charAt(index++);
                if (c1 == SLASH) {
                    indexHolder.setValue(index);
                    index = singleLineComment(smlStr, index, length);
                } else if (c1 == ASTERISK) {
                    indexHolder.setValue(index);
                    index = multiLineComment(smlStr, index, length);
                } else {
                    throw new SmlFormatException(ERROR_START);
                }
            }
            index = validateHighWhitespace(smlStr, index, c0);
        }
        return jsonNode;
    }

    private static int validateHighWhitespace(String smlStr, int index, char c0) {
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
        return index;
    }

    private static JsonNode parseObject(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        int index = indexHolder.getValue();
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        int count = 0;
        while (index < length) {
            char c0 = smlStr.charAt(index++);
            if (Character.isWhitespace(c0)) {
                if (count > 0) {
                    break;
                } else {
                    continue;
                }
            }
            if (Character.isHighSurrogate(c0)) {
                char c1 = smlStr.charAt(index++);
                if (Character.isLowSurrogate(c1)) {
                    if (Character.isWhitespace(Character.toCodePoint(c0, c1))) {
                        continue;
                    } else {
                        sb.append(c0).append(c1);
                        count++;
                    }
                } else {
                    throw new SmlCharacterException("illegal character");
                }
            }
            if (c0 == SINGLE_QUOTE) {
                if (count > 0) {
                    throw new SmlFormatException("illegal format");
                }
            }
            if (c0 == DOUBLE_QUOTE) {
                if (count > 0) {
                    throw new SmlFormatException("illegal format");
                }
            }
        }
        return null;
    }

    private static JsonNode parseArray(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        return null;
    }

    private static int singleLineComment(String smlStr, int index, int length) {
        char c0;
        char c1;
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (c0 == LF) {
                break;
            }
            if (c0 == CR) {
                c1 = smlStr.charAt(index++);
                if (c1 == LF) {
                    break;
                } else {
                    index--;
                    break;
                }
            }
        }
        return index;
    }

    private static int multiLineComment(String smlStr, int index, int length) {
        char c0 = 0;
        char c1 = 0;
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (c0 == ASTERISK) {
                c1 = smlStr.charAt(index++);
                if (c1 == SLASH) {
                    break;
                }
            }
        }
        if (!(c0 == ASTERISK && c1 == SLASH)) {
            throw new SmlFormatException("multi line comment not close");
        }
        return index;
    }

    private static int singleQuote(String smlStr, int index, int length, StringBuilder sb) {
        char c0 = 0;
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (c0 != SINGLE_QUOTE) {
                sb.append(c0);
            } else {
                break;
            }
        }
        if (c0 != SINGLE_QUOTE) {
            throw new SmlFormatException("single quote not close");
        }
        return index;
    }

    private static int doubleQuote(String smlStr, int index, int length, StringBuilder sb) {
        char c0 = 0;
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (c0 != DOUBLE_QUOTE) {
                sb.append(c0);
            } else {
                break;
            }
        }
        if (c0 != DOUBLE_QUOTE) {
            throw new SmlFormatException("double quote not close");
        }
        return index;
    }

}
