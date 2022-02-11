grammar Sml;

body: meta? data;

meta: 'sml' attribute;

data: node*;

node: NODE_NAME (attribute | value | attribute value);

attribute: LEFT_PARENTHESIS pair* RIGHT_PARENTHESIS;

pair: NODE_NAME '=' NODE_ATTRIBUTE_VALUE;

value: LEFT_BRACE node* RIGHT_BRACE;

NODE_NAME: ~( '`' | '"' | '\'' | '(' | ')' | '{' | '}' )+;

NODE_ATTRIBUTE_VALUE: '"' .*? '"' | '\'' .*? '\'';

LEFT_PARENTHESIS: '(';
RIGHT_PARENTHESIS: ')';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';

WS : [ \n\r\t\u000B\u000C\u0000]+   -> channel(HIDDEN) ;

BLOCK_COMMENT : '/*' .*? '*/'   -> channel(HIDDEN) ; // nesting comments allowed

LINE_COMMENT : '//' .*? ('\n'|EOF)  -> channel(HIDDEN) ;