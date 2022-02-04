lexer grammar CoolLexer;

tokens { ERROR } 

@header{
    package cool.lexer;	
}

@members{    
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }
}

CLASS: 'class';
ELSE: 'else';
FI: 'fi';
IF: 'if';
IN: 'in';
INHERITS: 'inherits';
ISVOID: 'isvoid';
LET: 'let';
LOOP: 'loop';
POOL: 'pool';
THEN: 'then';
WHILE: 'while';
CASE: 'case';
ESAC: 'esac';
NEW: 'new';
OF: 'of';
NOT: 'not';
AT: '@';
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
ASSIGN: '<-';
LT: '<';
LE: '<=';
EQ: '=';
ARROW: '=>';
LPAR: '(';
RPAR: ')';
LBRACE: '{';
RBRACE: '}';
NEG: '~';
SEMI: ';';
COLON: ':';
COMMA: ',';
DOT: '.';

fragment DIGIT: [0-9];
fragment LLETTER: [a-z];
fragment ULETTER: [A-Z];
fragment NEWLINE : '\r'? '\n';

INT: DIGIT+;
BOOL: 'true' | 'false';
ID: LLETTER (DIGIT | LLETTER | ULETTER | '_')*;
TYPE: ULETTER (DIGIT | LLETTER | ULETTER | '_')*;
STRING : '"' ('\\"' | '\\' NEWLINE | .)*? ('"' {
        String s = getText();
        s = s.replace("\\\r\n", "\r\n");
        s = s.replace("\\n", "\n");
        s = s.replace("\\t", "\t");
        s = s.replace("\\b", "\b");
		s = s.replace("\\f", "\f");
		for (int i = s.length() - 1; i  > 0; i--){
			if (s.charAt(i - 1) == '\\' && s.charAt(i) != '\\' && (i < 2 || s.charAt(i - 2) != '\\')) {
				s = new StringBuilder(s).deleteCharAt(i - 1).toString();
			}
		}
		s = s.replace("\\\\",  "\\");
		s = s.substring(1, s.length() - 1);
		setText(s);
        if (s.length() > 1024) {
            raiseError("String constant too long");
        }
        if (s.contains("\0")) {
            raiseError("String contains null character");
        }
    }
  | NEWLINE { raiseError("Unterminated string constant"); }
  | EOF { raiseError("EOF in string constant"); });


COMMENT: '--' .*? (NEWLINE | EOF) -> skip;
BLOCK_COMMENT: '(*' (BLOCK_COMMENT | .)*? ('*)' { skip(); }
 | EOF { raiseError("EOF in comment"); });
HANGING_COMMENT: '*)' { raiseError("Unmatched *)"); };

WS :   [ \n\f\r\t]+ -> skip ;

INVALID_CHARACTER: . { raiseError("Invalid character: " + getText()); };
