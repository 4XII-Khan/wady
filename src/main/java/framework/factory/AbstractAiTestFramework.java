package framework.factory;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

/**
 * @program: wady
 * @description: TODO
 * @author: YJiang
 * @create: 2020-01-31 16:59
 */
public abstract class AbstractAiTestFramework  {

    /**
     * 定义一个数据驱动类
     *
     * @return
     */
    @DataProvider(name = "TestDataProvider")
    public Object[][] getTestData(Method method) {
        // 利用反射获取类/方法的注解，获取测试数据，进行测试数据装配
        return DataProviderFactory.assembleDataProvider(this.getClass(), method);
    }

}

