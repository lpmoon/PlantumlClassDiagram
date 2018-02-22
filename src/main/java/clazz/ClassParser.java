package clazz;

import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.parser.Parser;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class ClassParser {
    private ParserFactory factory;

    public ClassParser() {
        factory = getParserFactory();
    }

    public ParsedClass parse(String file) throws IOException {
        ParsedClass parsedClass = new ParsedClass();
        JCTree.JCCompilationUnit unit = innerParse(file);

        if (unit.getPackageName() != null) {
            parsedClass.setPackagePath(unit.getPackageName().toString());
        }

        ClassScanner scanner = new ClassScanner();
        scanner.visitCompilationUnit(unit, parsedClass);

        ImportsScanner scanner1 = new ImportsScanner();
        scanner1.visitCompilationUnit(unit, parsedClass);

        return parsedClass;
    }

    private JCTree.JCCompilationUnit innerParse(String file) throws IOException {
        Parser parser = factory.newParser(readFile(file), true, false, true);
        return parser.parseCompilationUnit();
    }

    private ParserFactory getParserFactory() {
        Context context = new Context();
        JavacFileManager.preRegister(context);
        ParserFactory factory = ParserFactory.instance(context);
        return factory;
    }

    private CharSequence readFile(String file) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        FileChannel ch = fin.getChannel();
        ByteBuffer buffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, ch.size());
        return Charset.defaultCharset().decode(buffer);
    }
}
