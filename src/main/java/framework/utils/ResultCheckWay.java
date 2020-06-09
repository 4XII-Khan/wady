package framework.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * @program: wady
 *
 * @description: TODO
 *
 * @author:
 *
 * @create: 2020-06-06 10:35
 */
public class ResultCheckWay {

    public static JSONObject checkWay(JSONObject paramsObj) {
        String precision = null;
        String key = null;
        String way = null;
        JSONObject jsonReturn = new JSONObject();
        if (paramsObj.containsKey("check")) {
            String check = JSON.toJSONString(paramsObj.get("check"));
            JSONObject jsonCheck = JSONObject.parseObject(check);
            if (jsonCheck.containsKey("precision")){
                precision = jsonCheck.get("precision").toString().trim();
            }
            if (jsonCheck.containsKey("key")){
                key = jsonCheck.get("key").toString().trim();
            }
            if (jsonCheck.containsKey("way")){
                way = jsonCheck.get("way").toString().trim();
            }
        }

        if (StringUtils.isNotBlank(precision)) {
            jsonReturn.put("precision", !"fuzzy".equals(precision));
        }else {
            jsonReturn.put("precision",true);
        }

        if (StringUtils.isNotBlank(way)) {
            if ("exclude".equals(way)) {
                jsonReturn.put("way","exclude");
            }else if ("choose".equals(way)) {
                jsonReturn.put("way","choose");
            }else {
                jsonReturn.put("way","all");
            }
        }else {
            jsonReturn.put("way","all");
        }

        if (StringUtils.isNotBlank(key)) {
            jsonReturn.put("key",key);
        }else {
            jsonReturn.put("key","");
        }

        return jsonReturn;

    }

}
