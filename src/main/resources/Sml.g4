grammar Sml;

content: meta? node;

meta: 'sml' attribute;

node: nodeName (attribute | value | attribute value);

attribute: LEFT_PARENTHESIS pair* RIGHT_PARENTHESIS;

nodeName: NOT_KEYWORD_AND_WHITESPACE;

attributeName: NOT_KEYWORD_AND_WHITESPACE;

pair: attributeName '=' NODE_ATTRIBUTE_VALUE;

value: LEFT_BRACE (node* | NODE_VALUE?) RIGHT_BRACE;

LINE_COMMENT: '//' .*? ('\n'|EOF) -> skip ;
BLOCK_COMMENT: '/*' .*? '*/' -> skip ; // nesting comments allowed
LEFT_PARENTHESIS: '(';
RIGHT_PARENTHESIS: ')';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
WS: [ \n\r\t\u000B\u000C\u0000]+ -> skip;
NOT_KEYWORD_AND_WHITESPACE: (~('`' | '"' | '\'' | '(' | ')' | '{' | '}' | '=' | ' ' | '\n' | '\r' | '\t' | '\u000B' | '\u000C' | '\u0000' ))+;
NODE_VALUE: NOT_KEYWORD | '`' .*? '`';
NODE_ATTRIBUTE_VALUE: '"' .*? '"' | '\'' .*? '\'';
fragment
NOT_KEYWORD: (~('`' | '"' | '\'' | '(' | ')' | '{' | '}' | '=' ))+;