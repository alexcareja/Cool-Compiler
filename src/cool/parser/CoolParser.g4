parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program : (classes+=classDef)* EOF;

formal: ID COLON TYPE;

classElement: ID COLON TYPE (ASSIGN initValue=expr)? SEMI                                                   # attribute
    | ID LPAR (formals+=formal (COMMA formals+=formal)* )? RPAR COLON TYPE LBRACE (body+=expr)* RBRACE SEMI # method
    ;

classDef: CLASS TYPE (INHERITS parent=TYPE)? LBRACE (classBody+=classElement)* RBRACE SEMI;

varDef: ID COLON TYPE (ASSIGN value=expr)?;

caseDef: ID COLON TYPE ARROW body=expr SEMI;

expr: invoker=expr (AT TYPE)? DOT ID LPAR (args+=expr (COMMA args+=expr)*)? RPAR        # staticDispatch
    | ID LPAR (args+=expr (COMMA args+=expr)*)? RPAR                                    # dispatch
    | IF cond = expr THEN thenBranch=expr ELSE elseBranch=expr FI                       # if
    | WHILE cond=expr LOOP (body+=expr) POOL                                            # while
    | LBRACE (blockBody+=expr SEMI)+ RBRACE                                             # block
    | LET vars+=varDef (COMMA vars+=varDef)* IN body=expr                               # let
    | CASE value=expr OF (cases+=caseDef)+ ESAC                                         # case
    | NEW TYPE                                                                          # new
    | NEG value=expr                                                                    # neg
    | ISVOID value=expr                                                                 # voidCheck
    | lo=expr op=(MULT | DIV) ro=expr                                                   # arithmetic
    | lo=expr op=(PLUS | MINUS) ro=expr                                                 # arithmetic
    | lo=expr op=(LT | LE | EQ) ro=expr                                                 # relational
    | NOT value=expr                                                                    # not
    | LPAR par=expr RPAR                                                                # par
    | ID ASSIGN value=expr                                                              # assign
    | ID                                                                                # id
    | INT                                                                               # int
    | STRING                                                                            # string
    | BOOL                                                                              # bool
    ;
