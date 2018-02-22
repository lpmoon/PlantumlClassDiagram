package clazz;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;

import javax.lang.model.element.Modifier;
import java.util.Set;

public class ClassScanner extends
        TreeScanner<ParsedClass, ParsedClass> {

    @Override
    public ParsedClass visitClass(ClassTree node, ParsedClass parsedClass) {
        parsedClass.setName(node.getSimpleName().toString());

        if (node.getKind() == Tree.Kind.ENUM) {
            parsedClass.setType(ClassEnum.ENUM);
        } else if (node.getKind() == Tree.Kind.INTERFACE) {
            parsedClass.setType(ClassEnum.INTERFACE);
        }

        String[] modifiers = node.getModifiers().toString().split(" ");
        if (modifiers.length == 2 && modifiers[1].equals("abstract")) {
            parsedClass.setType(ClassEnum.ABSTRACT_CLASS);
        }

        if (node.getExtendsClause() != null) {
            parsedClass.getExtendsClasses().add(node.getExtendsClause().toString());
        }

        for (Tree i : node.getImplementsClause()) {
            parsedClass.getImplementsClasses().add(i.toString());
        }

        for (Tree member : node.getMembers()) {
            if (member instanceof VariableTree) {
                VariableTree variable = (VariableTree) member;
                parsedClass.getMembers().put(variable.getName().toString(), variable.getType().toString());
            }
        }

        return parsedClass;
    }
}
