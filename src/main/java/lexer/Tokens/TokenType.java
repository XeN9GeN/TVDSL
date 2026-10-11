package lexer.Tokens;

public enum TokenType {
    // Имена и константы
    IDENT,
    INT,
    EOF,

    // Объявления функций и контракты
    KW_FUNCTION,
    KW_RETURNS,
    KW_REQUIRES,
    KW_ENSURES,
    KW_USES,

    // Управление потоком
    KW_IF,
    KW_ELSE,
    KW_WHILE,
    KW_INVARIANT,

    // Верификация
    KW_ASSERT,
    KW_ASSUME,
    KW_FORALL,
    KW_EXISTS,

    // Логические константы и операции
    KW_TRUE,
    KW_FALSE,
    KW_NOT,
    KW_AND,
    KW_OR,

    // Типы и встроенные имена
    KW_INT,
    KW_LENGTH,

    // Присваивание и сравнения
    OP_EQ,       // =
    OP_EEQ,      // ==
    OP_NEQ,      // !=
    OP_LT,       // <
    OP_LE,       // <=
    OP_GT,       // >
    OP_GE,       // >=

    // Арифметика
    OP_PLUS,     // +
    OP_MINUS,    // -
    OP_STAR,     // *
    OP_SLASH,    // /

    // Стрелки
    OP_IMPLIES,       // ->
    OP_FORMULA_ARROW, // =>

    // Разделители
    LPAREN,      // (
    RPAREN,      // )
    LBRACKET,    // [
    RBRACKET,    // ]
    LBRACE,      // {
    RBRACE,      // }
    COMMA,       // ,
    SEMI,        // ;
    COLON        // :
}