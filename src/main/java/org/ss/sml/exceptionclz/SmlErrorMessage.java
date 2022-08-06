package org.ss.sml.exceptionclz;

public interface SmlErrorMessage {
    String NOT_START_WITH_SML = "not start with sml";

    String EXCEPT_KEYWORD = "except keyword";

    //left
    String EXCEPT_LEFT_BRACE_OR_BRACKET = "except left brace or left bracket";
    String EXCEPT_LEFT_PARENTHESIS = "except left parenthesis";
    String EXCEPT_LEFT_BRACE = "except left brace";

    //right
    String EXCEPT_RIGHT_BRACE = "except right brace";

    String EXCEPT_ATTRIBUTE_NAME = "except attribute name";
    String EXCEPT_ATTRIBUTE_VALUE = "except attribute value";
    String EXCEPT_ATTRIBUTE_VALUE_CONTEXT = "except attribute value context";

    //token 错误
    String EXCEPT_EQUAL = "except equal";
    String EXCEPT_DOUBLE_QUOTE = "except double quote";
    String EXCEPT_SINGLE_QUOTE = "except single quote";
    String EXCEPT_QUOTE = "except quote";

    // 结构错误
    String CONTEXT_NODE_WITH_ATTRIBUTE = "context node can not with attribute";
}
