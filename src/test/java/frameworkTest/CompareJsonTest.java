package frameworkTest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.JSONPath;
import framework.base.CompareBaseResultDTO;
import framework.factory.AbstractAiTestFramework;
import framework.utils.CompareJsonUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class CompareJsonTest extends AbstractAiTestFramework {


    @Test(dataProvider = "TestDataProvider")
    public void compareJsonObjectTest(Map<String, Object> parameter){
        JSONObject paramsObj = new JSONObject(parameter);

        // 获取参数
        JSONObject methodParameter = (JSONObject) JSONPath.eval(paramsObj,"$.parameter.jsonObjecta");

        // 方法调用,此处省略. 假设 jsonObject1 同样作为方法返回结果.

        // 获取期望结果
        JSONObject expectResult = (JSONObject) JSONPath.eval(paramsObj,"$.expectResult.expect");


        // 统一的结果比对接口, 根据配置实现即可灵活选择、过滤比对方式 及精确、模糊校验角度.
        CompareBaseResultDTO compareBaseResultDTO
            = CompareJsonUtils.compareJson(methodParameter, expectResult, paramsObj);

        // 结果断言
        Assert.assertEquals(compareBaseResultDTO.getRetCode(), 0,
            String.valueOf(compareBaseResultDTO.getRetValue()));


    }



    @Test(dataProvider = "TestDataProvider")
    public void compareJsonArrayTest(Map<String, Object> parameter) {
        JSONObject paramsObj = new JSONObject(parameter);
        // 获取参数
        JSONArray methodParameter = (JSONArray) JSONPath.eval(paramsObj,"$.parameter.JsonArrayDemo");
        // 方法调用,此处省略. 假设 jsonObject1 同样作为方法返回结果.

        // 获取期望结果
        JSONArray expectResult = (JSONArray) JSONPath.eval(paramsObj,"$.expectResult.expect");

        // 统一的结果比对接口, 根据配置实现即可灵活选择、过滤比对方式 及精确、模糊校验角度.
        CompareBaseResultDTO compareBaseResultDTO
                = CompareJsonUtils.compareJson(methodParameter, expectResult, paramsObj);

        // 结果断言
        Assert.assertEquals(compareBaseResultDTO.getRetCode(), 0,
                String.valueOf(compareBaseResultDTO.getRetValue()));

        System.out.println("比对结果：" + JSONObject.toJSONString(compareBaseResultDTO));
    }

    @Test(dataProvider = "TestDataProvider")
    public void compareStringTest(Map<String, Object> parameter) {
        JSONObject paramsObj = new JSONObject(parameter);
        // 获取参数
        String methodParameter = JSONPath.eval(paramsObj,"$.parameter.string").toString();
        // 方法调用,此处省略. 假设 jsonObject1 同样作为方法返回结果.

        // 获取期望结果
        String expectResult = JSONPath.eval(paramsObj,"$.expectResult.expect").toString();
        System.out.println(
                methodParameter+""+expectResult
        );
        // 统一的结果比对接口, 根据配置实现即可灵活选择、过滤比对方式 及精确、模糊校验角度.
        CompareBaseResultDTO compareBaseResultDTO
                = CompareJsonUtils.compareJson(methodParameter, expectResult, paramsObj);

        // 结果断言
        Assert.assertEquals(compareBaseResultDTO.getRetCode(), 0,
                String.valueOf(compareBaseResultDTO.getRetValue()));

        System.out.println("比对结果：" + JSONObject.toJSONString(compareBaseResultDTO));
    }

    @Test(dataProvider = "TestDataProvider")
    public void compareIntegerTest(Map<String, Object> parameter) {
        JSONObject paramsObj = new JSONObject(parameter);
        // 获取参数
        Integer methodParameter = Integer.parseInt(JSONPath.eval(paramsObj,"$.parameter.integer").toString());
        // 方法调用,此处省略. 假设 jsonObject1 同样作为方法返回结果.

        // 获取期望结果
        Integer expectResult = Integer.parseInt(JSONPath.eval(paramsObj,"$.expectResult.expect").toString());
        System.out.println(
                methodParameter+""+expectResult
        );
        // 统一的结果比对接口, 根据配置实现即可灵活选择、过滤比对方式 及精确、模糊校验角度.
        CompareBaseResultDTO compareBaseResultDTO
                = CompareJsonUtils.compareJson(methodParameter, expectResult, paramsObj);

        // 结果断言
        Assert.assertEquals(compareBaseResultDTO.getRetCode(), 0,
                String.valueOf(compareBaseResultDTO.getRetValue()));

        System.out.println("比对结果：" + JSONObject.toJSONString(compareBaseResultDTO));
    }

    @Test(dataProvider = "TestDataProvider")
    public void compareLongTest(Map<String, Object> parameter) {
        JSONObject paramsObj = new JSONObject(parameter);
        // 获取参数
        Long methodParameter = Long.parseLong(JSONPath.eval(paramsObj,"$.parameter.long").toString());
        // 方法调用,此处省略. 假设 jsonObject1 同样作为方法返回结果.

        // 获取期望结果
        Long expectResult = Long.parseLong(JSONPath.eval(paramsObj,"$.expectResult.expect").toString());
        System.out.println(
                methodParameter+""+expectResult
        );
        // 统一的结果比对接口, 根据配置实现即可灵活选择、过滤比对方式 及精确、模糊校验角度.
        CompareBaseResultDTO compareBaseResultDTO
                = CompareJsonUtils.compareJson(methodParameter, expectResult, paramsObj);

        // 结果断言
        Assert.assertEquals(compareBaseResultDTO.getRetCode(), 0,
                String.valueOf(compareBaseResultDTO.getRetValue()));

        System.out.println("比对结果：" + JSONObject.toJSONString(compareBaseResultDTO));
    }

    @Test(dataProvider = "TestDataProvider")
    public void compareLongCase(Map<String, Object> parameter) {
        JSONObject paramsObj = new JSONObject(parameter);
        // 获取参数
        Long methodParameter = Long.parseLong(JSONPath.eval(paramsObj,"$.parameter.long").toString());
        // 方法调用,此处省略. 假设 jsonObject1 同样作为方法返回结果.

        // 获取期望结果
        Long expectResult = Long.parseLong(JSONPath.eval(paramsObj,"$.expectResult.expect").toString());
        System.out.println(
                methodParameter+""+expectResult
        );
        // 统一的结果比对接口, 根据配置实现即可灵活选择、过滤比对方式 及精确、模糊校验角度.
        CompareBaseResultDTO compareBaseResultDTO
                = CompareJsonUtils.compareJson(methodParameter, expectResult, paramsObj);

        // 结果断言
        Assert.assertEquals(compareBaseResultDTO.getRetCode(), 0,
                String.valueOf(compareBaseResultDTO.getRetValue()));

        System.out.println("比对结果：" + JSONObject.toJSONString(compareBaseResultDTO));
    }

}
