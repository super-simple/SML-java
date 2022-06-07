package org.ss.smljava.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.ss.smljava.smlmodel.SmlAttributePair;
import org.ss.smljava.smlmodel.SmlDocument;
import org.ss.smljava.smlmodel.SmlElement;
import org.ss.smljava.smlmodel.SmlNode;

import java.util.List;

public class SmlToJsonNodes {

    public static ObjectNode smlDocumentToJsonNode(SmlDocument smlDocument) {
        ObjectMapper objectMapper = ObjectMappers.getObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        SmlNode root = smlDocument.getRoot();
        String name = root.getName();
        objectNode.set(name, smlNodeToJsonNode(root, objectMapper));
        return objectNode;
    }

    private static JsonNode smlNodeToJsonNode(SmlNode smlNode, ObjectMapper objectMapper) {
        List<SmlAttributePair> pairList = smlNode.getPairList();
        List<SmlElement> elementList = smlNode.getElementList();
        if (pairList == null) {
            if (elementList == null) {
                return NullNode.getInstance();
            }

            if (elementList.size() == 1) {
                SmlElement smlElement = elementList.get(0);
                String strValue = smlElement.getStrValue();
                if (strValue != null) {
                    return new TextNode(strValue);
                }
            }
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        if (pairList != null) {
            if (pairList.size() > 0) {
                for (SmlAttributePair pair : pairList) {
                    objectNode.set(pair.getName(), new TextNode(pair.getValue()));
                }
            }
        }

        if (elementList != null) {
            for (SmlElement smlElement : elementList) {
                SmlNode elementSmlNode = smlElement.getSmlNode();
                objectNode.set(elementSmlNode.getName(), smlNodeToJsonNode(elementSmlNode, objectMapper));
            }
        }
        return objectNode;
    }

}
