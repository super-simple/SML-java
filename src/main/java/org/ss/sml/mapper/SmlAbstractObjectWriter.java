package org.ss.sml.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.ss.sml.parse.SmlKeyword;

public abstract class SmlAbstractObjectWriter implements SmlObjectWriter {
    @Override
    public String write(JsonNode jsonNode) {
        StringBuilder sb = new StringBuilder();
        writeRoot(jsonNode, sb);
        return sb.toString();
    }

    private void writeRoot(JsonNode jsonNode, StringBuilder sb) {
        if (jsonNode.isArray()) {
            writeArray(jsonNode, sb);
        } else if (jsonNode.isObject()) {
            writeObject(jsonNode, sb);
        } else {
            writeValue(jsonNode, sb);
        }
    }

    private void writeArray(JsonNode jsonNode, StringBuilder sb) {

    }

    private void writeObject(JsonNode jsonNode, StringBuilder sb) {

    }

    private void writeValue(JsonNode jsonNode, StringBuilder sb) {
        if (jsonNode.isTextual()) {
            sb.append(jsonNode.textValue());
        } else if (jsonNode.isNumber()) {
            sb.append(jsonNode.numberValue());
        } else if (jsonNode.isBoolean()) {
            sb.append(jsonNode.booleanValue());
        } else {
            sb.append(SmlKeyword.nullString);
        }
    }
}
