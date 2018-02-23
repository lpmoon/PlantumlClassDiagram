import clazz.ClassParser;
import clazz.ParsedClass;
import plantuml.PlantumlPainter;
import util.FileHandler;
import util.RecursiveScanner;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws IOException {
//        drawAllClasses("src/test/", "src/test/", "tmp");
        drawAssociated("src/test/", "src/test/", "tmp2", "AbstractTest");
    }

    public static void drawAssociated(String path, String dest, String name, String clazz) throws IOException {
        List<ParsedClass> parsedClassList = getParsedClasses(path);

        List<ParsedClass> shouldPainted = new ArrayList<>();
        Set<String> processed = new HashSet<>();
        Queue<ParsedClass> tmp = new LinkedBlockingQueue<>();

        ParsedClass centerClass = queryParsedClass(clazz, parsedClassList);
        if (centerClass == null) {
            return;
        }

        processed.add(centerClass.getFullName());
        tmp.add(centerClass);
        shouldPainted.add(centerClass);

        while (!tmp.isEmpty()) {
            ParsedClass currentClass = tmp.poll();
            System.out.println(currentClass);
            for (String implementClass : currentClass.getImplementsClasses()) {
                String fullImplemenClass = currentClass.getFullClass(implementClass);
                ParsedClass parsedClass = queryParsedClass(fullImplemenClass, parsedClassList);
                if (parsedClass == null) {
                    continue;
                }

                if (!processed.contains(parsedClass.getFullName())) {
                    tmp.add(parsedClass);
                    shouldPainted.add(parsedClass);
                }
            }

            for (String extendClass : currentClass.getExtendsClasses()) {
                String fullExtendClass = currentClass.getFullClass(extendClass);
                ParsedClass parsedClass = queryParsedClass(fullExtendClass, parsedClassList);
                if (parsedClass == null) {
                    continue;
                }

                if (!processed.contains(parsedClass.getFullName())) {
                    tmp.add(parsedClass);
                    shouldPainted.add(parsedClass);
                }
            }

            for (Map.Entry entry : currentClass.getMembers().entrySet()) {
                String dependencyClass = currentClass.getFullClass((String) entry.getValue());
                if (dependencyClass.equals(currentClass.getFullName())) {
                    continue;
                }

                ParsedClass parsedClass = queryParsedClass(dependencyClass, parsedClassList);
                if (parsedClass == null) {
                    continue;
                }

                if (!processed.contains(parsedClass.getFullName())) {
                    tmp.add(parsedClass);
                    shouldPainted.add(parsedClass);
                }
            }
        }

        PlantumlPainter painter = new PlantumlPainter(dest, name);
        painter.begin();
        painter.paint(shouldPainted);
        painter.end();

        return;
    }

    private static ParsedClass queryParsedClass(String clazz, List<ParsedClass> parsedClassList) {
        for (ParsedClass parsedClass : parsedClassList) {
            if (parsedClass.getFullName().equals(clazz)) {
                return parsedClass;
            }
        }

        return null;
    }

    public static void drawAllClasses(String path, String dest, String name) throws IOException {
        List<ParsedClass> parsedClassList = getParsedClasses(path);

        PlantumlPainter painter = new PlantumlPainter(dest, name);
        painter.begin();
        painter.paint(parsedClassList);
        painter.end();
    }

    private static List<ParsedClass> getParsedClasses(String path) throws IOException {
        List<ParsedClass> parsedClassList = new ArrayList<>();
        Set<String> processed = new HashSet<>();

        RecursiveScanner scanner = new RecursiveScanner();

        scanner.scan(path,
                new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".java");
                    }
                },

                new FileHandler() {
                    @Override
                    public boolean handle(String path) {
                        ClassParser classParser = new ClassParser();
                        try {
                            ParsedClass clazz = classParser.parse(path);
                            System.out.println(clazz);
                            parsedClassList.add(clazz);
                            processed.add(clazz.getFullName());

                            Queue<ParsedClass> tmp = new LinkedBlockingQueue<>();
                            tmp.add(clazz);
                            while (!tmp.isEmpty()) {
                                ParsedClass currentClass = tmp.poll();
                                for (ParsedClass innerClass : currentClass.getInnerClasses()) {
                                    if (!processed.contains(innerClass.getFullName())) {
                                        parsedClassList.add(innerClass);
                                        tmp.add(innerClass);
                                    }
                                }
                            }
                        } catch (IOException e) {
                        }

                        return true;
                    }
                }
        );

        return parsedClassList;
    }
}
