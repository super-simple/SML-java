package org.ss.smljava.parse;

import org.ss.smljava.bo.MutableInt;
import org.ss.smljava.exceptionclz.SmlParseException;
import org.ss.smljava.model.*;

import java.util.ArrayList;
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
        parseBody(documentStr, size, indexInt, sb, smlDocument);
        return smlDocument;
    }

    public void parseHeader(String documentStr, int size, MutableInt indexInt, StringBuilder sb, SmlDocument smlDocument) {
        String sml = exceptNodeName(documentStr, size, indexInt, sb);
        if (sml.compareTo("sml") != 0) {
            throw new SmlParseException("header name is not `sml`");
        }
        SmlNode smlNode = new SmlNode();
        List<SmlAttributePair> attributePairList = exceptAttribute(documentStr, size, indexInt, sb);
        if (attributePairList != null) {
            SmlAttribute attribute = new SmlAttribute();
            attribute.setPairList(attributePairList);
            smlNode.setAttribute(attribute);
        }
        smlNode.setName(sml);
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

    private List<SmlAttributePair> exceptAttribute(String documentStr, int size, MutableInt indexInt, StringBuilder sb) {
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
                    sb.append(c);
                    count++;
                }
            }
            String pairName = sb.toString();
            sb.delete(0, count);
            count = 0;
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
            // find attribute value "
            for (; index < size; index++) {
                char c = documentStr.charAt(index);
                if (c == RIGHT_PARENTHESIS) {
                    break breakAttribute;
                }
                if (c == DOUBLE_QUOTE) {
                    break;
                }
            }
            // find attribute value context
            for (; index < size; index++) {
                char c = documentStr.charAt(index);
                if (c == DOUBLE_QUOTE) {
                    break;
                }
                sb.append(c);
                count++;
            }
            String pairValue = sb.toString();
            SmlAttributePair smlAttributePair = new SmlAttributePair();
            smlAttributePair.setName(pairName);
            smlAttributePair.setValue(pairValue);
            if (pairList == null) {
                pairList = new ArrayList<>();
            }
            pairList.add(smlAttributePair);
        }
        indexInt.setValue(index);
        return pairList;
    }

    private void parseBody(String documentStr, int size, MutableInt indexInt, StringBuilder sb, SmlDocument smlDocument) {
        String nodeName = exceptNodeName(documentStr, size, indexInt, sb);
        List<SmlAttributePair> attributePairList = exceptAttribute(documentStr, size, indexInt, sb);
        int index = indexInt.getValue();
        for (; index < size; index++) {
            char c = documentStr.charAt(index);
            if (c == LEFT_BRACE) {
                break;
            }
            if (Character.isWhitespace(c)) {
                continue;
            }
        }
        SmlNode bodyRootNode = new SmlNode();
        if (attributePairList!=null){

        }
    }

    private SmlElement parseNode(String documentStr, int size, MutableInt indexInt, StringBuilder sb, SmlDocument smlDocument) {
        int index = indexInt.getValue();
        int count = 0;
        return null;
    }

}
