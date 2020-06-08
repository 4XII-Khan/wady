package framework.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import framework.base.CompareBaseResultDTO;
import framework.base.ResultDetailDTO;
import org.apache.commons.lang3.StringUtils;
import static framework.utils.ResultCheckWay.checkWay;

public class CompareJsonUtils {

    private static CompareBaseResultDTO compareBaseResultDTO;

    public static CompareBaseResultDTO getBaseResult() {
        return compareBaseResultDTO;
    }

    public static void setBaseResult(CompareBaseResultDTO compareBaseResultDTO) {
        CompareJsonUtils.compareBaseResultDTO = compareBaseResultDTO;
    }

    private static void updateBaseResult(ResultDetailDTO resultDetailDTO) {

        // 错误数量统计
        compareBaseResultDTO.setRetCode(compareBaseResultDTO.getRetCode() + 1);
        // 填充 RetValue 错误内容
        if (compareBaseResultDTO.getRetValue() == null) {
            ArrayList<ResultDetailDTO> resultDetailDTOArrayList = new ArrayList<ResultDetailDTO>();
            resultDetailDTOArrayList.add(resultDetailDTO);
            compareBaseResultDTO.setRetValue(resultDetailDTOArrayList);
        } else {
            compareBaseResultDTO.getRetValue().add(resultDetailDTO);
        }
        System.out.println(
            "\033[1;31m" + "【fail】Key:" + resultDetailDTO.getKey() + "\n     actualJson:" + resultDetailDTO.getActual()
                + "\n     expectJson:"
                + resultDetailDTO.getExpect() + "\033[m");

    }

    private static void preciseComparisons(String actualString, String expectString, String prefix) {
        if (!expectString.equals(actualString)) {
            ResultDetailDTO resultDetailDTO = new ResultDetailDTO(prefix, actualString, expectString,
                "Value Not Equal");
            updateBaseResult(resultDetailDTO);
        } else {
            System.out.println(
                "\033[1;94m" + "【success】Key:" + prefix + "\n     actualJson:" + actualString
                    + "\n     expectJson:"
                    + expectString + "\033[m");
        }
    }

    private static void fuzzyComparisons(String actualString, String expectString, String prefix) {

        if (!StringUtils.isNotBlank(actualString)) {
            ResultDetailDTO resultDetailDTO = new ResultDetailDTO(prefix, actualString, expectString,
                "Value is Null");
            updateBaseResult(resultDetailDTO);
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

    private static void compareJson(JSONArray actualJsonArray, JSONArray expectJsonArray, String key, String prefix,
        String ignore, boolean ignoreType, boolean compareType) {
        if (actualJsonArray != null && expectJsonArray != null) {
            if (actualJsonArray.size() == expectJsonArray.size()) {
                Iterator iteratorActualJsonArray = actualJsonArray.iterator();
                if (StringUtils.isBlank(prefix)) { prefix = ""; }
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
                ResultDetailDTO resultDetailDTO = new ResultDetailDTO(prefix, actualJsonArray.size(), expectJsonArray.size(),
                    "Length Not Equal");
                updateBaseResult(resultDetailDTO);
            }
        } else {
            if (actualJsonArray == null && expectJsonArray == null) {
                ResultDetailDTO resultDetailDTO = new ResultDetailDTO(prefix, "在 actualJsonArray 中不存在",
                    "在 expectJsonArray 中不存在",
                    "Both Not Exist");

                updateBaseResult(resultDetailDTO);
            } else if (actualJsonArray == null) {
                ResultDetailDTO resultDetailDTO = new ResultDetailDTO(prefix, "在 actualJsonArray 中不存在", expectJsonArray,
                    "Other Exist");
                updateBaseResult(resultDetailDTO);
            } else {
                ResultDetailDTO resultDetailDTO = new ResultDetailDTO(prefix, actualJsonArray, "在 expectJsonArray 中不存在",
                    "Other Exist");
                updateBaseResult(resultDetailDTO);
            }
        }

    }

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
                    ResultDetailDTO resultDetailDTO = new ResultDetailDTO(prefix, actualJson, expectJson, "String 转换发生异常 Key");
                    updateBaseResult(resultDetailDTO);
                    e.printStackTrace();
                }

            } else {
                compareJson(actualJson.toString(), expectJson.toString(), key, prefix, ignore, ignoreType, compareType);
            }
        } else {
            if (actualJson == null && expectJson == null) {
                ResultDetailDTO resultDetailDTO = new ResultDetailDTO(prefix, "在actualJson中不存在", "在expectJson中不存在",
                    "Both Not Exist");
                updateBaseResult(resultDetailDTO);
            } else if (actualJson == null) {
                ResultDetailDTO resultDetailDTO = new ResultDetailDTO(prefix, "在actualJson中不存在", expectJson, "Other Exist");
                updateBaseResult(resultDetailDTO);
            } else {
                ResultDetailDTO resultDetailDTO = new ResultDetailDTO(prefix, actualJson, "在expectJson中不存在", "Not Exist");
                updateBaseResult(resultDetailDTO);
            }

        }

    }

    public static <T> CompareBaseResultDTO compareJsonWithExclude(T actual, T expect, String ignore, boolean compareType) {
        CompareJsonUtils.compareBaseResultDTO = new CompareBaseResultDTO();
        boolean ignoreType = true;

        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);

        return CompareJsonUtils.compareBaseResultDTO;
    }

    public static <T> CompareBaseResultDTO compareJsonWithChoose(T actual, T expect, String ignore, boolean compareType) {
        CompareJsonUtils.compareBaseResultDTO = new CompareBaseResultDTO();
        boolean ignoreType = false;

        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);

        return CompareJsonUtils.compareBaseResultDTO;
    }

    public static <T> CompareBaseResultDTO compareJson(T actual, T expect, boolean compareType) {
        CompareJsonUtils.compareBaseResultDTO = new CompareBaseResultDTO();
        boolean ignoreType = true;
        String ignore = null;
        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);
        return CompareJsonUtils.compareBaseResultDTO;
    }

    public static <T> CompareBaseResultDTO compareJson(T actual, T expect) {
        CompareJsonUtils.compareBaseResultDTO = new CompareBaseResultDTO();
        boolean ignoreType = true;
        boolean compareType = true;

        String ignore = null;
        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);
        return CompareJsonUtils.compareBaseResultDTO;
    }

    public static <T> CompareBaseResultDTO compareJson(T actual, T expect, JSONObject check) {
        JSONObject checkWayJson = checkWay(check);
        boolean ignoreType = false;
        boolean compareType = true;
        String ignore = checkWayJson.get("key").toString();

        if ("exclude".equals(checkWayJson.get("way").toString())) { ignoreType = true; }

        if ("false".equals(checkWayJson.get("precision").toString())){ compareType = false; }

        if ("".equals(ignore)){ ignore = null; }

        CompareJsonUtils.compareBaseResultDTO = new CompareBaseResultDTO();

        CompareJsonUtils.compareBaseResultDTO.setRetParameterDetail(check.toJSONString());

        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);

        return CompareJsonUtils.compareBaseResultDTO;
    }

    @Deprecated
    public static <T> CompareBaseResultDTO CompareJsonUtilsFactory(T actual, T expect, String ignore) {
        CompareJsonUtils.compareBaseResultDTO = new CompareBaseResultDTO();
        boolean ignoreType = true;
        boolean compareType = true;

        CompareJsonUtils.compareJson(actual, expect, null, null, ignore, ignoreType, compareType);

        return CompareJsonUtils.compareBaseResultDTO;
    }

}
