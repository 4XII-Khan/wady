package framework.utils;
import com.alibaba.fastjson.JSONObject;

/**
 * @program: wady
 * @description: TODO
 * @author:
 * @create: 2020-03-07 13:04
 */
public class TypeConversion {

    public static <T> T stringToJavaBean(Object object, Class<T> clazz) {

        String string = JSONObject.toJSONString(object);
        JSONObject json = JSONObject.parseObject(string);

        return JSONObject.toJavaObject(json, clazz);
    }
}
