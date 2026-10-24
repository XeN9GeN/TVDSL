package AST;

import AST.Nodes.ExprNodes.*;
import AST.NodesInterfaces.ExprNode;
import lexer.Tokens.Token;
import lexer.Tokens.TokenType;

import java.util.List;

//приоритет операций: 2+3*4 = 2+(3*4)
//в порядке возрастания слева направо
//parseExpr(+,-) --> parseTerm(*, /) --> parseFactor(vars, (), unar -)

public class ParserFunny {
    private final List<Token> tokens;
    private int pos = 0;

    public ParserFunny(List<Token> toks, int p){
        this.tokens = toks;
        this.pos = p;
    }


    private Token peek(){
        return tokens.get(pos);
    }
    private Token advancePeek(){
        return tokens.get(pos++);
    }
    private boolean match(TokenType type){//проглатываем текущий токен, если вернули то, что ожидали
        if (peek().type == type){
            advancePeek();
            return true;
        }
        return false;
    }
    private Token expect(TokenType type, String msg) {
        if (peek().type == type) return advancePeek();
        Token t = peek();
        throw new RuntimeException(msg + " (строка " + t.line + ", столбец " + t.s_ind + "). Найден: " + t.type);
    }


    public ExprNode parseExpr(){
        ExprNode left = parseTerm();

        while(peek().type == TokenType.OP_PLUS || peek().type == TokenType.OP_MINUS){
            Token op_token = advancePeek();
            ExprNode right = parseTerm();

            left = new BinaryOpNode(op_token.lexeme, left, right);
        }
        return left;
    }
    public ExprNode parseTerm(){
        ExprNode left = parseFactor();

        while(peek().type == TokenType.OP_STAR || peek().type == TokenType.OP_SLASH){
            Token op_token = advancePeek();
            ExprNode right = parseFactor();

            left = new BinaryOpNode(op_token.lexeme, left,right);
        }
        return left;
    }
    public ExprNode parseFactor(){
        if(peek().type == TokenType.INT){
            Token num_token = advancePeek();
            return new IntNode(Integer.parseInt(num_token.lexeme));
        }

        if(peek().type == TokenType.IDENT) {
            Token identToken = advancePeek();
            return new VarNode(identToken.lexeme);
        }

        if (match(TokenType.LPAREN)) {
            ExprNode expr = parseExpr();
            expect(TokenType.RPAREN, "Ожидалась закрывающая скобка ')'");
            return expr;
        }

        if (match(TokenType.OP_MINUS)) {
            ExprNode expr = parseFactor();
            return new UnaryOpNode("-", expr);
        }

        throw new RuntimeException("Ожидалось выражение (число, переменная или скобка) на строке " + peek().line);
    }



}