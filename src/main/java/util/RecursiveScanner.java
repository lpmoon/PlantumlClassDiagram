package util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class RecursiveScanner {

    public void scan(String path, FilenameFilter filenameFilter, FileHandler handler) throws IOException {
        File root = new File(path);
        Queue<File> files = new LinkedBlockingQueue<>();
        files.add(root);

        Set<String> processed = new HashSet<>();
        // 处理软链接
        processed.add(root.getCanonicalPath());

        while (!files.isEmpty()) {
            File cur = files.poll();
            File[] subFiles = cur.listFiles();
            for (File subFile : subFiles) {
                if (subFile.isDirectory()) {
                    if (!processed.contains(subFile.getCanonicalPath())) {
                        files.add(subFile);
                        processed.add(subFile.getCanonicalPath());
                        continue;
                    }
                }

                if (subFile.isFile() && filenameFilter.accept(cur, subFile.getName())) {
                    handler.handle(subFile.getAbsolutePath());
                }
            }
        }
    }
}
