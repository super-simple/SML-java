package org.ss.smljava.parse;

import org.ss.smljava.bo.MutableInt;
import org.ss.smljava.exceptionclz.SmlParseException;
import org.ss.smljava.model.SmlAttribute;
import org.ss.smljava.model.SmlAttributePair;
import org.ss.smljava.model.SmlDocument;
import org.ss.smljava.model.SmlNode;

import java.util.List;

import static org.ss.smljava.parse.SmlDelimiter.*;

public class SmlParser {

    public SmlDocument parseDocument(String documentStr) {
        if (documentStr == null) {
            return null;
        }
        SmlDocument smlDocument = new SmlDocument();
        int size = documentStr.length();
        if (size == 0) {
            return smlDocument;
        }
        StringBuilder sb = new StringBuilder();
        MutableInt indexInt = new MutableInt(0);
        parseHeader(documentStr, size, indexInt, sb, smlDocument);
        return smlDocument;
    }

    public void parseHeader(String documentStr, int size, MutableInt indexInt, StringBuilder sb, SmlDocument smlDocument) {
        String sml = exceptNodeName(documentStr, size, indexInt, sb);
        if (sml.compareTo("sml") != 0) {
            throw new SmlParseException("header name is not `sml`");
        }
        SmlNode smlNode = new SmlNode();
        SmlAttribute smlAttribute = exceptAttribute(documentStr, size, indexInt, sb);
        if (smlAttribute != null) {
            smlNode.setAttribute(smlAttribute);
        }
        smlDocument.setSml(smlNode);
    }

    private String exceptNodeName(String documentStr, int size, MutableInt indexInt, StringBuilder sb) {
        int index = indexInt.getValue();
        int count = 0;
        for (; index < size; index++) {
            char c = documentStr.charAt(index);
            if (c == LEFT_PARENTHESIS || c == LEFT_BRACE) {
                break;
            }
            if (Character.isWhitespace(c) && count != 0) {
                break;
            }
            count++;
            sb.append(c);
        }
        if (count == 0) {
            throw new SmlParseException("illegal context");
        }
        String str = sb.toString();
        sb.delete(0, count);
        indexInt.setValue(index);
        return str;
    }

    private SmlAttribute exceptAttribute(String documentStr, int size, MutableInt indexInt, StringBuilder sb) {
        int index = indexInt.getValue();
        //find (
        for (; index < size; index++) {
            char c = documentStr.charAt(index);
            if (c == LEFT_PARENTHESIS) {
                break;
            }
            if (c == LEFT_BRACE) {
                return null;
            }
        }
        // find attribute name and find value
        List<SmlAttributePair> pairList = null;
        breakAttribute:
        while (true) {
            int count = 0;
            //find attribute name
            for (; index < size; index++) {
                char c = documentStr.charAt(index);
                if (c == RIGHT_PARENTHESIS) {
                    break breakAttribute;
                }
                if (Character.isWhitespace(c)) {
                    if (count != 0) {
                        break;
                    }
                } else {
                    count++;
                    sb.append(c);
                }
            }
            String attributeName = sb.toString();
            // find equal
            for (; index < size; index++) {
                char c = documentStr.charAt(index);
                if (c == RIGHT_PARENTHESIS) {
                    break breakAttribute;
                }
                if (c == EQUAL_SIGN) {
                    break;
                }
            }
            // find attribute value
            for (; index < size; index++) {
                char c = documentStr.charAt(index);
                if (c == RIGHT_PARENTHESIS) {
                    break breakAttribute;
                }
                if (c == DOUBLE_QUOTE) {
                    break;
                }
            }
        }

        return null;
    }

}
