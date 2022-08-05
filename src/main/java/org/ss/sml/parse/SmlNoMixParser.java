package org.ss.sml.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.ss.sml.bo.MutableInt;
import org.ss.sml.bo.TokenTrait;
import org.ss.sml.exceptionclz.*;
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

    public static JsonNode doParse(String smlStr) {
        if (smlStr == null || smlStr.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        MutableInt indexHolder = new MutableInt(0);
        int length = smlStr.length();
        TokenTrait tokenTrait = new TokenTrait();
        ObjectNode headerAttribute = parseHeader(smlStr, indexHolder, length, sb);
        return parseBody(smlStr, indexHolder, length, sb, tokenTrait);
    }

    private static ObjectNode parseHeader(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        char keyword = readContext(smlStr, indexHolder, length, sb);
        String sml = sb.toString();
        sb.delete(0, sb.length());
        int index = indexHolder.getValue();
        if (SmlDelimiter.SML.compareTo(sml) != 0) {
            throw new SmlErrorStartException(SmlErrorMessage.NOT_START_WITH_SML);
        }
        if (keyword != SmlDelimiter.LEFT_PARENTHESIS) {
            throw new SmlFormatException(SmlErrorMessage.EXCEPT_LEFT_PARENTHESIS, index, keyword);
        }
        if (index >= length) {
            throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_ATTRIBUTE_NAME);
        }
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        indexHolder.setValue(index);
        parseAttribute(smlStr, indexHolder, length, sb, objectNode);
        return objectNode;
    }

    private static void parseAttribute(String smlStr, MutableInt indexHolder, int length, StringBuilder sb, ObjectNode objectNode) {
        int index = indexHolder.getValue();
        char c0 = 0;
        while (index < length) {
            // 找到属性名字,关键字结尾
            char keyword = readContext(smlStr, indexHolder, length, sb);
            index = indexHolder.getValue();
            if (keyword == SmlDelimiter.RIGHT_PARENTHESIS) {
                break;
            }
            if (keyword != SmlDelimiter.EQUAL_SIGN) {
                throw new SmlFormatException(SmlErrorMessage.EXCEPT_EQUAL, index, keyword);
            }
            if (index >= length) {
                throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_ATTRIBUTE_VALUE);
            }
            //找到value
            while (index < length) {
                c0 = smlStr.charAt(index++);
                if (!Character.isWhitespace(c0)) {
                    if (!(c0 == SmlDelimiter.SINGLE_QUOTE || c0 == SmlDelimiter.DOUBLE_QUOTE)) {
                        throw new SmlFormatException(SmlErrorMessage.EXCEPT_QUOTE, index, c0);
                    }
                    break;
                }
            }
            String attributeName = sb.toString();
            sb.delete(0, sb.length());
            if (c0 == SmlDelimiter.SINGLE_QUOTE) {
                if (index >= length) {
                    throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_ATTRIBUTE_VALUE_CONTEXT);
                }
                while (index < length) {
                    c0 = smlStr.charAt(index++);
                    if (c0 != SmlDelimiter.SINGLE_QUOTE) {
                        sb.append(c0);
                    } else {
                        break;
                    }
                }
                continue;
            }
            if (c0 == SmlDelimiter.DOUBLE_QUOTE) {
                if (index >= length) {
                    throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_ATTRIBUTE_VALUE_CONTEXT);
                }
                while (index < length) {
                    c0 = smlStr.charAt(index++);
                    if (c0 != SmlDelimiter.DOUBLE_QUOTE) {
                        sb.append(c0);
                    } else {
                        break;
                    }
                }
            }
            String attributeValue = sb.toString();
            sb.delete(0, sb.length());
            objectNode.set(attributeName, new TextNode(attributeValue));
            indexHolder.setValue(index);
        }
        indexHolder.setValue(index);
    }

    /**
     * 读取内容,跳过两边的空格,用于确定是元素名字的情况下才使用,而且只会遇到关键字才会停止
     *
     * @param smlStr
     * @param indexHolder
     * @param length
     * @param sb
     * @return
     */
    private static char readContext(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        int index = indexHolder.getValue();
        char c0 = 0;
        int count = 0;
        boolean meetWhitespaceAgain = false;
        context:
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (!Character.isWhitespace(c0)) {
                for (char keyword : KEYWORD) {
                    if (c0 == keyword) {
                        break context;
                    }
                }
                if (!meetWhitespaceAgain) {
                    sb.append(c0);
                    count++;
                }
            } else {
                if (count > 0) {
                    if (!meetWhitespaceAgain) {
                        meetWhitespaceAgain = true;
                    }
                }
            }
        }
        indexHolder.setValue(index);
        keywordValidate(c0, index);
        return c0;
    }

    private static void keywordValidate(char c0, int index) {
        boolean isKeyword = false;
        for (char c : KEYWORD) {
            if (c0 == c) {
                isKeyword = true;
                break;
            }
        }
        if (!isKeyword) {
            throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_KEYWORD);
        }
    }

    /**
     * 贪婪读取内容,跳过两边的空格,但是中间的空格不会放弃,同样也是遇到关键字才会停止
     *
     * @param smlStr
     * @param indexHolder
     * @param length
     * @param sb
     * @param tokenTrait
     * @return
     */
    private static char readContextGreedy(String smlStr, MutableInt indexHolder, int length, StringBuilder sb, TokenTrait tokenTrait) {
        int index = indexHolder.getValue();
        char c0;
        int wsCount = 0;
        int notWsCount = 0;
        int transformCount = 0;
        //减少循环里面的判断
        c0 = smlStr.charAt(index);
        boolean firstCharIsWhitespace = Character.isWhitespace(c0);
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (!Character.isWhitespace(c0)) {
                for (char keyword : KEYWORD) {
                    if (c0 == keyword) {
                        return c0;
                    }
                }
                if (notWsCount != 0) {
                    notWsCount = 0;
                    transformCount++;
                }
                sb.append(c0);
                wsCount++;
            } else {
                if (wsCount != 0) {
                    wsCount = 0;
                    transformCount++;
                }
                notWsCount++;
                if (transformCount > 0) {
                    sb.append(c0);
                }
            }
        }
        if (notWsCount > 0) {
            int sbLength = sb.length();
            sb.delete(sbLength - notWsCount, sbLength);
        }
        indexHolder.setValue(index);
        keywordValidate(c0, index);
        if (firstCharIsWhitespace) {
            if (transformCount >= 3) {
                tokenTrait.setContainWhitespace(true);
            }
        } else {
            if (transformCount >= 2) {
                tokenTrait.setContainWhitespace(true);
            }
        }
        return c0;
    }

    private static JsonNode parseBody(String smlStr, MutableInt indexHolder, int length, StringBuilder sb, TokenTrait tokenTrait) {
        char c = readContext(smlStr, indexHolder, length, sb);
        String rootName = sb.toString();
        sb.delete(0, sb.length());
        ObjectNode rootNode = OBJECT_MAPPER.createObjectNode();
        JsonNode valueNode = null;
        if (c == SmlDelimiter.LEFT_PARENTHESIS) {
            valueNode = OBJECT_MAPPER.createObjectNode();
            parseAttribute(smlStr, indexHolder, length, sb, (ObjectNode) valueNode);
        }
        int index = indexHolder.getValue();
        if (index >= length) {
            throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_LEFT_BRACE);
        }
        char c0 = 0;
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (!Character.isWhitespace(c0)) {
                if (!(c0 == SmlDelimiter.LEFT_BRACE || c0 == SmlDelimiter.LEFT_BRACKET)) {
                    throw new SmlFormatException(SmlErrorMessage.EXCEPT_LEFT_BRACE_OR_BRACKET, index, c0);
                }
                break;
            }
        }
        indexHolder.setValue(index);
        if (c0 == SmlDelimiter.LEFT_BRACE) {
            parseElement(smlStr, indexHolder, length, sb, (ObjectNode) valueNode, tokenTrait);
        }
        if (c0 == SmlDelimiter.LEFT_BRACKET) {
            valueNode = parseArray(smlStr, indexHolder, length, sb);
        }
        rootNode.set(rootName, valueNode);
        return rootNode;
    }

    private static void parseElement(String smlStr, MutableInt indexHolder, int length, StringBuilder sb, ObjectNode valueNode, TokenTrait tokenTrait) {
        char keyword = readContextGreedy(smlStr, indexHolder, length, sb, tokenTrait);

    }

    private static JsonNode parseArray(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        ArrayNode arrayNode = OBJECT_MAPPER.createArrayNode();
        return arrayNode;
    }

}
