import clazz.ClassParser;
import clazz.ParsedClass;
import file.FileHandler;
import file.RecursiveScanner;
import graph.Graph;
import org.apache.commons.cli.*;
import plantuml.PlantumlPainter;
import util.StringUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws IOException {
        CommandLineParser parser = new BasicParser( );
        Options options = new Options( );
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("s", "src", true, "Source folder");
        options.addOption("d", "dest", true, "Destination folder");
        options.addOption("n", "name", true, "Name of generated plantuml file");
        options.addOption("c", "class", true, "Destination class");

        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        if (commandLine.hasOption("h")) {
            StringBuilder sb = new StringBuilder();
            sb.append("-h help  # Print this usage information\n");
            sb.append("-s src   # Source folder\n");
            sb.append("-d dest  # Destination folder\n");
            sb.append("-n name  # Name of generated plantuml file\n");
            sb.append("-c class # Destination class\n");
            System.out.println(sb.toString());
            return;
        }

        String srcFolder = commandLine.getOptionValue("s");
        String destFolder = commandLine.getOptionValue("d");
        String name = commandLine.getOptionValue("n");
        String clazz = commandLine.getOptionValue("c");

        if (StringUtil.isBlank(srcFolder) || StringUtil.isBlank(destFolder) || StringUtil.isBlank(name)) {
            System.err.println("Notice: -s -d -n should not be empty!!");
            return;
        }

        Main main = new Main();
        if (StringUtil.isBlank(clazz)) {
            main.drawAllClasses(srcFolder, destFolder, name);
        } else {
            main.drawAssociated(srcFolder, destFolder, name, clazz);
        }
    }

    public void drawAllClasses(String path, String dest, String name) throws IOException {
        List<ParsedClass> parsedClassList = getParsedClasses(path);

        PlantumlPainter painter = new PlantumlPainter(dest, name);
        painter.begin();
        painter.paint(parsedClassList);
        painter.end();
    }

    public void drawAssociated(String path, String dest, String name, String clazz) throws IOException {
        List<ParsedClass> parsedClassList = getParsedClasses(path);

        Graph<ParsedClass> graph = new Graph<>();

        for (ParsedClass currentClass : parsedClassList) {
            for (String implementClass : currentClass.getImplementsClasses()) {
                String fullImplementClass = currentClass.getFullClass(implementClass);
                ParsedClass parsedClass = queryParsedClass(fullImplementClass, parsedClassList);
                if (parsedClass == null) {
                    continue;
                }

                graph.connect(currentClass, parsedClass);
            }

            for (String extendClass : currentClass.getExtendsClasses()) {
                String fullExtendClass = currentClass.getFullClass(extendClass);
                ParsedClass parsedClass = queryParsedClass(fullExtendClass, parsedClassList);
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

                ParsedClass parsedClass = queryParsedClass(dependencyClass, parsedClassList);
                if (parsedClass == null) {
                    continue;
                }

                graph.connect(currentClass, parsedClass);
            }
        }

        List<ParsedClass> shouldPainted = new ArrayList<>();

        ParsedClass centerClass = queryParsedClass(clazz, parsedClassList);
        if (centerClass == null) {
            return;
        }

        shouldPainted.addAll(graph.findConnectedNodes(centerClass));

        PlantumlPainter painter = new PlantumlPainter(dest, name);
        painter.begin();
        painter.paint(shouldPainted);
        painter.end();

        return;
    }

    private ParsedClass queryParsedClass(String clazz, List<ParsedClass> parsedClassList) {
        for (ParsedClass parsedClass : parsedClassList) {
            if (parsedClass.getFullName().equals(clazz)) {
                return parsedClass;
            }
        }

        return null;
    }

    private List<ParsedClass> getParsedClasses(String path) throws IOException {
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
