package org.ss.sml.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ss.sml.parse.SmlDelimiter;

public class SmlObjectMiniWriter extends SmlAbstractObjectWriter {

    @Override
    public String write(JsonNode jsonNode) {
        StringBuilder sb = new StringBuilder();
        writeElementInArray(jsonNode, sb);
        return sb.toString();
    }

    private void writeElementInArray(JsonNode jsonNode, StringBuilder sb) {
        if (jsonNode.isArray()) {
            writeArray(jsonNode, sb);
        } else if (jsonNode.isObject()) {
            writeObject(jsonNode, sb);
        } else {
            writeValue(jsonNode, sb);
        }
    }

    private void writeElementInObject(JsonNode jsonNode, StringBuilder sb) {
        if (jsonNode.isArray()) {
            writeArray(jsonNode, sb);
        } else if (jsonNode.isObject()) {
            writeObject(jsonNode, sb);
        } else {
            sb.append(SmlDelimiter.LEFT_PARENTHESIS);
            writeValue(jsonNode, sb);
            sb.append(SmlDelimiter.RIGHT_PARENTHESIS);
        }
    }

    private void writeArray(JsonNode jsonNode, StringBuilder sb) {
        sb.append(SmlDelimiter.LEFT_BRACKET);
        ArrayNode arrayNode = (ArrayNode) jsonNode;
        for (JsonNode node : arrayNode) {
            writeElementInArray(node, sb);
            sb.append(SmlDelimiter.SPACE);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(SmlDelimiter.RIGHT_BRACKET);
    }

    private void writeObject(JsonNode jsonNode, StringBuilder sb) {
        sb.append(SmlDelimiter.LEFT_BRACE);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.fields().forEachRemaining(stringJsonNodeEntry -> {
            String key = stringJsonNodeEntry.getKey();
            JsonNode value = stringJsonNodeEntry.getValue();
            sb.append(key);
            writeElementInArray(value, sb);
        });
        sb.append(SmlDelimiter.RIGHT_BRACE);
    }

}
