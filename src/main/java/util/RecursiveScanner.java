package util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class RecursiveScanner {

    public void scan(String path, FilenameFilter filenameFilter, FileHandler handler) {
        File root = new File(path);
        Queue<File> files = new LinkedBlockingQueue<>();
        files.add(root);

        while (!files.isEmpty()) {
            File cur = files.poll();
            File[] subFiles = cur.listFiles();
            for (File subFile : subFiles) {
                if (subFile.isDirectory()) {
                    files.add(subFile);
                    continue;
                }

                if (subFile.isFile() && filenameFilter.accept(cur, subFile.getName())) {
                    handler.handle(subFile.getAbsolutePath());
                }
            }
        }
    }
}
