package clazz;

import com.sun.source.tree.*;
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
            parsedClass.getExtendsClasses().add(TypeUtil.getType(node.getExtendsClause()));
        }

        for (Tree i : node.getImplementsClause()) {
            parsedClass.getImplementsClasses().add(i.toString());
        }

        for (Tree member : node.getMembers()) {
            if (member instanceof VariableTree) {
                handleVariableTree(parsedClass, (VariableTree) member);
            }

            if (member instanceof MethodTree) {
                handleMethodTree(parsedClass, (MethodTree) member);
            }

            if (member instanceof ClassTree) {
                handleClassTree(parsedClass, (ClassTree) member);
            }
        }

        return parsedClass;
    }

    private void handleClassTree(ParsedClass parsedClass, ClassTree member) {
        ParsedClass innerClass = new ParsedClass();
        visitClass(member, innerClass);
        parsedClass.getInnerClasses().add(innerClass);
    }

    private void handleVariableTree(ParsedClass parsedClass, VariableTree member) {
        VariableTree variable = member;
        parsedClass.getMembers().put(variable.getName().toString(), TypeUtil.getType(variable.getType()));
    }

    private void handleMethodTree(ParsedClass parsedClass, MethodTree member) {
        ClassMethod classMethod = new ClassMethod();

        MethodTree method = member;
        String name = method.getName().toString();
        String returnType;
        if (method.getReturnType() != null) {
            returnType = method.getReturnType().toString();
        } else {
            returnType = "void";
        }

        for (Tree param : method.getParameters()) {
            VariableTree variable = (VariableTree) param;
            classMethod.getParamsType().add(variable.getType().toString());
        }
        classMethod.setName(name);
        classMethod.setReturnType(returnType);

        parsedClass.getMethods().add(classMethod);
    }
}
