package org.ss.basic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.ss.smljava.parse.SmlParser;
import org.ss.smljava.smlmodel.SmlDocument;
import org.ss.smljava.util.IOs;
import org.ss.smljava.util.SmlToJsonNodes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SmlToJsonNodesTest {
    @Test
    void test1() throws JsonProcessingException {
        InputStream resourceAsStream = SimpleTest.class.getResourceAsStream("/basic/simple.sml");
        String documentStr = IOs.toString(resourceAsStream, StandardCharsets.UTF_8);
        SmlParser smlParser = new SmlParser();
        SmlDocument smlDocument = smlParser.parseDocument(documentStr);
        ObjectNode objectNode = SmlToJsonNodes.smlDocumentToJsonNode(smlDocument);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode));
    }
}
