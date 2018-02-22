package pluntuml;

import clazz.ClassEnum;
import clazz.ParsedClass;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class PlantumlPainter {
    public void paint(String filePath, String fileName, List<ParsedClass> parsedClassList) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filePath + "/" + fileName + ".puml")));
        bufferedWriter.write("@startuml\n");
        for (ParsedClass parsedClass : parsedClassList) {
            for (String extendsClass : parsedClass.getExtendsClasses()) {
                bufferedWriter.write(extendsClass + "<|--" + parsedClass.getName() + "\n");
            }

            for (String implementsClass : parsedClass.getImplementsClasses()) {
                bufferedWriter.write(implementsClass + "<|.." + parsedClass.getName() + "\n");
            }

            if (parsedClass.getType() == ClassEnum.INTERFACE) {
                bufferedWriter.write("interface " + parsedClass.getName() + "\n");
            } else if (parsedClass.getType() == ClassEnum.ABSTRACT_CLASS) {
                bufferedWriter.write("abstract class " + parsedClass.getName() + "\n");
            } else if (parsedClass.getType() == ClassEnum.ENUM) {
                bufferedWriter.write("enum " + parsedClass.getName() + "\n");
            } else {
                bufferedWriter.write("class " + parsedClass.getName() + "\n");
            }

            bufferedWriter.write("\n\n");
        }

        bufferedWriter.write("@enduml\n");

        bufferedWriter.close();
    }
}
