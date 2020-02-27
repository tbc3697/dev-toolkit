package pub.tbc.dev.util;

import pub.tbc.dev.util.test.P;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 扫描代码行数
 *
 * @Author tbc on 2019-08-22 10:41
 */
public class CodeLinesScanner {
    private static final String SCAN_PATH = "scan.path";

    static {
        String scanPath = System.getProperty(SCAN_PATH);
        System.setProperty(SCAN_PATH,
                scanPath == null
                        ? CodeLinesScanner.class.getResource("").getPath().replace("target/classes", "src/main/java")
                        : scanPath);
    }

    private static Optional<String> getScanPath(String[] args) {
        return Optional.of(Arrays.stream(args).findFirst().orElse(System.getProperty(SCAN_PATH)));
    }

    private static List<File> getFileListWithEndName(File file, String n) {
        return getFileList(file, f -> f.getPath().endsWith(n));
    }

    private static List<File> getFileList(File file, Predicate<File> fileFilter) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            List<File> list = new ArrayList<>();
            if (children != null && children.length != 0) {
                for (File f : children) {
                    List<File> files = getFileList(f, fileFilter);
                    if (files != null) {
                        list.addAll(files);
                    }
                }
            }
            return list;
        }
        // @formatter:off
        return fileFilter.test(file) ? new ArrayList() {{add(file);}} : null;
        // @formatter:on
    }

    private static long computeCodeLineNum(File file) {
        return computeCodeLineNum(file, Objects::nonNull);
    }

    private static long computeCodeLineNum(File file, Predicate<String> filter) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            return bufferedReader.lines().filter(filter).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Optional<String> scanPathOp = getScanPath(args);
        scanPathOp.ifPresent(scanPath -> {
            P.println("待扫描文件（目录）：{}", scanPath);

            File file = new File(scanPath);
            List<File> files = getFileList(file, f -> !f.getPath().contains("test") && f.getName().endsWith(".java"));
            P.println("java文件数量：{}", files == null ? 0 : files.size());
            if (files == null) {
                return;
            }

            long lineNum = files.stream().mapToLong(CodeLinesScanner::computeCodeLineNum).sum();
            P.println("java代码行数：{}", lineNum);


            List<String> filterKeywords = Arrays.stream(new String[]{"/", "*", "import", "package"}).collect(Collectors.toList());
            Predicate<String> contentFilter = str -> {
                if (str != null && !str.isEmpty()) {
                    str = str.replace(" ", "");
                    for (String s : filterKeywords) {
                        if (str.startsWith(s)) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            };
            long codeLineNum = files.stream().mapToLong(f -> computeCodeLineNum(f, contentFilter)).sum();
            P.println("去除[空行、注释、import、package]后有效代码行数：{}", codeLineNum);
        });
    }
}
