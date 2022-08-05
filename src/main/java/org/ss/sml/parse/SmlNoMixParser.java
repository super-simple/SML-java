package org.ss.sml.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ss.sml.bo.MutableInt;
import org.ss.sml.bo.TokenTrait;
import org.ss.sml.exceptionclz.SmlBug;
import org.ss.sml.exceptionclz.SmlErrorMessage;
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
        TokenTrait tokenTrait = new TokenTrait();
        ObjectNode headerAttribute = parseHeader(smlStr, indexHolder, length, sb, tokenTrait);
        return parseBody(smlStr, indexHolder, length, sb);
    }

    private static ObjectNode parseHeader(String smlStr, MutableInt indexHolder, int length, StringBuilder sb, TokenTrait tokenTrait) {
        return null;
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
        while (index < length) {
            c0 = smlStr.charAt(index++);
            if (!Character.isWhitespace(c0)) {
                for (char keyword : KEYWORD) {
                    if (c0 == keyword) {
                        return c0;
                    }
                }
                if (!meetWhitespaceAgain) {
                    sb.append(c0);
                    count++;
                } else {
                    throw new SmlFormatException(SmlErrorMessage.EXCEPT_LEFT);
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
        return c0;
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
            throw new SmlFormatException(SmlErrorMessage.EXCEPT_LEFT);
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
        keywordValidate(c0);
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

    private static ObjectNode parseBody(String smlStr, MutableInt indexHolder, int length, StringBuilder sb) {
        return null;
    }

    private void restTokenTrait(TokenTrait tokenTrait) {
        tokenTrait.setContainWhitespace(false);
    }

}
