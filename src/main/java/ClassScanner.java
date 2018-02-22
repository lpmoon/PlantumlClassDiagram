import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;

import java.util.List;

public class ClassScanner extends
        TreeScanner<ParsedClass, ParsedClass> {

    @Override
    public ParsedClass visitClass(ClassTree node, ParsedClass parsedClass) {
        for (Tree member : node.getMembers()) {
            if (member instanceof VariableTree) {
                VariableTree variable = (VariableTree) member;
                parsedClass.getMembers().put(variable.getName().toString(), variable.getType().toString());
            }
        }

        parsedClass.getExtendsClasses().add(node.getExtendsClause().toString());

        for (Tree i : node.getImplementsClause()) {
            parsedClass.getImplementsClasses().add(node.getImplementsClause().toString());
        }

        return parsedClass;
    }
}
