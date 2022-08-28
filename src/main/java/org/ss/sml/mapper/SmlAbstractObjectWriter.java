package org.ss.sml.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ss.sml.parse.SmlDelimiter;
import org.ss.sml.parse.SmlKeyword;

public abstract class SmlAbstractObjectWriter implements SmlObjectWriter {
    @Override
    public String write(JsonNode jsonNode) {
        StringBuilder sb = new StringBuilder();
        writeElementInArray(jsonNode, sb);
        return sb.toString();
    }

    protected void writeElementInArray(JsonNode jsonNode, StringBuilder sb) {
        if (jsonNode.isArray()) {
            writeArray(jsonNode, sb);
        } else if (jsonNode.isObject()) {
            writeObject(jsonNode, sb);
        } else {
            writeValue(jsonNode, sb);
        }
    }

    protected void writeElementInObject(JsonNode jsonNode, StringBuilder sb) {
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

    protected void writeArray(JsonNode jsonNode, StringBuilder sb) {
        sb.append(SmlDelimiter.LEFT_BRACKET);
        ArrayNode arrayNode = (ArrayNode) jsonNode;
        for (JsonNode node : arrayNode) {
            writeElementInArray(node, sb);
            sb.append(SmlDelimiter.SPACE);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(SmlDelimiter.RIGHT_BRACKET);
    }

    protected void writeObject(JsonNode jsonNode, StringBuilder sb) {
        sb.append(SmlDelimiter.LEFT_BRACE);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.fields().forEachRemaining(stringJsonNodeEntry -> {
            String key = stringJsonNodeEntry.getKey();
            JsonNode value = stringJsonNodeEntry.getValue();
            sb.append(key);
            writeElementInObject(value, sb);
        });
        sb.append(SmlDelimiter.RIGHT_BRACE);
    }

    protected void writeValue(JsonNode jsonNode, StringBuilder sb) {
        if (jsonNode.isTextual()) {
            sb.append(SmlDelimiter.DOUBLE_QUOTE);
            sb.append(jsonNode.textValue());
            sb.append(SmlDelimiter.DOUBLE_QUOTE);
        } else if (jsonNode.isNumber()) {
            sb.append(jsonNode.numberValue());
        } else if (jsonNode.isBoolean()) {
            sb.append(jsonNode.booleanValue());
        } else {
            sb.append(SmlKeyword.nullString);
        }
    }
}
