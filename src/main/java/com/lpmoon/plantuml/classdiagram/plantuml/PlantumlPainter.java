package com.lpmoon.plantuml.classdiagram.plantuml;

import com.lpmoon.plantuml.classdiagram.clazz.ClassEnum;
import com.lpmoon.plantuml.classdiagram.clazz.ClassMethod;
import com.lpmoon.plantuml.classdiagram.clazz.ParsedClass;
import com.lpmoon.plantuml.classdiagram.util.ClassUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class PlantumlPainter {
    private BufferedWriter bufferedWriter;
    private Map<String, ParsedClass> parsedClassMap;
    private boolean ignoreClassNotInParsedMap;

    public PlantumlPainter(String filePath, String fileName, Map<String, ParsedClass> parsedClassMap, boolean ignoreClassNotInParsedMap) {
        try {
            this.bufferedWriter = new BufferedWriter(new FileWriter(new File(filePath + "/" + fileName + ".puml")));
            this.parsedClassMap = parsedClassMap;
            this.ignoreClassNotInParsedMap = ignoreClassNotInParsedMap;
        } catch (IOException e) {
        }
    }

    public void begin() throws IOException {
        bufferedWriter.write("@startuml\n");
    }

    public void end() throws IOException {
        bufferedWriter.write("@enduml\n");
        bufferedWriter.close();
    }

    public void paint() throws IOException {
        for (ParsedClass parsedClass : this.parsedClassMap.values()) {
            paint(parsedClass);
        }
    }

    private void paint(ParsedClass parsedClass) throws IOException {
        bufferedWriter.write("\n");
        paintClass(parsedClass);
        paintExtends(parsedClass);
        paintImplements(parsedClass);
        paintDependencies(parsedClass);
        paintInnerClass(parsedClass);
        bufferedWriter.write("\n");
    }

    private void paintInnerClass(ParsedClass parsedClass) throws IOException {
        for (ParsedClass innerClass : parsedClass.getInnerClasses()) {
            if (!ignoreClassNotInParsedMap || parsedClassMap.containsKey(innerClass.getFullName())) {
                bufferedWriter.write(parsedClass.getFullName() + "+-- " + innerClass.getFullName() + "\n");
            }
        }
    }

    private void paintDependencies(ParsedClass parsedClass) throws IOException {
        for (Map.Entry<String, String> entry : parsedClass.getMembers().entrySet()) {
            String clazz = entry.getValue();
            String fullClass = parsedClass.getFullClass(clazz);
            if (!parsedClass.getName().equals(clazz) && !ClassUtil.isJdkBasicClass(fullClass)) {
                if (!ignoreClassNotInParsedMap || parsedClassMap.containsKey(fullClass)) {
                    bufferedWriter.write(parsedClass.getFullName() + "*.. " + fullClass + "\n");
                }
            }
        }
    }

    private void paintImplements(ParsedClass parsedClass) throws IOException {
        for (String implementsClass : parsedClass.getImplementsClasses()) {
            if (!ignoreClassNotInParsedMap || parsedClassMap.containsKey(parsedClass.getFullClass(implementsClass))) {
                bufferedWriter.write(parsedClass.getFullClass(implementsClass) + "<|.. " + parsedClass.getFullName() + "\n");
            }
        }
    }

    private void paintExtends(ParsedClass parsedClass) throws IOException {
        for (String extendsClass : parsedClass.getExtendsClasses()) {
            if (!ignoreClassNotInParsedMap || parsedClassMap.containsKey(parsedClass.getFullClass(extendsClass))) {
                bufferedWriter.write(parsedClass.getFullClass(extendsClass) + "<|-- " + parsedClass.getFullName() + "\n");
            }
        }
    }

    private void paintClass(ParsedClass parsedClass) throws IOException {
        bufferedWriter.write("' =========== " + parsedClass.getFullName() + " =========== \n");
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

        bufferedWriter.write(type + " " + parsedClass.getFullName() + "{ \n");

        for (Map.Entry<String, String> entry : parsedClass.getMembers().entrySet()) {
            bufferedWriter.write("    " + entry.getKey() + " : " + entry.getValue() + "\n");
        }

        for (ClassMethod classMethod : parsedClass.getMethods()) {
            bufferedWriter.write("    " + classMethod.getReturnType() + " " + classMethod.getName() + "()" + "\n");
        }

        bufferedWriter.write("} \n");
    }
}
