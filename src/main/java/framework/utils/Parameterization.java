package framework.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.JSONPath;
import org.apache.commons.lang3.StringUtils;

public class Parameterization {

    public static void parameterCombination(ArrayList<ArrayList<HashMap<String, String>>> inputList, int beginIndex,
        ArrayList<HashMap<String, String>> parameterList, ArrayList<ArrayList<HashMap<String, String>>> outputList) {
        // 判断是否完成一次参数组合
        if (beginIndex == inputList.size()) {
            // 浅拷贝 解决对象引用问题
            ArrayList<HashMap<String, String>> parameterListClone = (ArrayList)parameterList.clone();
            outputList.add(parameterListClone);
            return;
        }
        for (HashMap<String, String> hashMap : inputList.get(beginIndex)) {
            // 根据当前组合进度判断进行添加 OR 更新
            if (parameterList.size() > beginIndex) {
                parameterList.set(beginIndex, hashMap);
            } else {
                parameterList.add(beginIndex, hashMap);
            }
            // 递归处理
            parameterCombination(inputList, beginIndex + 1, parameterList, outputList);
        }
    }

    public static ArrayList<JSONObject> permutationCombination(JSONObject paramsObj) {

        String parameterizationString = JSON.toJSONString(paramsObj.get("parameterization"));
        JSONObject parameterizationJson = JSONObject.parseObject(parameterizationString);

        String parameterString = JSON.toJSONString(paramsObj.get("parameter"));
        JSONObject parameterJson = JSONObject.parseObject(parameterString);

        String expectString = JSON.toJSONString(paramsObj.get("expectResult"));
        JSONObject expectJson = JSONObject.parseObject(expectString);

        ArrayList<ArrayList<HashMap<String, String>>> paramList = new ArrayList<>();
        for (String i : parameterizationJson.keySet()) {
            ArrayList<HashMap<String, String>> tmp = new ArrayList<>();
            String param = parameterizationJson.get(i).toString();
            List<String> listIgnore = Arrays.asList(param.split(","));
            for (String o : listIgnore) {
                HashMap<String, String> map = new HashMap<>();
                map.put(i, o.trim());
                tmp.add(map);
            }
            paramList.add(tmp);
        }

        ArrayList<HashMap<String, String>> parameterList = new ArrayList<>();
        ArrayList<ArrayList<HashMap<String, String>>> out = new ArrayList<>();
        parameterCombination(paramList, 0, parameterList, out);
        ArrayList<JSONObject> yamlBlock = new ArrayList<>();

        int i = 0 ;
        for (ArrayList<HashMap<String, String>> p : out) {
            i ++ ;
            System.out.println("\n创建ID："+i);
            for (HashMap<String, String> kv : p) {
                for (Map.Entry<String, String> entry : kv.entrySet()) {
                    String setKey = "$."+entry.getKey();
                    System.out.println("取值："+"$."+entry.getKey()+":"+entry.getValue());
                    // 如果不存在则 跳过
                    if (JSONPath.contains(parameterJson, setKey)){
                        JSONPath.set(parameterJson, setKey, entry.getValue());
                    }else if (JSONPath.contains(expectJson, setKey)){
                        JSONPath.set(expectJson, setKey, entry.getValue());
                    }
                }
            }
            paramsObj.put("parameter",parameterJson);
            paramsObj.put("expectResult",expectJson);
            System.out.println("参数："+parameterJson.toJSONString());
            System.out.println("期望："+expectJson.toJSONString());

            JSONObject paramsObjClone = (JSONObject) paramsObj.clone();
            paramsObjClone.remove("parameterization");
            yamlBlock.add(paramsObjClone);
        }
        return yamlBlock;
    }

    public static ArrayList<JSONObject> sequentialComposition(JSONObject paramsObj) {
        String parameterizationString = JSON.toJSONString(paramsObj.get("parameterization"));
        JSONObject parameterizationJson = JSONObject.parseObject(parameterizationString);

        String parameterString = JSON.toJSONString(paramsObj.get("parameter"));
        JSONObject parameterJson = JSONObject.parseObject(parameterString);

        String expectString = JSON.toJSONString(paramsObj.get("expectResult"));
        JSONObject expectJson = JSONObject.parseObject(expectString);

        ArrayList<ArrayList<HashMap<String, String>>> paramList = new ArrayList<>();
        int maxLength = 0;
        for (String key : parameterizationJson.keySet()) {
            ArrayList<HashMap<String, String>> tmp = new ArrayList<>();
            String param = parameterizationJson.get(key).toString();
            List<String> listIgnore = Arrays.asList(param.split(","));
            if (maxLength < listIgnore.size()) {maxLength = listIgnore.size(); }
            for (String ignore : listIgnore) {
                HashMap<String, String> map = new HashMap<>();
                map.put(key, ignore);
                tmp.add(map);
            }
            paramList.add(tmp);
        }
        ArrayList<ArrayList<HashMap<String, String>>> outputArrayLists = new ArrayList<>();
        for (int index = 0; index < maxLength; index++) {
            ArrayList<HashMap<String, String>> disposable = new ArrayList<>();
            for (ArrayList<HashMap<String, String>> arrayList : paramList) {
                if (index < arrayList.size()) {
                    disposable.add(arrayList.get(index));
                }
            }
            outputArrayLists.add(disposable);
        }
        System.out.println(outputArrayLists.toString());

        ArrayList<JSONObject> yamlBlock = new ArrayList<>();
        int index = 0;
        for (ArrayList<HashMap<String, String>> p : outputArrayLists) {
            index++;
            System.out.println("\n创建ID："+index);
            for (HashMap<String, String> kv : p) {
                for (Map.Entry<String, String> entry : kv.entrySet()) {
                    System.out.println("取值："+entry.getKey()+":"+ entry.getValue());
                    if (JSONPath.contains(parameterJson, entry.getKey())){
                        JSONPath.set(parameterJson, entry.getKey(), entry.getValue());
                    }else if (JSONPath.contains(expectJson, entry.getKey())){
                        JSONPath.set(expectJson, entry.getKey(), entry.getValue());
                    }
                }
            }
            paramsObj.put("parameter",parameterJson);
            paramsObj.put("expectResult",expectJson);
            System.out.println("参数："+parameterJson.toJSONString());
            System.out.println("期望："+expectJson.toJSONString());
            JSONObject paramsObjClone = (JSONObject) paramsObj.clone();
            paramsObjClone.remove("parameterization");
            yamlBlock.add(paramsObjClone);
        }
        return yamlBlock;
    }

    public static ArrayList<JSONObject> parameterComposition(JSONObject paramsObj) {
        String parameterizationString = null;
        if(paramsObj.containsKey("parameterCombination")){
            parameterizationString = paramsObj.get("parameterCombination").toString();
        }
        if(StringUtils.isNotBlank(parameterizationString)){
            if ("sequential".equals(parameterizationString)){
                return sequentialComposition(paramsObj);
            }
        }
        return permutationCombination(paramsObj);
    }
}
