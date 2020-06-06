package framework.utils;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @program: wady
 * @description: TODO
 * @author: YJiang（叶闲）
 * @create: 2020-01-31 17:17
 */
public class GetFilesUtils {

    /**
     * 遍历路径获得对应的文件列表 ： 正则表达式对应的文件名称
     *
     * @param path
     * @param regexFileNames 正则表达式对应的文件名称 带扩展名的
     */
    public static List<String> traverse(String path, List<String> regexFileNames) {

        List<String> pathFileNames = new ArrayList<String>();
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    list.add(file2);
                } else {

                    for (String regexFileName : regexFileNames) {
                        if (Pattern.matches(regexFileName, file2.getName())) {
                            pathFileNames.add(file2.getAbsolutePath());
                            break;
                        }
                    }

                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        list.add(file2);
                    } else {
                        for (String regexFileName : regexFileNames) {
                            if (Pattern.matches(regexFileName, file2.getName())) {
                                pathFileNames.add(file2.getAbsolutePath());
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException(MessageFormat.format("路径path={0}对应的文件夹不存在", path));
        }
        return pathFileNames;
    }

}
