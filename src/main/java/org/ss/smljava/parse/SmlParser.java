package org.ss.smljava.parse;

import org.ss.smljava.bo.MutableInt;
import org.ss.smljava.exceptionclz.SmlParseException;
import org.ss.smljava.model.SmlDocument;

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

    }

    private String exceptNodeName(String documentStr, int size, MutableInt indexInt, StringBuilder sb) {
        int index = indexInt.getValue();
        int i;
        for (i = 0; i < size; i++) {
            char c = documentStr.charAt(index);
            if (Character.isWhitespace(c) && sb.length() != 0) {
                break;
            }
            sb.append(c);
        }
        String str = sb.toString();
        sb.delete(0, sb.length());
        index = index + (i - 1);
        indexInt.setValue(index);
        return str;
    }

    private String exceptAttribute(String documentStr, int size, MutableInt indexInt, StringBuilder sb) {
        int value = indexInt.getValue();
        for (int i = value; i < size; i++) {

        }
        return null;
    }
}
