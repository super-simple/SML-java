grammar Sml;

content: meta? node;

meta: 'sml' attribute;

node: NODE_NAME (attribute | value | attribute value);

attribute: LEFT_PARENTHESIS pair* RIGHT_PARENTHESIS;

pair: ATTRIBUTE_NAME '=' NODE_ATTRIBUTE_VALUE;

value: LEFT_BRACE (node* | NODE_VALUE?) RIGHT_BRACE;

LINE_COMMENT: '//' .*? ('\n'|EOF) -> skip ;
BLOCK_COMMENT: '/*' .*? '*/' -> skip ; // nesting comments allowed
WS: [ \n\r\t\u000B\u000C\u0000]+ -> skip;
NODE_NAME: NOT_KEYWORD_AND_WHITESPACE;
ATTRIBUTE_NAME: NOT_KEYWORD_AND_WHITESPACE;
NODE_VALUE: NOT_KEYWORD | '`' .*? '`';
NODE_ATTRIBUTE_VALUE: '"' .*? '"' | '\'' .*? '\'';
LEFT_PARENTHESIS: '(';
RIGHT_PARENTHESIS: ')';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
fragment
NOT_KEYWORD_AND_WHITESPACE: ~('`' | '"' | '\'' | '(' | ')' | '{' | '}' | '=' | ' ' | '\n' | '\r' | '\t' | '\u000B' | '\u000C' | '\u0000' )+;
fragment
NOT_KEYWORD: ~('`' | '"' | '\'' | '(' | ')' | '{' | '}' | '=' )+;