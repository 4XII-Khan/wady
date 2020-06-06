package framework.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import org.apache.commons.lang3.StringUtils;

import static framework.utils.ResultCheckWay.checkWay;

public class CompareJsonUtils {

    private static CompareBaseResult compareBaseResult;

    public static CompareBaseResult getBaseResult() {
        return compareBaseResult;
    }

    public static void setBaseResult(CompareBaseResult compareBaseResult) {
        CompareJsonUtils.compareBaseResult = compareBaseResult;

    }

    /**
     * @Description: 更新结果，错误数量计数
     * @Param: [parameter]
     * @Author: YJiang
     * @DateTimeFormat: 2020/3/8
     */
    private static void updateBaseResult(ResultDetail resultDetail) {

        // 错误数量统计
        compareBaseResult.setRetCode(compareBaseResult.getRetCode() + 1);

        // 填充 RetValue 错误内容
        if (compareBaseResult.getRetValue() == null) {
            ArrayList<ResultDetail> resultDetailArrayList = new ArrayList<ResultDetail>();
            resultDetailArrayList.add(resultDetail);
            compareBaseResult.setRetValue(resultDetailArrayList);
        } else {
            compareBaseResult.getRetValue().add(resultDetail);
        }
        System.out.println(
            "\033[1;31m" + "【fail】Key:" + resultDetail.getKey() + "\n     actualJson:" + resultDetail.getActual()
                + "\n     expectJson:"
                + resultDetail.getExpect() + "\033[m");

    }

    /**
     * *
     *
     * @Description: 精确比较
     * @Param: [actualString, expectString, prefix]
     * @return: void
     * @Author: YJiang(叶闲)
     * @DateTimeFormat: 2020/6/3
     */
    private static void preciseComparisons(String actualString, String expectString, String prefix) {
        if (!expectString.equals(actualString)) {
            ResultDetail resultDetail = new ResultDetail(prefix, actualString, expectString,
                "Value Not Equal");
            updateBaseResult(resultDetail);
        } else {
            System.out.println(
                "\033[1;94m" + "【success】Key:" + prefix + "\n     actualJson:" + actualString
                    + "\n     expectJson:"
                    + expectString + "\033[m");
        }
    }

    /**
     * *
     *
     * @Description: 模糊比较
     * @Param: [actualString, expectString, prefix]
     * @return: void
     * @Author: YJiang(叶闲)
     * @DateTimeFormat: 2020/6/3
     */
    private static void fuzzyComparisons(String actualString, String expectString, String prefix) {

        if (!StringUtils.isNotBlank(actualString)) {
            ResultDetail resultDetail = new ResultDetail(prefix, actualString, expectString,
                "Value is Null");
            updateBaseResult(resultDetail);
        } else {
            System.out.println(
                "\033[1;94m" + "【success】Key:" + prefix + "\n     actualJson:" + actualString
                    + "\n     expectJson:"
                    + expectString + "\033[m");
        }
    }

    private static void compareType(String actualString, String expectString, String prefix, boolean type) {
        if (type) {
            preciseComparisons(actualString, expectString, prefix);
        } else {
            fuzzyComparisons(actualString, expectString, prefix);
        }
    }

    /**
     * @Description: 字符串比较
     * @Param: [parameter] ignoreType 忽略自己：true 忽略其他 fasle
     * @return: void
     * @Author: YJiang
     * @DateTimeFormat: 2020/3/8
     */
    private static void compareJson(String actualString, String expectString, String key, String prefix,
        String ignore, boolean ignoreType, boolean compareType) {

        boolean status = false;

        if (StringUtils.isNotBlank(ignore)) {
            List<String> listIgnore = Arrays.asList(ignore.split(","));

            status = listIgnore.contains(key);

            if (status) {
                // 忽略其他 只校验ignore中key
                if (!ignoreType) {
                    compareType(actualString, expectString, prefix, compareType);
                }
            } else {
                // 忽略自己 只校验除ignore中的key
                if (ignoreType) {
                    compareType(actualString, expectString, prefix, compareType);
                }

            }
        } else {
            // 全校验
            compareType(actualString, expectString, prefix, compareType);

        }

    }

    /**
     * @Description: JSONObject 比较
     * @Param: [parameter]
     * @return: void
     * @Author: YJiang
     * @DateTimeFormat: 2020/3/8
     */
    private static void compareJson(JSONObject actualJson, JSONObject expectJson, String key, String prefix,
        String ignore, boolean ignoreType, boolean compareType) {
        if (StringUtils.isBlank(prefix)) {
            prefix = "";
        } else {
            prefix = prefix + ".";
        }

        for (String s : expectJson.keySet()) {
            key = s;
            compareJson(actualJson.get(key), expectJson.get(key), key, prefix + key, ignore, ignoreType, compareType);
        }
    }

    /**
     * @Description: JSONArray 比较
     * @Param: [parameter]
     * @return: void
     * @Author: YJiang
     * @DateTimeFormat: 2020/3/8
     */
    private static void compareJson(JSONArray actualJsonArray, JSONArray expectJsonArray, String key, String prefix,
        String ignore, boolean ignoreType, boolean compareType) {
        if (actualJsonArray != null && expectJsonArray != null) {
            if (actualJsonArray.size() == expectJsonArray.size()) {
                Iterator iteratorActualJsonArray = actualJsonArray.iterator();
                if (StringUtils.isBlank(prefix)) {
                    prefix = "";
                }
                int num = 0;
                for (Object o : expectJsonArray) {
                    compareJson(iteratorActualJsonArray.next(), o, key, prefix + "[" + num + "]", ignore, ignoreType,
                        compareType);
                    num++;

                }
            } else {
                System.out.println(
                    "\033[1;31m" + "【fail】Key:" + prefix + "\n     actualJson:" + actualJsonArray.toString() + "\n   "
                        + "  expectJson:"
                        + expectJsonArray.toString() + "\033[m");
                ResultDetail resultDetail = new ResultDetail(prefix, actualJsonArray.size(), expectJsonArray.size(),
                    "Length Not Equal");
                updateBaseResult(resultDetail);
            }
        } else {
            if (actualJsonArray == null && expectJsonArray == null) {
                ResultDetail resultDetail = new ResultDetail(prefix, "在 actualJsonArray 中不存在",
                    "在 expectJsonArray 中不存在",
                    "Both Not Exist");

                updateBaseResult(resultDetail);
            } else if (actualJsonArray == null) {
                ResultDetail resultDetail = new ResultDetail(prefix, "在 actualJsonArray 中不存在", expectJsonArray,
                    "Other Exist");
                updateBaseResult(resultDetail);
            } else {
                ResultDetail resultDetail = new ResultDetail(prefix, actualJsonArray, "在 expectJsonArray 中不存在",
                    "Other Exist");
                updateBaseResult(resultDetail);
            }
        }

    }

    /**
     * @Description: Object 比较
     * @Param: [parameter]
     * @return: void
     * @Author: YJiang
     * @DateTimeFormat: 2020/3/8
     */
    private static void compareJson(Object actualJson, Object expectJson, String key, String prefix, String ignore,
        boolean ignoreType, boolean compareType) {
        if (actualJson != null && expectJson != null) {
            if (actualJson instanceof JSONObject) {
                compareJson((JSONObject)actualJson, (JSONObject)expectJson, key, prefix, ignore, ignoreType,
                    compareType);
            } else if (actualJson instanceof JSONArray) {
                compareJson((JSONArray)actualJson, (JSONArray)expectJson, key, prefix, ignore, ignoreType, compareType);
            } else if (actualJson instanceof String) {
                try {
                    String actualJsonToStr = actualJson.toString();
                    String expectJsonToStr = expectJson.toString();
                    compareJson(actualJsonToStr, expectJsonToStr, key, prefix, ignore, ignoreType, compareType);

                } catch (Exception e) {
                    ResultDetail resultDetail = new ResultDetail(prefix, actualJson, expectJson, "String 转换发生异常 Key");
                    updateBaseResult(resultDetail);
                    e.printStackTrace();
                }

            } else {
                compareJson(actualJson.toString(), expectJson.toString(), key, prefix, ignore, ignoreType, compareType);
            }
        } else {
            if (actualJson == null && expectJson == null) {
                ResultDetail resultDetail = new ResultDetail(prefix, "在actualJson中不存在", "在expectJson中不存在",
                    "Both Not Exist");
                updateBaseResult(resultDetail);
            } else if (actualJson == null) {
                ResultDetail resultDetail = new ResultDetail(prefix, "在actualJson中不存在", expectJson, "Other Exist");
                updateBaseResult(resultDetail);
            } else {
                ResultDetail resultDetail = new ResultDetail(prefix, actualJson, "在expectJson中不存在", "Not Exist");
                updateBaseResult(resultDetail);
            }

        }

    }

    /**
     * 非线性安全
     *
     * @Description: CompareFactory
     * @Param: [parameter]
     * @return: void
     * @Author: YJiang
     * @DateTimeFormat: 2020/3/9
     */
    public static <T> CompareBaseResult compareJsonWithExclude(T actual, T expect, String ignore, boolean compareType) {
        CompareJsonUtils.compareBaseResult = new CompareBaseResult();
        boolean ignoreType = true;

        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);

        return CompareJsonUtils.compareBaseResult;
    }

    public static <T> CompareBaseResult compareJsonWithChoose(T actual, T expect, String ignore, boolean compareType) {
        CompareJsonUtils.compareBaseResult = new CompareBaseResult();
        boolean ignoreType = false;

        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);

        return CompareJsonUtils.compareBaseResult;
    }

    public static <T> CompareBaseResult compareJson(T actual, T expect, boolean compareType) {
        CompareJsonUtils.compareBaseResult = new CompareBaseResult();
        boolean ignoreType = true;
        String ignore = null;
        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);
        return CompareJsonUtils.compareBaseResult;
    }

    public static <T> CompareBaseResult compareJson(T actual, T expect) {
        CompareJsonUtils.compareBaseResult = new CompareBaseResult();
        boolean ignoreType = true;
        boolean compareType = true;

        String ignore = null;
        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);
        return CompareJsonUtils.compareBaseResult;
    }

    public static <T> CompareBaseResult compareJson(T actual, T expect, JSONObject check) {
        JSONObject checkWayJson = checkWay(check);
        boolean ignoreType = false;
        boolean compareType = true;
        String ignore = checkWayJson.get("key").toString();

        if ("exclude".equals(checkWayJson.get("way").toString())) { ignoreType = true; }

        if ("false".equals(checkWayJson.get("precision").toString())){ compareType = false; }

        if ("".equals(ignore)){ ignore = null; }

        CompareJsonUtils.compareBaseResult = new CompareBaseResult();

        CompareJsonUtils.compareBaseResult.setRetParameterDetail(check.toJSONString());

        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);

        return CompareJsonUtils.compareBaseResult;
    }


    @Deprecated
    public static <T> CompareBaseResult CompareJsonUtilsFactory(T actual, T expect, String ignore) {
        CompareJsonUtils.compareBaseResult = new CompareBaseResult();
        boolean ignoreType = true;
        boolean compareType = true;

        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);

        return CompareJsonUtils.compareBaseResult;
    }

}
