package org.ss.sml.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.ss.sml.bo.MutableInt;
import org.ss.sml.exceptionclz.SmlBug;
import org.ss.sml.exceptionclz.SmlErrorEndException;
import org.ss.sml.exceptionclz.SmlErrorMessage;
import org.ss.sml.exceptionclz.SmlFormatException;
import org.ss.sml.util.ObjectMappers;

public class SmlDataParser {

    private static final char[] KEYWORD = new char[]{'(', ')', '[', ']', '{', '}', '=', '"', '\'', '`', '\\'};

    private static final char[] OTHER_KEYWORD = new char[]{'(', ')', '[', ']', '{', '}', '=', '\\'};

    private static final char[] STRING_KEYWORD = new char[]{'"', '\'', '`'};

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
        return parseRoot(smlStr, indexHolder, length, sb);
    }

    private static void keywordValidate(char c0) {
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

    private static char readKeyword(String smlStr, MutableInt indexHolder, int length) {
        int index = indexHolder.getValue();
        char c0 = 0;
        topLoop:
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (!Character.isWhitespace(c0)) {
                for (char keyword : KEYWORD) {
                    if (c0 == keyword) {
                        break topLoop;
                    }
                }
                break;
            }
        }
        keywordValidate(c0);
        indexHolder.setValue(index);
        return c0;
    }

    /**
     * 排除两边的空格,遇到关键字停止
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
                } else {
                    break;
                }
            } else {
                if (count > 0) {
                    if (!meetWhitespaceAgain) {
                        meetWhitespaceAgain = true;
                    }
                }
            }
        }
        keywordValidate(c0);
        indexHolder.setValue(index);
        return c0;
    }


    private static JsonNode parseRoot(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        char keyword = readKeyword(smlStr, indexHolder, length);
        JsonNode rootNode;
        int index = indexHolder.getValue();
        if (index >= length) {
            throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_CONTEXT);
        }
        if (keyword == SmlDelimiter.LEFT_BRACE) {
            rootNode = parseObject(smlStr, indexHolder, length, sb);
        } else if (keyword == SmlDelimiter.LEFT_BRACKET) {
            rootNode = parseArray(smlStr, indexHolder, length, sb);
        } else {
            throw new SmlFormatException(SmlErrorMessage.EXCEPT_LEFT_BRACE_OR_BRACKET, indexHolder.getValue(), keyword);
        }
        return rootNode;
    }

    private static JsonNode parseAttribute(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        char keyword = readContext(smlStr, indexHolder, length, sb);
        int index = indexHolder.getValue();
        JsonNode result;
        switch (keyword) {
            case SmlDelimiter.RIGHT_PARENTHESIS: {
                if (index >= length) {
                    throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_LEFT_BRACE);
                }
                int contextLength = sb.length();
                if (contextLength == 0) {
                    result = NullNode.getInstance();
                } else {
                    String context = sb.toString();
                    sb.delete(0, contextLength);
                    switch (context) {
                        case SmlKeyword.nullString: {
                            result = NullNode.getInstance();
                            break;
                        }
                        case SmlKeyword.trueString: {
                            result = BooleanNode.TRUE;
                            break;
                        }
                        case SmlKeyword.falseString: {
                            result = BooleanNode.FALSE;
                            break;
                        }
                        default: {
                            //todo 完善具体的逻辑
                            if (context.indexOf(SmlDelimiter.DOT) >= 0) {
                                result = DoubleNode.valueOf(Double.parseDouble(context));
                            } else {
                                result = IntNode.valueOf(Integer.parseInt(context));
                            }
                        }
                    }

                }
                break;
            }
            case SmlDelimiter.SINGLE_QUOTE: {
            }
            case SmlDelimiter.DOUBLE_QUOTE: {
                if (index >= length) {
                    throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_ATTRIBUTE_VALUE);
                }
                consumeString(smlStr, indexHolder, length, sb, keyword);
                index = indexHolder.getValue();
                if (index >= length) {
                    throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_RIGHT_PARENTHESIS);
                }
                keyword = readKeyword(smlStr, indexHolder, length);
                index = indexHolder.getValue();
                if (keyword == SmlDelimiter.RIGHT_PARENTHESIS) {
                    String context = sb.toString();
                    sb.delete(0, sb.length());
                    result = new TextNode(context);
                } else {
                    throw new SmlFormatException(SmlErrorMessage.EXCEPT_RIGHT_PARENTHESIS, indexHolder.getValue(), keyword);
                }
                if (index >= length) {
                    throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_RIGHT_BRACE);
                }
                break;
            }
            //todo 完善支持其它string类型
            default: {
                throw new SmlFormatException(SmlErrorMessage.EXCEPT_CONTEXT, indexHolder.getValue(), keyword);
            }
        }
        return result;
    }

    private static void consumeString(String smlStr, MutableInt indexHolder, int length, StringBuilder sb, char untilChar) {
        int index = indexHolder.getValue();
        char c0;
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (c0 != untilChar) {
                sb.append(c0);
            } else {
                break;
            }
        }
        indexHolder.setValue(index);
    }

    private static ObjectNode parseObject(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        int index = indexHolder.getValue();
        ObjectNode result = OBJECT_MAPPER.createObjectNode();
        while (index < length) {
            char keyword = readContext(smlStr, indexHolder, length, sb);
            int nameLength = sb.length();
            if (keyword == SmlDelimiter.RIGHT_BRACE) {
                if (nameLength != 0) {
                    throw new SmlFormatException(SmlErrorMessage.NO_MIXED, indexHolder.getValue(), keyword);
                }
                break;
            }
            index = indexHolder.getValue();
            if (index >= length) {
                throw new SmlErrorEndException(SmlErrorMessage.EXCEPT_LEFT);
            }
            if (nameLength == 0) {
                throw new SmlFormatException(SmlErrorMessage.EXCEPT_ATTRIBUTE_NAME, indexHolder.getValue(), keyword);
            }
            String name = sb.toString();
            sb.delete(0, nameLength);
            if (keyword == SmlDelimiter.LEFT_PARENTHESIS) {
                result.set(name, parseAttribute(smlStr, indexHolder, length, sb));
            } else if (keyword == SmlDelimiter.LEFT_BRACE) {
                result.set(name, parseObject(smlStr, indexHolder, length, sb));
            } else if (keyword == SmlDelimiter.LEFT_BRACKET) {
                result.set(name, parseArray(smlStr, indexHolder, length, sb));
            } else {
                throw new SmlFormatException(SmlErrorMessage.EXCEPT_LEFT, indexHolder.getValue(), keyword);
            }
            index = indexHolder.getValue();
            if (index >= length) {
                throw new SmlFormatException(SmlErrorMessage.EXCEPT_RIGHT_BRACE, indexHolder.getValue(), keyword);
            }
        }
        return result;
    }

    private static ArrayNode parseArray(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        return null;
    }

}
