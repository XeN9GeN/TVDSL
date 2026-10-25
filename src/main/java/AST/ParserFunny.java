package AST;

import AST.Nodes.ExprNodes.*;
import AST.Nodes.FunctionNode;
import AST.Nodes.ParamNode;
import AST.Nodes.ProgramNode;
import AST.Nodes.StmtNodes.AssignNode;
import AST.Nodes.StmtNodes.BlockNode;
import AST.NodesInterfaces.ExprNode;
import AST.NodesInterfaces.StmtNode;
import lexer.Tokens.Token;
import lexer.Tokens.TokenType;

import java.util.ArrayList;
import java.util.List;

//приоритет операций: 2+3*4 = 2+(3*4)
//в порядке возрастания слева направо
//parseExpr(+,-) --> parseTerm(*, /) --> parseFactor(vars, (), unar -)

public class ParserFunny {
    private final List<Token> tokens;
    private int pos = 0;

    public ParserFunny(List<Token> toks){
        this.tokens = toks;
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
    public ExprNode parseFactor() {
        if (peek().type == TokenType.INT) {
            Token num_token = advancePeek();
            return new IntNode(Integer.parseInt(num_token.lexeme));
        }

        if (peek().type == TokenType.IDENT) {
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

    public StmtNode parseStatement() {
        //{
        //x=5;
        //}
        if (peek().type == TokenType.LBRACE){// {
            return parseBlock();
        }

        if(peek().type == TokenType.IDENT){
            Token id_token = advancePeek();
            expect(TokenType.OP_EQ, "Need '='");

            ExprNode val = parseExpr();

            expect(TokenType.SEMI, "Need ';'");
            return new AssignNode(id_token.lexeme,val);
        }
        throw new RuntimeException("Ожидался оператор на строке " + peek().line);
    }
    public BlockNode parseBlock() {
        expect(TokenType.LBRACE, "Need '{'");
        List<StmtNode> statements = new ArrayList<>();


        while (peek().type != TokenType.RBRACE && peek().type != TokenType.EOF) {// }
            statements.add(parseStatement());
        }

        expect(TokenType.RBRACE, "Need '}'");
        return new BlockNode(statements);
    }

    public ProgramNode parseProgram(){
        List<FunctionNode> funcs = new ArrayList<>();
        while(peek().type != TokenType.EOF){
            funcs.add(parseFunc());
        }
        return new ProgramNode(funcs);
    }
    public FunctionNode parseFunc(){
        //function main(a: int) returns res: int[] {
        //    x = 5;
        //}
        expect(TokenType.KW_FUNCTION, "Need 'function'");
        String name = expect(TokenType.IDENT, "Need function name").lexeme;

        expect(TokenType.LPAREN, "Need '('");
        List<ParamNode> params = parseParamList();
        expect(TokenType.RPAREN, "Need ')'");

        expect(TokenType.KW_RETURNS, "Need 'returns'");
        List<ParamNode> returns = parseParamList();

        BlockNode body = parseBlock();
        return new FunctionNode(name, params, returns, null, null, body);
    }
    public List<ParamNode> parseParamList(){
        // ParamList -> (IDENT ":" Type ("," IDENT ":" Type)*)
        List<ParamNode> params = new ArrayList<>();
        if(peek().type == TokenType.RPAREN || peek().type == TokenType.LBRACE){
            return params;
        }

        do {
            String paramName = expect(TokenType.IDENT, "Need param's name").lexeme;
            expect(TokenType.COLON, "Need ':'");
            String type = expect(TokenType.KW_INT, "Need type 'int'").lexeme;

            if (match(TokenType.LBRACKET)) {// int[]
                expect(TokenType.RBRACKET, "Need ']'");
                type = "int[]";
            }
            params.add(new ParamNode(paramName, type));
        } while (match(TokenType.COMMA));

        return params;
    }


}