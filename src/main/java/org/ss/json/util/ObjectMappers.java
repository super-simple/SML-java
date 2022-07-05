package org.ss.json.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMappers {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {

    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
