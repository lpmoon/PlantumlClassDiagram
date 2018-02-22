import clazz.ClassParser;
import clazz.ParsedClass;
import plantuml.PlantumlPainter;
import util.FileHandler;
import util.RecursiveScanner;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        List<ParsedClass> parsedClassList = new ArrayList<>();

        RecursiveScanner scanner = new RecursiveScanner();
        scanner.scan("src/test/java",
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
                            parsedClassList.add(clazz);
                        } catch (IOException e) {
                        }

                        return true;
                    }
                }
        );

        PlantumlPainter painter = new PlantumlPainter();
        painter.paint("src/test/", "tmp", parsedClassList);
    }
}
