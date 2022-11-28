import antlr.MiniPythonASTVisitor;
import antlr.MiniPythonLexer;
import antlr.MiniPythonParser;
import nodes.ASTNode;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import visitors.ASTEvalVisitor;
import visitors.ASTStringVisitor;
import visitors.ASTSymbolVisitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;

public class App {
    public static void main(String[] args) throws Exception {
        MiniPythonLexer lexer = null;
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            lexer = new MiniPythonLexer(CharStreams.fromString(everything));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            lexer = new MiniPythonLexer(CharStreams.fromStream(System.in));
        }
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniPythonParser parser = new MiniPythonParser(tokens);

        ParseTree tree = parser.prog();

        MiniPythonASTVisitor cst = new MiniPythonASTVisitor();
        ASTStringVisitor str = new ASTStringVisitor();
        ASTSymbolVisitor symbol = new ASTSymbolVisitor();
        ASTEvalVisitor eval = new ASTEvalVisitor();

        ASTNode ast = tree.accept(cst);
        ast.accept(symbol);
        ast.accept(eval);
        
        System.out.println(ast.accept(str));

        TreeViewer viewer = new TreeViewer(Collections.emptyList(), ast);
        viewer.open();
    }
}
