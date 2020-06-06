package framework.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @program: interface-test-tools
 * @description: TODO
 * @author:
 * @create: 2020-03-07 13:04
 */
public class TypeConversion {

    /**
     * *
     *
     * @Description: 转换JavaBean对象
     * @Param: [object, clazz]
     * @return: T
     * @Author:
     * @DateTimeFormat: 2020/3/7
     */
    public static <T> T stringToJavaBean(Object object, Class<T> clazz) {

        String string = JSONObject.toJSONString(object);
        JSONObject json = JSONObject.parseObject(string);

        return JSONObject.toJavaObject(json, clazz);
    }
}
