package org.ss.sml.parse;

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
    char SINGLE_QUOTE = '\'';

    char BACK_QUOTE = '`';

    char ESCAPE = '\\';

    char SLASH = '/';

    char ASTERISK = '*';

    char CR = '\r';
    char LF = '\n';
}
