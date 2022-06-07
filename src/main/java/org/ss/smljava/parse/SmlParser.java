package org.ss.smljava.parse;

import org.ss.smljava.bo.MutableInt;
import org.ss.smljava.exceptionclz.SmlParseException;
import org.ss.smljava.smlmodel.*;

import java.util.ArrayList;
import java.util.Collections;
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

    private void parseHeader(String documentStr, int size, MutableInt indexInt, StringBuilder sb, SmlDocument smlDocument) {
        String sml = exceptNodeName(documentStr, size, indexInt, sb);
        if (sml.compareTo("sml") != 0) {
            throw new SmlParseException("header name is not `sml`");
        }
        SmlHeader smlHeader = new SmlHeader();
        int index = indexInt.getValue();
        boolean haveAttribute = false;
        while (index < size) {
            char c = documentStr.charAt(index++);
            if (c == LEFT_PARENTHESIS) {
                haveAttribute = true;
                break;
            }
        }
        indexInt.setValue(index);
        if (haveAttribute) {
            List<SmlAttributePair> attributePairList = exceptAttribute(documentStr, size, indexInt, sb);
            if (attributePairList != null) {
                smlHeader.setPairList(attributePairList);
            }
        }
        smlHeader.setName(sml);
        smlDocument.setSml(smlHeader);
    }

    private String exceptNodeName(String documentStr, int size, MutableInt indexInt, StringBuilder sb) {
        int index = indexInt.getValue();
        int count = 0;

        while (index < size) {
            char c = documentStr.charAt(index);
            if (c == LEFT_PARENTHESIS || c == LEFT_BRACE) {
                break;
            }
            if (Character.isWhitespace(c)) {
                if (count != 0) {
                    break;
                }
            } else {
                count++;
                sb.append(c);
            }
            index++;
        }
        String str = sb.toString();
        sb.delete(0, count);
        indexInt.setValue(index);
        return str;
    }

    private List<SmlAttributePair> exceptAttribute(String documentStr, int size, MutableInt indexInt, StringBuilder sb) {
        int index = indexInt.getValue();
        // find attribute name and find value
        List<SmlAttributePair> pairList = null;
        breakAttribute:
        while (true) {
            int count = 0;
            //find attribute name
            boolean findEqual = false;
            while (index < size) {
                char c = documentStr.charAt(index);
                if (c == LEFT_BRACE) {
                    break breakAttribute;
                }
                if (c == RIGHT_PARENTHESIS) {
                    index++;
                    break breakAttribute;
                }
                if (c == EQUAL_SIGN) {
                    findEqual = true;
                    index++;
                    break;
                }
                if (Character.isWhitespace(c)) {
                    if (count != 0) {
                        break;
                    }
                } else {
                    sb.append(c);
                    count++;
                }
                index++;
            }
            String pairName = sb.toString();
            sb.delete(0, count);
            count = 0;
            // find equal
            if (!findEqual) {
                while (index < size) {
                    char c = documentStr.charAt(index++);
                    if (c == RIGHT_PARENTHESIS) {
                        break breakAttribute;
                    }
                    if (c == EQUAL_SIGN) {
                        break;
                    }
                }
            }
            // find attribute value "
            while (index < size) {
                char c = documentStr.charAt(index++);
                if (c == RIGHT_PARENTHESIS) {
                    break breakAttribute;
                }
                if (c == DOUBLE_QUOTE) {
                    break;
                }
            }
            // find attribute value context
            while (index < size) {
                char c = documentStr.charAt(index++);
                if (c == DOUBLE_QUOTE) {
                    break;
                }
                sb.append(c);
                count++;
            }
            String pairValue = sb.toString();
            sb.delete(0, count);
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
        String rootName = exceptNodeName(documentStr, size, indexInt, sb);
        List<SmlAttributePair> attributePairList = exceptAttribute(documentStr, size, indexInt, sb);
        int index = indexInt.getValue();
        while (index < size) {
            char c = documentStr.charAt(index++);
            if (c == LEFT_BRACE) {
                break;
            }
        }
        indexInt.setValue(index);
        SmlNode rootNode = new SmlNode();
        rootNode.setName(rootName);
        if (attributePairList != null) {
            rootNode.setPairList(attributePairList);
        }
        smlDocument.setRoot(rootNode);
        List<SmlElement> smlElementList = parseNode(documentStr, size, indexInt, sb);
        rootNode.setElementList(smlElementList);
    }

    private List<SmlElement> parseNode(String documentStr, int size, MutableInt indexInt, StringBuilder sb) {
        int index = indexInt.getValue();
        int count;
        List<SmlElement> elementList = null;
        nodeEnd:
        while (index < size) {
            count = 0;
            Boolean haveAttribute = null;
            Boolean haveNode = null;

            while (index < size) {
                char c = documentStr.charAt(index++);
                if (c == RIGHT_BRACE) {
                    haveAttribute = Boolean.FALSE;
                    haveNode = Boolean.FALSE;
                    break;
                }
                if (c == LEFT_PARENTHESIS) {
                    haveAttribute = Boolean.TRUE;
                    break;
                }
                if (c == LEFT_BRACE) {
                    haveNode = Boolean.TRUE;
                    break;
                }
                if (!Character.isWhitespace(c)) {
                    sb.append(c);
                    count++;
                } else {
                    if (count > 0) {
                        break;
                    }
                }
            }
            String nodeNameOrElement = sb.toString();
            sb.delete(0, count);

            SmlElement element = new SmlElement();
            SmlNode currentNode = new SmlNode();
            indexInt.setValue(index);

            if (haveAttribute == Boolean.TRUE) {
                List<SmlAttributePair> pairList = exceptAttribute(documentStr, size, indexInt, sb);
                index = indexInt.getValue();
                if (pairList != null) {
                    currentNode.setPairList(pairList);
                }
            }

            if (haveNode == null) {
                while (index < size) {
                    char c = documentStr.charAt(index++);
                    if (c == LEFT_BRACE) {
                        haveNode = true;
                        break;
                    }
                    if (!Character.isWhitespace(c)) {
                        sb.append(c);
                        count++;
                    } else {
                        if (count > 0) {
                            break;
                        }
                    }
                }
            }

            if (haveAttribute == Boolean.FALSE && haveNode == Boolean.FALSE) {
                element.setStrValue(nodeNameOrElement);
                return Collections.singletonList(element);
            }

            currentNode.setName(nodeNameOrElement);
            element.setSmlNode(currentNode);

            if (haveNode == Boolean.TRUE) {
                indexInt.setValue(index);
                List<SmlElement> children = parseNode(documentStr, size, indexInt, sb);
                index = indexInt.getValue();
                currentNode.setElementList(children);
            }

            if (elementList == null) {
                elementList = new ArrayList<>();
            }
            elementList.add(element);
            while (index < size) {
                char c = documentStr.charAt(index);
                if (c == RIGHT_BRACE) {
                    index++;
                    break nodeEnd;
                }
                if (!Character.isWhitespace(c)) {
                    break;
                }
                index++;
            }
            indexInt.setValue(index);
        }
        indexInt.setValue(index);
        return elementList;
    }

}
