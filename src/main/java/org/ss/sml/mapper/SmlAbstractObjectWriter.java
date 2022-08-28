package org.ss.sml.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.ss.sml.parse.SmlDelimiter;
import org.ss.sml.parse.SmlKeyword;

public abstract class SmlAbstractObjectWriter implements SmlObjectWriter {

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
