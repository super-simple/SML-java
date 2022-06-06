package org.ss.smljava.parse;

public interface SmlDelimiter {
    String SML = "sml";

    char LEFT_PARENTHESIS = '(';
    char RIGHT_PARENTHESIS = ')';
    char LEFT_BRACKET = '[';
    char RIGHT_BRACKET = ']';
    char LEFT_BRACE = '{';
    char RIGHT_BRACE = '}';
    char EQUAL_SIGN = '=';
    char DOUBLE_QUOTE = '"';

    char escape = '\\';
}
