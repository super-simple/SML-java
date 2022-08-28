package org.ss.sml.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ss.sml.parse.SmlDelimiter;

public class SmlObjectPrettyWriter extends SmlAbstractObjectWriter {

    @Override
    public String write(JsonNode jsonNode) {
        StringBuilder sb = new StringBuilder();
        writeElementInArray(jsonNode, sb, 0);
        return sb.toString();
    }

    private void writeElementInArray(JsonNode jsonNode, StringBuilder sb, int depth) {
        if (jsonNode.isArray()) {
            writeArray(jsonNode, sb, depth);
        } else if (jsonNode.isObject()) {
            writeObject(jsonNode, sb, depth);
        } else {
            writeValue(jsonNode, sb);
        }
    }

    private void writeElementInObject(JsonNode jsonNode, StringBuilder sb, int depth) {
        if (jsonNode.isArray()) {
            writeArray(jsonNode, sb, depth);
        } else if (jsonNode.isObject()) {
            writeObject(jsonNode, sb, depth);
        } else {
            sb.append(SmlDelimiter.LEFT_PARENTHESIS);
            writeValue(jsonNode, sb);
            sb.append(SmlDelimiter.RIGHT_PARENTHESIS);
        }
    }

    private void writeArray(JsonNode jsonNode, StringBuilder sb, int depth) {
        sb.append(SmlDelimiter.LEFT_BRACKET);
        sb.append(SmlDelimiter.LF);
        depth += 1;
        ArrayNode arrayNode = (ArrayNode) jsonNode;
        for (JsonNode node : arrayNode) {
            sb.append(tabDepth(depth));
            writeElementInArray(node, sb, depth);
            sb.append(SmlDelimiter.LF);
        }
        depth -= 1;
        sb.append(tabDepth(depth));
        sb.append(SmlDelimiter.RIGHT_BRACKET);
    }

    private void writeObject(JsonNode jsonNode, StringBuilder sb, int depth) {
        sb.append(SmlDelimiter.LEFT_BRACE);
        sb.append(SmlDelimiter.LF);
        depth += 1;
        int finalDepth = depth;
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.fields().forEachRemaining(stringJsonNodeEntry -> {
            String key = stringJsonNodeEntry.getKey();
            JsonNode value = stringJsonNodeEntry.getValue();
            sb.append(tabDepth(finalDepth));
            sb.append(key);
            writeElementInObject(value, sb, finalDepth);
            sb.append(SmlDelimiter.LF);
        });
        depth -= 1;
        sb.append(tabDepth(depth));
        sb.append(SmlDelimiter.RIGHT_BRACE);
    }

    private char[] tabDepth(int depth) {
        char[] chars = new char[depth];
        for (int i = 0; i < depth; i++) {
            chars[i] = '\t';
        }
        return chars;
    }

}
