grammar Sml;

content: meta? node;

meta: 'sml' attribute;

node: NOT_KEYWORD (attribute | nodeBody | attribute nodeBody);

attribute: LEFT_PARENTHESIS pair* RIGHT_PARENTHESIS;

pair: NOT_KEYWORD EQUAL (DOUBLE_QUOTE_STRING | SINGLE_QUOTE_STRING);

nodeBody: LEFT_BRACE (nodeSimpleValue | node)* RIGHT_BRACE;

nodeSimpleValue: NOT_KEYWORD+ | BACK_QUOTE_STRING;

LINE_COMMENT: '//' .*? ('\n'|EOF) -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
WHITE_SPACE: [ \n\r\t\u000B\u000C\u0000]+ -> channel(HIDDEN);

BACK_QUOTE_STRING: '`' .*? '`';
DOUBLE_QUOTE_STRING: '"' .*? '"';
SINGLE_QUOTE_STRING: '\'' .*? '\'';
LEFT_PARENTHESIS: '(';
RIGHT_PARENTHESIS: ')';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
EQUAL: '=';

// no ( ) { }
NOT_KEYWORD: (ESCAPE_CHAR | [!-'*-z|~] | Unicode_CHAR)+;
fragment
ESCAPE_CHAR: '\\' [\\bfnrt(){}s];
fragment
Unicode_CHAR: '\\u' Hex_Digit Hex_Digit Hex_Digit Hex_Digit;

fragment
Hex_Digit: [0-9a-fA-F];