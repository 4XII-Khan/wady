package framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.yaml.snakeyaml.Yaml;

/**
 * @program: interface-test-tools
 * @description: TODO
 * @author:
 * @create: 2020-04-04 14:47
 */
public class LoadYaml {

    public static Map<String, Object> load(String datdFilePath) {

        Yaml yaml = new Yaml();

        // 各yaml文件中所有yaml配置块列表
        List<Object> listYamlBlocks = new ArrayList<Object>();

        try {
            File file = new File(datdFilePath);

            Iterable<Object> objIterable
                = yaml.loadAll(new FileInputStream(file));

            for (Object object : objIterable) {
                listYamlBlocks.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> yamlMap
            = TypeConversion.stringToJavaBean(
            listYamlBlocks.get(
                new Random().nextInt(listYamlBlocks.size())),
            Map.class);

        return yamlMap;
    }

}
