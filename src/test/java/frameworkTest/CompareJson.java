package frameworkTest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.jayway.jsonpath.JsonPath;
import framework.factory.AbstractAiTestFramework;
import framework.utils.CompareBaseResult;
import framework.utils.CompareJsonUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class CompareJson extends AbstractAiTestFramework {


    @Test(dataProvider = "TestDataProvider")
    public void compareJsonObjectTest(Map<String, Object> parameter){
        JSONObject paramsObj = new JSONObject(parameter);

        // 获取参数
        JSONObject methodParameter = JsonPath.parse(parameter).read("$.parameter.jsonObjecta");

        // 方法调用,此处省略. 假设 jsonObject1 同样作为方法返回结果.


        // 获取期望结果
        JSONObject expectResult = JsonPath.parse(parameter).read("$.expectResult.expect");


        // 统一的结果比对接口, 根据配置实现即可灵活选择、过滤比对方式 及精确、模糊校验角度.
        CompareBaseResult compareBaseResult
            = CompareJsonUtils.compareJson(methodParameter, expectResult, paramsObj);

        // 结果断言
        Assert.assertEquals(compareBaseResult.getRetCode(), 0,
            String.valueOf(compareBaseResult.getRetValue()));

    }



    @Test(dataProvider = "TestDataProvider")
    public void compareJsonArrayTest(Map<String, Object> parameter) {
        JSONObject paramsObj = new JSONObject(parameter);

        // JSONArray 对象
        String jsonArrayA = JSON.toJSONString(paramsObj.get("JsonArrayA"));
        String jsonArrayB = JSON.toJSONString(paramsObj.get("JsonArrayB"));

        JSONArray jsonArray1 = JSONArray.parseArray(jsonArrayA);
        JSONArray jsonArray2 = JSONArray.parseArray(jsonArrayB);

        CompareBaseResult compareBaseResult
            = CompareJsonUtils.compareJson(jsonArray1, jsonArray2,false);
        System.out.println("比对结果：" + JSONObject.toJSONString(compareBaseResult));
    }

    @Test(dataProvider = "TestDataProvider")
    public void compareStringTest(Map<String, Object> parameter) {
        // String
        JSONObject paramsObj = new JSONObject(parameter);

        String stringA = JSON.toJSONString(paramsObj.get("stringA"));
        String stringB = JSON.toJSONString(paramsObj.get("stringB"));


        CompareBaseResult compareBaseResult
            = CompareJsonUtils.compareJson(stringA, stringB,true);
        System.out.println("比对结果：" + JSONObject.toJSONString(compareBaseResult));
    }

    @Test(dataProvider = "TestDataProvider")
    public void compareIntegerTest(Map<String, Object> parameter) {
        // Integer
        Integer integerA = 9527;
        Integer integerB = 9521;
        CompareBaseResult compareBaseResult
            = CompareJsonUtils.compareJson(integerA, integerB,true);

        System.out.println("比对结果：" + JSONObject.toJSONString(compareBaseResult));
    }

    @Test(dataProvider = "TestDataProvider")
    public void compareLongTest(Map<String, Object> parameter) {
        // Long
        Long LongA = 9527L;
        Long LongB = 9521L;
        CompareBaseResult compareBaseResult
            = CompareJsonUtils.compareJson(LongA, LongB,true);
        System.out.println("比对结果：" + JSONObject.toJSONString(compareBaseResult));
    }
}
