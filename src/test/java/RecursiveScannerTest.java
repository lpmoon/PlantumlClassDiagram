import com.lpmoon.plantuml.classdiagram.file.FileHandler;
import com.lpmoon.plantuml.classdiagram.file.RecursiveScanner;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class RecursiveScannerTest {

    @Test
    public void test() throws IOException {
        RecursiveScanner scanner = new RecursiveScanner();

        AtomicInteger c = new AtomicInteger();
        scanner.scan("src/test/tmp", new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("tmp");
            }
        }, new FileHandler() {
            @Override
            public boolean handle(String path) {
                c.incrementAndGet();
                return true;
            }
        });

        Assert.assertEquals(c.get(), 2);
    }
}
