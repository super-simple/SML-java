package org.ss.sml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ss.sml.parse.SmlDataParser;
import org.ss.sml.util.ObjectMappers;

public class SmlObjectReaderImpl implements SmlObjectReader {

    private static final ObjectMapper OBJECT_MAPPER = ObjectMappers.getObjectMapper();

    @Override
    public <T> T readSml(String smlStr, Class<T> clz) {
        try {
            JsonNode jsonNode = SmlDataParser.parse(smlStr);
            return OBJECT_MAPPER.treeToValue(jsonNode, clz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
