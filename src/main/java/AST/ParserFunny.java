package AST;

import AST.Nodes.ExprNodes.*;
import AST.Nodes.FunctionNode;
import AST.Nodes.ParamNode;
import AST.Nodes.PredNodes.ComparisonNode;
import AST.Nodes.PredNodes.LogicalOpNode;
import AST.Nodes.PredNodes.NotNode;
import AST.Nodes.ProgramNode;
import AST.Nodes.StmtNodes.*;
import AST.NodesInterfaces.ExprNode;
import AST.NodesInterfaces.PredNode;
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
    private Token peekNext() {
        if (pos + 1 < tokens.size()) {
            return tokens.get(pos + 1);
        }
        return tokens.get(tokens.size() - 1);
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

        if (peek().type == TokenType.IDENT || peek().type == TokenType.KW_LENGTH) {
            Token identToken = advancePeek();

            if (peek().type == TokenType.LBRACKET) {
                advancePeek();//[  --> a[i]
                ExprNode index = parseExpr();
                expect(TokenType.RBRACKET, "Need ']'");
                return new ArrayAccessNode(identToken.lexeme, index);
            }

            if (peek().type == TokenType.LPAREN) {
                advancePeek();//(  --> length(a) or like that
                List<ExprNode> args = new ArrayList<>();

                if (peek().type != TokenType.RPAREN) {
                    do {
                        args.add(parseExpr());
                    } while (match(TokenType.COMMA));
                }
                expect(TokenType.RPAREN, "Need ')'");
                return new FuncCallNode(identToken.lexeme, args);
            }
            return new VarNode(identToken.lexeme);
        }

        if (match(TokenType.LPAREN)) {
            ExprNode expr = parseExpr();
            expect(TokenType.RPAREN, "Need ')'");
            return expr;
        }

        if (match(TokenType.OP_MINUS)) {
            ExprNode expr = parseFactor();
            return new UnaryOpNode("-", expr);
        }

        throw new RuntimeException("Need выражение (число, переменная или скобка) на строке " + peek().line);
    }

    public StmtNode parseStatement() {
        //{x=5;}
        if (peek().type == TokenType.LBRACE){// {
            return parseBlock();
        }

        // if (cond) stmt [else stmt]
        if(match(TokenType.KW_IF)){
            expect(TokenType.LPAREN, "Need '(");
            PredNode cond = parsePredicate();
            expect(TokenType.RPAREN, "Need ')'");
            StmtNode then_branch = parseStatement();

            StmtNode else_branch = null;
            if(match(TokenType.KW_ELSE)){
                else_branch = parseStatement();
            }
            return new IfNode(cond, then_branch, else_branch);
        }

        //while (cond) [inv P] stmt
        if(match(TokenType.KW_WHILE)){
            expect(TokenType.LPAREN, "Need '(");
            PredNode cond = parsePredicate();
            expect(TokenType.RPAREN, "Need ')'");

            PredNode inv = null;
            if(match(TokenType.KW_INVARIANT)){
                inv = parsePredicate();
            }
            StmtNode body = parseStatement();
            return new WhileNode(cond,inv,body);
        }

        //assert/assume P
        if(match(TokenType.KW_ASSERT)){
            PredNode pred = parsePredicate();
            expect(TokenType.SEMI, "Need ';");
            return new AssertNode(pred);
        }
        if (match(TokenType.KW_ASSUME)) {
            PredNode pred = parsePredicate();
            expect(TokenType.SEMI, "Need ';'");
            return new AssumeNode(pred);
        }

        //IDENT = Expr ";" или a[i] = Expr ;
        if (peek().type == TokenType.IDENT) {
            Token identToken = advancePeek();

            //a[i] = ...
            if (peek().type == TokenType.LBRACKET) {
                advancePeek();
                ExprNode index = parseExpr();
                expect(TokenType.RBRACKET, "Need ']'");
                expect(TokenType.OP_EQ, "Need '='");
                ExprNode value = parseExpr();
                expect(TokenType.SEMI, "Need ';'");
                return new ArrayAssignNode(identToken.lexeme, index, value);
            }

            expect(TokenType.OP_EQ, "Need '='");
            ExprNode value = parseExpr();
            expect(TokenType.SEMI, "Need ';'");
            return new AssignNode(identToken.lexeme, value);
        }

        throw new RuntimeException("Неизвестный оператор на строке " + peek().line);
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

        PredNode requires = null;
        if (match(TokenType.KW_REQUIRES)) {
            requires = parsePredicate();
        }

        expect(TokenType.KW_RETURNS, "Need 'returns'");
        List<ParamNode> returns = parseParamList();

        PredNode ensures = null;
        if (match(TokenType.KW_ENSURES)) {
            ensures = parsePredicate();
        }

        BlockNode body = parseBlock();
        return new FunctionNode(name, params, returns, requires, ensures, body);
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


    public PredNode parsePredicate() {
        PredNode left = parseAndExpr();

        while (peek().type == TokenType.KW_OR || peek().type == TokenType.OP_IMPLIES) {
            Token optoken = advancePeek();
            PredNode right = parseAndExpr();
            left = new LogicalOpNode(optoken.lexeme, left, right);
        }
        return left;
    }
    public PredNode parseAndExpr(){
        PredNode left = parseNotExpr();

        while(peek().type == TokenType.KW_AND){
            advancePeek();
            PredNode right = parseNotExpr();
            left = new LogicalOpNode("and",left,right);
        }
        return left;
    }
    public PredNode parseNotExpr() {
        if (match(TokenType.KW_NOT)) {
            return new NotNode(parseNotExpr());
        }
        return parseComparison();
    }
    //(x+y>0)
    public PredNode parseComparison(){
        if(match(TokenType.LPAREN)){//smth like (Pred...)
            PredNode pred = parsePredicate();
            expect(TokenType.RPAREN, "Need ')");
            return pred;
        }

        ExprNode left = parseExpr();

        if (peek().type == TokenType.OP_EEQ || peek().type == TokenType.OP_NEQ ||
                peek().type == TokenType.OP_LT || peek().type == TokenType.OP_LE ||
                peek().type == TokenType.OP_GT || peek().type == TokenType.OP_GE) {
            Token optoken = advancePeek();
            ExprNode right = parseExpr();
            return new ComparisonNode(optoken.lexeme,left,right);
        }
        throw new RuntimeException("Ожидался оператор сравнения (==, !=, <, >, <=, >=) на строке " + peek().line);
    }

}