package AST;

import AST.Nodes.ExprNodes.*;
import AST.Nodes.FunctionNode;
import AST.Nodes.ParamNode;
import AST.Nodes.PredNodes.BoolNode;
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
//exprNode - что вычисляется и возвращается значение, stmtNode - команда, инструкция(выполнить цикл, присвоить...)

public class ParserFunny {
    private final List<Token> tokens;
    private int pos = 0;

    public ParserFunny(List<Token> toks){
        this.tokens = toks;
    }
    public final List<String> errors = new ArrayList<>();

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

            BinaryOpNode node = new BinaryOpNode(op_token.lexeme, left, right);
            node.line = op_token.line;
            node.col = op_token.s_ind;
            left = node;
        }
        return left;
    }
    public ExprNode parseTerm(){
        ExprNode left = parseFactor();

        while(peek().type == TokenType.OP_STAR || peek().type == TokenType.OP_SLASH){
            Token op_token = advancePeek();
            ExprNode right = parseFactor();

            BinaryOpNode node = new BinaryOpNode(op_token.lexeme, left,right);
            node.line = op_token.line;
            node.col = op_token.s_ind;
            left = node;
        }
        return left;
    }
    public ExprNode parseFactor() {
        if (peek().type == TokenType.INT) {
            Token num_token = advancePeek();
            IntNode node = new IntNode(Integer.parseInt(num_token.lexeme));
            node.line = num_token.line;
            node.col = num_token.s_ind;
            return node;
        }

        if (peek().type == TokenType.IDENT || peek().type == TokenType.KW_LENGTH) {
            Token identToken = advancePeek();

            if (peek().type == TokenType.LBRACKET) {
                advancePeek();
                ExprNode index = parseExpr();
                expect(TokenType.RBRACKET, "Need ']'");

                ArrayAccessNode node = new ArrayAccessNode(identToken.lexeme, index);
                node.line = identToken.line;
                node.col = identToken.s_ind;
                return node;
            }

            if (peek().type == TokenType.LPAREN) {
                advancePeek();
                List<ExprNode> args = new ArrayList<>();

                if (peek().type != TokenType.RPAREN) {
                    do {
                        args.add(parseExpr());
                    } while (match(TokenType.COMMA));
                }

                expect(TokenType.RPAREN, "Need ')'");
                FuncCallNode node = new FuncCallNode(identToken.lexeme, args);
                node.line = identToken.line;
                node.col = identToken.s_ind;
                return node;
            }
            VarNode node = new VarNode(identToken.lexeme);
            node.line = identToken.line;
            node.col = identToken.s_ind;
            return node;
        }

        if (match(TokenType.LPAREN)) {
            ExprNode expr = parseExpr();
            expect(TokenType.RPAREN, "Need ')'");
            return expr;
        }

        if (match(TokenType.OP_MINUS)) {
            Token opToken = tokens.get(pos - 1);
            ExprNode expr = parseFactor();
            UnaryOpNode node = new UnaryOpNode("-", expr);
            node.line = opToken.line;
            node.col = opToken.s_ind;
            return node;
        }

        throw new RuntimeException("Need выражение (число, переменная или скобка) на строке " + peek().line);
    }

    public StmtNode parseStatement() {
        if (peek().type == TokenType.LBRACE){
            return parseBlock();
        }

        if(match(TokenType.KW_IF)){
            Token ifToken = tokens.get(pos - 1);
            expect(TokenType.LPAREN, "Need '(");
            PredNode cond = parsePredicate();
            expect(TokenType.RPAREN, "Need ')'");
            StmtNode then_branch = parseStatement();

            StmtNode else_branch = null;
            if(match(TokenType.KW_ELSE)){
                else_branch = parseStatement();
            }
            IfNode node = new IfNode(cond, then_branch, else_branch);
            node.line = ifToken.line;
            node.col = ifToken.s_ind;
            return node;
        }

        if(match(TokenType.KW_WHILE)){
            Token whileToken = tokens.get(pos - 1);
            expect(TokenType.LPAREN, "Need '(");
            PredNode cond = parsePredicate();
            expect(TokenType.RPAREN, "Need ')'");

            PredNode inv = null;
            if(match(TokenType.KW_INVARIANT)){
                inv = parsePredicate();
            }

            StmtNode body = parseStatement();
            WhileNode node = new WhileNode(cond,inv,body);
            node.line = whileToken.line;
            node.col = whileToken.s_ind;
            return node;
        }

        if(match(TokenType.KW_ASSERT)){
            Token assertToken = tokens.get(pos - 1);
            PredNode pred = parsePredicate();
            expect(TokenType.SEMI, "Need ';");
            AssertNode node = new AssertNode(pred);
            node.line = assertToken.line;
            node.col = assertToken.s_ind;
            return node;
        }
        if (match(TokenType.KW_ASSUME)) {
            Token assumeToken = tokens.get(pos - 1);
            PredNode pred = parsePredicate();
            expect(TokenType.SEMI, "Need ';'");
            AssumeNode node = new AssumeNode(pred);
            node.line = assumeToken.line;
            node.col = assumeToken.s_ind;
            return node;
        }

        if (peek().type == TokenType.IDENT) {
            Token identToken = advancePeek();

            if (peek().type == TokenType.LBRACKET) {
                advancePeek();
                ExprNode index = parseExpr();
                expect(TokenType.RBRACKET, "Need ']'");
                expect(TokenType.OP_EQ, "Need '='");

                ExprNode value = parseExpr();
                expect(TokenType.SEMI, "Need ';'");

                ArrayAssignNode node = new ArrayAssignNode(identToken.lexeme, index, value);
                node.line = identToken.line;
                node.col = identToken.s_ind;
                return node;
            }

            expect(TokenType.OP_EQ, "Need '='");
            ExprNode value = parseExpr();
            expect(TokenType.SEMI, "Need ';'");
            AssignNode node = new AssignNode(identToken.lexeme, value);
            node.line = identToken.line;
            node.col = identToken.s_ind;
            return node;
        }

        throw new RuntimeException("Неизвестный оператор на строке " + peek().line);
    }

    public BlockNode parseBlock() {
        Token lbraceToken = expect(TokenType.LBRACE, "Need '{'");
        List<StmtNode> statements = new ArrayList<>();

        while (peek().type != TokenType.RBRACE && peek().type != TokenType.EOF) {
            try {
                StmtNode stmt = parseStatement();
                if (stmt != null) statements.add(stmt);
            } catch (RuntimeException e) {
                errors.add(e.getMessage());
                synchronize();
            }
        }

        if(!match(TokenType.RBRACE)){
            Token t = peek();
            errors.add("Need '}' (строка " + t.line + ", столбец " + t.s_ind + "). Найден: " + t.type);
        }
        BlockNode node = new BlockNode(statements);
        node.line = lbraceToken.line;
        node.col = lbraceToken.s_ind;
        return node;
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
        List<ParamNode> params = new ArrayList<>();
        if(peek().type == TokenType.RPAREN || peek().type == TokenType.LBRACE){
            return params;
        }

        do {
            Token paramNameToken = expect(TokenType.IDENT, "Need param's name");
            String paramName = paramNameToken.lexeme;
            expect(TokenType.COLON, "Need ':'");
            String string = expect(TokenType.KW_INT, "Need string 'int'").lexeme;

            if (match(TokenType.LBRACKET)) {
                expect(TokenType.RBRACKET, "Need ']'");
                string = "int[]";
            }
            ParamNode node = new ParamNode(paramName, string);
            node.line = paramNameToken.line;
            node.col = paramNameToken.s_ind;
            params.add(node);
        } while (match(TokenType.COMMA));

        return params;
    }


    public PredNode parsePredicate() {
        PredNode left = parseOrExpr();

        while (peek().type == TokenType.OP_IMPLIES) {
            Token opToken = advancePeek();
            PredNode right = parsePredicate();
            LogicalOpNode node = new LogicalOpNode("->",left,right);
            node.line = opToken.line;
            node.col = opToken.s_ind;
            return node;
        }
        return left;
    }
    public PredNode parseOrExpr(){
        PredNode left = parseAndExpr();
        while(peek().type == TokenType.KW_OR){
            Token opToken = advancePeek();
            PredNode right = parseAndExpr();
            LogicalOpNode node = new LogicalOpNode("or",left,right);
            node.line = opToken.line;
            node.col = opToken.s_ind;
            left = node;
        }
        return left;
    }
    public PredNode parseAndExpr(){
        PredNode left = parseNotExpr();

        while(peek().type == TokenType.KW_AND){
            Token opToken = advancePeek();
            PredNode right = parseNotExpr();
            LogicalOpNode node = new LogicalOpNode("and",left,right);
            node.line = opToken.line;
            node.col = opToken.s_ind;
            left = node;
        }
        return left;
    }
    public PredNode parseNotExpr() {
        if (match(TokenType.KW_NOT)) {
            Token notToken = tokens.get(pos - 1);
            NotNode node = new NotNode(parseNotExpr());
            node.line = notToken.line;
            node.col = notToken.s_ind;
            return node;
        }
        return parseComparison();
    }
    public PredNode parseComparison(){
        if (match(TokenType.KW_TRUE)) {
            Token t = tokens.get(pos - 1);
            BoolNode node = new BoolNode(true);
            node.line = t.line;
            node.col = t.s_ind;
            return node;
        }
        if (match(TokenType.KW_FALSE)) {
            Token t = tokens.get(pos - 1);
            BoolNode node = new BoolNode(false);
            node.line = t.line;
            node.col = t.s_ind;
            return node;
        }

        if(match(TokenType.LPAREN)){
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
            ComparisonNode node = new ComparisonNode(optoken.lexeme,left,right);
            node.line = optoken.line;
            node.col = optoken.s_ind;
            return node;
        }
        throw new RuntimeException("Need оператор сравнения (==, !=, <, >, <=, >=) на строке " + peek().line);
    }

    private void synchronize() {//error check
        while (peek().type != TokenType.EOF) {
            if (peek().type == TokenType.SEMI) {
                advancePeek();
                return;
            }
            if (peek().type == TokenType.RBRACE) {
                return;
            }
            advancePeek();//skip trash
        }
    }


}