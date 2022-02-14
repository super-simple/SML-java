grammar Sml;

content: meta? node;

meta: 'sml' attribute;

node: NOT_KEYWORD_AND_WHITESPACE (attribute | value | attribute value);

attribute: '(' pair* ')';

pair: NOT_KEYWORD_AND_WHITESPACE '=' NODE_ATTRIBUTE_VALUE;

value: '{' (NODE_VALUE | node)* '}';

LINE_COMMENT: '//' .*? ('\n'|EOF) -> skip ;
BLOCK_COMMENT: '/*' .*? '*/' -> skip ; // nesting comments allowed
WS: [ \n\r\t\u000B\u000C\u0000]+ -> skip;
NOT_KEYWORD_AND_WHITESPACE: [a-zA-Z0-9]+;
NODE_VALUE: NOT_KEYWORD | '`' .*? '`';
NODE_ATTRIBUTE_VALUE: '"' .*? '"' | '\'' .*? '\'';
fragment
NOT_KEYWORD: [a-zA-Z0-9]+;