package framework.factory;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Retention(RUNTIME) // 执行时加载到JVM中
@Target({TYPE, METHOD}) //只能用在类 接口 枚举类型和方法声明中
public @interface AiTest {

    /**
     * *
     *
     * @Description: 测试文件的相对路径
     * @Param: []
     * @return: java.lang.String
     * @Author: YJiang
     * @DateTimeFormat: 2020/1/31
     */
    public String yamlPath() default "";

    /**
     * *
     *
     * @Description: 仅仅测试某个测试用例
     * @Param: []
     * @return: java.lang.String
     * @Author: YJiang
     * @DateTimeFormat: 2020/1/31
     */
    public String onlyYaml() default "";

    /**
     * *
     *
     * @Description: 描述 ，暂不使用
     * @Param: []
     * @return: java.lang.String
     * @Author: YJiang
     * @DateTimeFormat: 2020/1/31
     */
    public String desc() default "";

    /**
     * *
     *
     * @Description: 版本号，更新版本 暂不使用
     * @Param: []
     * @return: java.lang.String
     * @Author: YJiang
     * @DateTimeFormat: 2020/2/1
     */
    public String version() default "";
}


