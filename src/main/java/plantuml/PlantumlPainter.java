package plantuml;

import clazz.ClassEnum;
import clazz.ClassMethod;
import clazz.ParsedClass;

import java.io.*;
import java.util.List;
import java.util.Map;

public class PlantumlPainter {
    public void paint(String filePath, String fileName, List<ParsedClass> parsedClassList) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filePath + "/" + fileName + ".puml")));
        bufferedWriter.write("@startuml\n");

        for (ParsedClass parsedClass : parsedClassList) {
            paintClass(bufferedWriter, parsedClass);
            paintExtends(bufferedWriter, parsedClass);
            paintImplements(bufferedWriter, parsedClass);
            paintDependencies(bufferedWriter, parsedClass);
            bufferedWriter.write("\n\n");
        }

        bufferedWriter.write("@enduml\n");

        bufferedWriter.close();
    }

    private void paintDependencies(BufferedWriter bufferedWriter, ParsedClass parsedClass) throws IOException {
        for (Map.Entry<String, String> entry : parsedClass.getMembers().entrySet()) {
            if (!parsedClass.getName().equals(entry.getValue())) {
                bufferedWriter.write(parsedClass.getName() + "*.." + entry.getValue() + "\n");
            }
        }
    }

    private void paintImplements(BufferedWriter bufferedWriter, ParsedClass parsedClass) throws IOException {
        for (String implementsClass : parsedClass.getImplementsClasses()) {
            bufferedWriter.write(implementsClass + "<|.." + parsedClass.getName() + "\n");
        }
    }

    private void paintExtends(BufferedWriter bufferedWriter, ParsedClass parsedClass) throws IOException {
        for (String extendsClass : parsedClass.getExtendsClasses()) {
            bufferedWriter.write(extendsClass + "<|--" + parsedClass.getName() + "\n");
        }
    }

    private void paintClass(BufferedWriter bufferedWriter, ParsedClass parsedClass) throws IOException {
        String type = "";
        if (parsedClass.getType() == ClassEnum.INTERFACE) {
            type = "interface";
        } else if (parsedClass.getType() == ClassEnum.ABSTRACT_CLASS) {
            type = "abstract class";
        } else if (parsedClass.getType() == ClassEnum.ENUM) {
            type = "enum";
        } else {
            type = "class";
        }

        bufferedWriter.write(type + " " + parsedClass.getName() + "{ \n");

        for (Map.Entry<String, String> entry : parsedClass.getMembers().entrySet()) {
            bufferedWriter.write("    " + entry.getKey() + " : " + entry.getValue() + "\n");
        }

        for (ClassMethod classMethod : parsedClass.getMethods()) {
            bufferedWriter.write("    " + classMethod.getReturnType() + " " + classMethod.getName() + "()" + "\n");
        }

        bufferedWriter.write("} \n");
    }
}