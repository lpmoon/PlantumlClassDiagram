package com.lpmoon.plantuml.classdiagram.main;

import com.lpmoon.plantuml.classdiagram.clazz.ClassParser;
import com.lpmoon.plantuml.classdiagram.clazz.ParsedClass;
import com.lpmoon.plantuml.classdiagram.file.FileHandler;
import com.lpmoon.plantuml.classdiagram.file.RecursiveScanner;
import com.lpmoon.plantuml.classdiagram.graph.Graph;
import org.apache.commons.cli.*;
import com.lpmoon.plantuml.classdiagram.plantuml.PlantumlPainter;
import com.lpmoon.plantuml.classdiagram.util.StringUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException {
        CommandLineParser parser = new BasicParser( );
        Options options = new Options( );
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("s", "src", true, "Source folder");
        options.addOption("d", "dest", true, "Destination folder");
        options.addOption("n", "name", true, "Name of generated com.lpmoon.plantuml.classdiagram.plantuml com.lpmoon.com.lpmoon.plantuml.classdiagram.plantuml.classdiagram.file");
        options.addOption("c", "class", true, "Destination class");
        options.addOption("sub", "sub", false, "Destination class");

        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        if (commandLine.hasOption("h")) {
            String separator = System.lineSeparator();
            StringBuilder sb = new StringBuilder();
            sb.append("-h help  # Print this usage information").append(separator);
            sb.append("-s src   # Source folder").append(separator);
            sb.append("-d dest  # Destination folder").append(separator);
            sb.append("-n name  # Name of generated com.lpmoon.plantuml.classdiagram.plantuml com.lpmoon.com.lpmoon.plantuml.classdiagram.plantuml.classdiagram.file").append(separator);
            sb.append("-c class # Destination class").append(separator);
            sb.append("-sub     # Sub").append(separator);
            System.out.println(sb.toString());
            return;
        }

        String srcFolder = commandLine.getOptionValue("s");
        String destFolder = commandLine.getOptionValue("d");
        String name = commandLine.getOptionValue("n");
        String clazz = commandLine.getOptionValue("c");
        boolean sub = commandLine.hasOption("sub");

        if (StringUtil.isBlank(srcFolder) || StringUtil.isBlank(destFolder) || StringUtil.isBlank(name)) {
            System.err.println("Notice: -s -d -n should not be empty!!");
            return;
        }

        Main main = new Main();
        if (StringUtil.isBlank(clazz)) {
            main.drawAllClasses(srcFolder, destFolder, name);
        } else if (!sub){
            main.drawAssociated(srcFolder, destFolder, name, clazz);
        } else {
            main.drawImplementsOrExtends(srcFolder, destFolder, name, clazz);
        }
    }

    public void drawAllClasses(String path, String dest, String name) throws IOException {
        Map<String, ParsedClass> parsedClassMap = getParsedClasses(path);

        PlantumlPainter painter = new PlantumlPainter(dest, name, parsedClassMap, false);
        painter.begin();
        painter.paint();
        painter.end();
    }

    public void drawImplementsOrExtends(String path, String dest, String name, String clazz) throws IOException {
        Map<String, ParsedClass> parsedClassMap = getParsedClasses(path);

        Graph<ParsedClass> graph = new Graph<>();

        for (ParsedClass currentClass : parsedClassMap.values()) {
            graph.add(currentClass);

            for (String implementClass : currentClass.getImplementsClasses()) {
                String fullImplementClass = currentClass.getFullClass(implementClass);
                ParsedClass parsedClass = parsedClassMap.get(fullImplementClass);
                if (parsedClass == null) {
                    continue;
                }

                graph.connect(currentClass, parsedClass);
            }

            for (String extendClass : currentClass.getExtendsClasses()) {
                String fullExtendClass = currentClass.getFullClass(extendClass);
                ParsedClass parsedClass = parsedClassMap.get(fullExtendClass);
                if (parsedClass == null) {
                    continue;
                }

                graph.connect(currentClass, parsedClass);
            }
        }

        List<ParsedClass> shouldPainted = new ArrayList<>();

        ParsedClass centerClass = parsedClassMap.get(clazz);
        if (centerClass == null) {
            return;
        }

        shouldPainted.addAll(graph.findConnectedNodes(centerClass, true, false));

        PlantumlPainter painter = new PlantumlPainter(dest, name, transform(shouldPainted), true);
        painter.begin();
        painter.paint();
        painter.end();

        return;
    }

    public Map<String, ParsedClass> transform(List<ParsedClass> parsedClassList) {
        Map<String, ParsedClass> map = new HashMap<>();
        for (ParsedClass parsedClass : parsedClassList) {
            map.put(parsedClass.getFullName(), parsedClass);
        }

        return map;
    }

    public void drawAssociated(String path, String dest, String name, String clazz) throws IOException {
        Map<String, ParsedClass> parsedClassMap = getParsedClasses(path);

        Graph<ParsedClass> graph = new Graph<>();

        for (ParsedClass currentClass : parsedClassMap.values()) {
            graph.add(currentClass);

            for (String implementClass : currentClass.getImplementsClasses()) {
                String fullImplementClass = currentClass.getFullClass(implementClass);
                ParsedClass parsedClass = parsedClassMap.get(fullImplementClass);
                if (parsedClass == null) {
                    continue;
                }

                graph.connect(currentClass, parsedClass);
            }

            for (String extendClass : currentClass.getExtendsClasses()) {
                String fullExtendClass = currentClass.getFullClass(extendClass);
                ParsedClass parsedClass = parsedClassMap.get(fullExtendClass);
                if (parsedClass == null) {
                    continue;
                }

                graph.connect(currentClass, parsedClass);
            }

            for (Map.Entry entry : currentClass.getMembers().entrySet()) {
                String dependencyClass = currentClass.getFullClass((String) entry.getValue());
                if (dependencyClass.equals(currentClass.getFullName())) {
                    continue;
                }

                ParsedClass parsedClass = parsedClassMap.get(dependencyClass);
                if (parsedClass == null) {
                    continue;
                }

                graph.connect(currentClass, parsedClass);
            }
        }

        List<ParsedClass> shouldPainted = new ArrayList<>();

        ParsedClass centerClass = parsedClassMap.get(clazz);
        if (centerClass == null) {
            return;
        }

        shouldPainted.addAll(graph.findConnectedNodes(centerClass, true, true));

        PlantumlPainter painter = new PlantumlPainter(dest, name, transform(shouldPainted), false);
        painter.begin();
        painter.paint();
        painter.end();

        return;
    }

    private Map<String, ParsedClass> getParsedClasses(String path) throws IOException {
        Map<String, ParsedClass> parsedClassMap = new ConcurrentHashMap<>();
        RecursiveScanner scanner = new RecursiveScanner();
        ExecutorService e = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future> futures = new ArrayList<>();

        try {
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

                            Future f = e.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        System.out.println(path);
                                        ClassParser classParser = new ClassParser();
                                        ParsedClass clazz = classParser.parse(path);
                                        parsedClassMap.put(clazz.getFullName(), clazz);

                                        System.out.println(clazz);

                                        Queue<ParsedClass> tmp = new LinkedBlockingQueue<>();
                                        tmp.add(clazz);
                                        while (!tmp.isEmpty()) {
                                            ParsedClass currentClass = tmp.poll();
                                            for (ParsedClass innerClass : currentClass.getInnerClasses()) {
                                                parsedClassMap.put(innerClass.getFullName(), innerClass);
                                                tmp.add(innerClass);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            futures.add(f);

                            return true;
                        }
                    }
            );

            for (Future f : futures) {
                try {
                    f.get();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e1) {
        } finally {
            e.shutdown();
        }

        for (ParsedClass currentClass : parsedClassMap.values()) {
            addNonExistedClass(currentClass.getImplementsClasses(), currentClass, parsedClassMap);
            addNonExistedClass(currentClass.getExtendsClasses(), currentClass, parsedClassMap);
            addNonExistedClass(new ArrayList<>(currentClass.getMembers().values()), currentClass, parsedClassMap);
        }

        return parsedClassMap;
    }

    public void addNonExistedClass(List<String> classes, ParsedClass currentClass, Map<String, ParsedClass> parsedClassMap) {
        for (String clazz : classes) {
            String fullClassName = currentClass.getFullClass(clazz);
            ParsedClass parsedClass = parsedClassMap.get(fullClassName);
            if (parsedClass == null) {
                ParsedClass notExistedClass = new ParsedClass();
                notExistedClass.setName(fullClassName);
                parsedClassMap.put(fullClassName, notExistedClass);
                System.err.println("Class " + fullClassName + " not exists!!");
            }
        }
    }
}
