package org.ss.smljava.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ss.smljava.parse.SmlParser;
import org.ss.smljava.smlmodel.SmlDocument;
import org.ss.smljava.util.ObjectMappers;
import org.ss.smljava.util.SmlToJsonNodes;

public class SmlMapper {

    public <T> T readValue(String content, Class<T> valueType) {
        SmlParser smlParser = new SmlParser();
        SmlDocument smlDocument = smlParser.parseDocument(content);
        ObjectNode objectNode = SmlToJsonNodes.smlDocumentToJsonNode(smlDocument);
        ObjectMapper objectMapper = ObjectMappers.getObjectMapper();
        return objectMapper.convertValue(objectNode, valueType);
    }

}
