package framework.utils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;


/**
 * @program: interface-test-tools
 * @description: TODO
 * @author: YJiang（叶闲）
 * @create: 2020-03-30 00:17
 */
public class CreatFile {

    /**
     * *
     * @Description: 判断文件是否存在, 不存在则创建
     * @Param: [file]
     * @return: void
     * @Author: YJiang(叶闲)
     * @DateTimeFormat: 2020/3/30
     */
    public static void checkFileExistsToCreate(File file) {

        String fileParentPath = file.getParent();

        File filepath = new File(fileParentPath);
        if (!filepath.exists()) {
            filepath.mkdirs();
            System.out.println(MessageFormat.format("Create Yaml filepath {0}",
                fileParentPath));
        } else {
            System.out.println(MessageFormat.format("Exist Yaml filepath {0}",
                fileParentPath));
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("Create YamlFile {0}",
                file.getAbsolutePath()));
        } else {
            System.out.println(MessageFormat.format("Exist YamlFile {0}",
                file.getAbsolutePath()));
        }

    }

}
