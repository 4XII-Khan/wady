package framework.factory;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import framework.utils.CreateYamlDemo;
import framework.utils.GetFilesUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import static framework.utils.Parameterization.parameterComposition;

/**
 * @program: wady
 * @description: 数据提供者工厂类
 * @author: YJiang（叶闲）
 * @create: 2020-01-31 17:02
 */

public class DataProviderFactory {

    /**
     * *
     *
     * @Description: 测试用例的相对路径
     * @Param:
     * @return:
     * @Author: YJiang(叶闲)
     * @DateTimeFormat: 2020/1/31
     */
    private static String USER_CASE_DATA_PATH = "src/test/yaml/";

    /**
     * *
     *
     * @Description: 组装dataProvider，以配置块为单位，单个配置块作为一次数据驱动，二维数组中一个元素为一个配置块。
     * @Param: [testClass, method]
     * @return: java.lang.Object[][]
     * @Author: YJiang(叶闲)
     * @DateTimeFormat: 2020/1/31
     */
    public static Object[][] assembleDataProvider(Class<?> testClass, Method method) {

        // 获取所有的用例文件
        List<String> useCaseFileNames = extractUseCaseFileNames(testClass, method);
        if (useCaseFileNames.size() < 1) {
            throw new RuntimeException(MessageFormat.format("测试数据缺失, 测试类className={0},测试方法 methodName={1}",
                testClass.getName(), method.getName()));
        }
        Yaml yaml = new Yaml();
        // 各yaml文件中所有yaml配置块列表
        List<Object> yamlBlocks = new ArrayList<Object>();
        // 遍历所有yaml文件
        for (String useCaseFileName : useCaseFileNames) {
            try {
                File file = new File(useCaseFileName);
                if (file.exists()) {
                    Iterable<Object> objIterable = yaml.loadAll(new FileInputStream(file));
                    for (Object object : objIterable) {
                        String objectString = JSON.toJSONString(object);
                        JSONObject jsonObject = JSONObject.parseObject(objectString);
                        if (jsonObject.containsKey("parameterization")){
                            ArrayList<JSONObject> jsonObjectArrayList = parameterComposition(jsonObject);
                            yamlBlocks.addAll(jsonObjectArrayList);
                        }else {
                            yamlBlocks.add(jsonObject);
                        }
                    }
                } else {
                    // 不存在则创建，提高测试数据文件创建效率，避免手动创建
                    CreateYamlDemo.demo(useCaseFileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 定义一个二维数组，长度为配置块个数，一个配置块在二维数组中作为一个元素
        Object[][] result = new Object[yamlBlocks.size()][];
        // 填充二维数组
        for (int n = 0; n < yamlBlocks.size(); n++) {
            List<Object> tmp = new ArrayList<Object>();
            tmp.add(yamlBlocks.get(n));
            result[n] = tmp.toArray();
        }

        return result;
    }

    /**
     * *
     *
     * @Description: 支持Aitest注解和非注解两种模式
     * @Param: [testClass, method] 用例class对象,用例对象
     * @return: java.util.List<java.lang.String> 返回对应的测试文件名列表
     * @Author: YJiang(叶闲)
     * @DateTimeFormat: 2020/1/31
     */
    private static List<String> extractUseCaseFileNames(Class<?> testClass, Method method) {

        // 返回该程序元素上存在的、指定类型的注解数组。没有注解对应类型的注解时，返回长度为0的数组。
        // 若程序元素为类，当前类上找不到注解，且该注解为可继承的，则会去父类上检测对应的注解。
        AiTest[] classAiTests = testClass.getAnnotationsByType(AiTest.class);

        AiTest[] methodAiTests = method.getAnnotationsByType(AiTest.class);

        System.out.println(MessageFormat.format("Running {0}#{1}",
            testClass.getName(), method.getName()));

        boolean ifExistMoreAnnotation = (classAiTests.length > 1) || (methodAiTests.length > 1);
        if (ifExistMoreAnnotation) {
            throw new RuntimeException(
                MessageFormat.format("测试类 className={0}或者测试方法 methodName={1}对应的AiTest注解个数不等于1",
                    testClass.getName(), method.getName()));
        }

        // 获取当前工程路径，风险：在哪里执行java命令，user.dir返回的就是哪里，出现classnotfound
        String currentUserPath = System.getProperty("user.dir");
        // 拼接数据绝对目录路径
        String absoluteCasePath = currentUserPath + File.separator + USER_CASE_DATA_PATH;

        String testOnly = "";

        if (classAiTests.length != 0 || methodAiTests.length != 0) {
            // 类注解不为空
            if (classAiTests.length != 0 && methodAiTests.length == 0) {
                if (StringUtils.isEmpty(classAiTests[0].yamlPath())) {
                    throw new RuntimeException(
                        MessageFormat.format(
                            "方法 methodName={1}无Aitest注解，且测试类 "
                                + "className={0}对应的AiTest注解缺失relatePath参数，你可以增加测试类中注解参数或者为方法增加注解，并且明确参数",
                            testClass.getName(), method.getName()));
                }
                // 测试数据绝对文件路径-yaml路径
                absoluteCasePath += classAiTests[0].yamlPath().trim();

                System.out.println(MessageFormat.format("Yaml Path {0}",
                    absoluteCasePath));

                // 指定使用yaml文件
                if (!StringUtils.isEmpty(classAiTests[0].onlyYaml())) {
                    testOnly = classAiTests[0].onlyYaml().trim();
                }
            } else if (classAiTests.length != 0) {
                // 存在方法注解时，则使用方法注解
                if (!StringUtils.isEmpty(methodAiTests[0].yamlPath())) {
                    absoluteCasePath += methodAiTests[0].yamlPath().trim();
                } else {
                    absoluteCasePath += classAiTests[0].yamlPath().trim();
                }
                if (!StringUtils.isEmpty(methodAiTests[0].onlyYaml())) {
                    testOnly = methodAiTests[0].onlyYaml().trim();
                } else {
                    testOnly = classAiTests[0].onlyYaml().trim();
                }
                System.out.println(MessageFormat.format("Yaml Path {0}",
                    absoluteCasePath));
            } else {
                // if (classAiTests.length == 0 && methodAiTests.length != 0)
                if (StringUtils.isEmpty(methodAiTests[0].yamlPath())) {
                    // 该异常作为日志记录并且入库
                    throw new RuntimeException(
                        MessageFormat.format(
                            "测试类 且方法className={0}无Aitest注解，且方法 methodName={1}对应的AiTest注解缺失relatePath参数，你可以增加方法注解中参数",
                            testClass.getName(), method.getName()));
                }
                absoluteCasePath += methodAiTests[0].yamlPath().trim();
                System.out.println(MessageFormat.format("Yaml Path {0}",
                    absoluteCasePath));
                if (!StringUtils.isEmpty(methodAiTests[0].onlyYaml())) {
                    testOnly = methodAiTests[0].onlyYaml().trim();
                }
            }
            String allFile = "*";
            if (testOnly.isEmpty() || allFile.equals(testOnly)) {
                testOnly = ".*";
            } else {
                // 正则表达式替换，对于出现*的则替换成.*,出现.*的则不进行替换
                testOnly = testOnly.replace("*", ".*");
            }

            List<String> testOnlyFileNames = new ArrayList<String>();
            testOnlyFileNames.add(testOnly + ".yaml");
            // 获得对应的路径上的文件名和路径列表
            return GetFilesUtils.traverse(absoluteCasePath, testOnlyFileNames);

        } else {
            // 无 Aitest注解时
            String[] className = testClass.getName().split("\\.");

            // 拼接数据绝对目录路径
            absoluteCasePath = currentUserPath + File.separator + USER_CASE_DATA_PATH + className[className.length - 1]
                + File.separator + method.getName() + ".yaml";

            System.out.println(MessageFormat.format("Yaml Path {0}",
                absoluteCasePath));

            List<String> testOnlyFileNames = new ArrayList<String>();
            testOnlyFileNames.add(absoluteCasePath);

            return testOnlyFileNames;

        }

    }
}
