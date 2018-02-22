import com.sun.source.tree.ImportTree;
import com.sun.source.util.TreeScanner;

public class ImportsScanner extends
    TreeScanner<ParsedClass, ParsedClass> {

    public ParsedClass visitImport(ImportTree node, ParsedClass parsedClass) {
        parsedClass.getImports().add(node.getQualifiedIdentifier().toString());
        return parsedClass;
    }
}
