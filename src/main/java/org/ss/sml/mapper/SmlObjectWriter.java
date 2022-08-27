package org.ss.sml.mapper;

import com.fasterxml.jackson.databind.JsonNode;

public interface SmlObjectWriter {
    String write(JsonNode jsonNode);
}
