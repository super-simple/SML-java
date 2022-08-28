package org.ss.sml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ss.sml.parse.SmlDataParser;
import org.ss.sml.util.ObjectMappers;

public class SmlDefaultObjectMapper implements SmlObjectMapper {
    private static final ObjectMapper OBJECT_MAPPER = ObjectMappers.getObjectMapper();

    private final SmlObjectMiniWriter miniWriter = new SmlObjectMiniWriter();
    private final SmlObjectPrettyWriter prettyWriter = new SmlObjectPrettyWriter();

    @Override
    public <T> T readSml(String smlStr, Class<T> clz) {
        try {
            JsonNode jsonNode = SmlDataParser.parse(smlStr);
            return OBJECT_MAPPER.treeToValue(jsonNode, clz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T readSml(String smlStr, TypeReference<T> valueTypeRef) {
        try {
            JsonNode jsonNode = SmlDataParser.parse(smlStr);
            return OBJECT_MAPPER.treeToValue(jsonNode, OBJECT_MAPPER.constructType(valueTypeRef));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String writeMiniSml(Object object) {
        JsonNode jsonNode = OBJECT_MAPPER.valueToTree(object);
        return miniWriter.write(jsonNode);
    }

    @Override
    public String writePrettySml(Object object) {
        JsonNode jsonNode = OBJECT_MAPPER.valueToTree(object);
        return prettyWriter.write(jsonNode);
    }
}
