grammar sml;

body: meta data;

meta: 'sml' attribute;

data: node*;

node: NODE_NAME (attribute | value | attribute value);

attribute: Left_Parenthesis attribute* Right_Parenthesis;

value: NODE_NAME '=' NODE_ATTRIBUTE_VALUE;

NODE_NAME: [^ \{\}\(\)="'] [\S]*;

NODE_ATTRIBUTE_VALUE: '"' .* '"' | '\'' .* '\'';

Left_Parenthesis: '(';
Right_Parenthesis: ')';
Left_Brace: '{';
Right_Brace: '}';

WS : [ \n\r\t\u000B\u000C\u0000]+   -> channel(HIDDEN) ;

BLOCK_COMMENT : '/*' .*? '*/'   -> channel(HIDDEN) ; // nesting comments allowed

LINE_COMMENT : '//' .*? ('\n'|EOF)  -> channel(HIDDEN) ;