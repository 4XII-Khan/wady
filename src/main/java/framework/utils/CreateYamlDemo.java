package framework.utils;

import com.esotericsoftware.yamlbeans.YamlWriter;
import framework.base.YamlDetailDTO;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CreateYamlDemo {
    public static  void demo(String yamlFilePath) throws IOException {

        YamlDetailDTO yamlDetailDTO = new YamlDetailDTO();
        yamlDetailDTO.setTestcase("");


        HashMap<String,String> parameterization = new HashMap<>();
        parameterization.put("parameter1.name","Jack,Tim");
        yamlDetailDTO.setParameterization(parameterization);

        yamlDetailDTO.setParameterCombination("permutation or sequential");
        HashMap<String,String> parameter = new HashMap<>();
        parameter.put("parameter1",null);

        yamlDetailDTO.setParameter(parameter);

        HashMap<String,String> expectResult = new HashMap<>();
        expectResult.put("expect",null);
        yamlDetailDTO.setExpectResult(expectResult);

        HashMap<String,String> check = new HashMap<>();
        check.put("precision","precision or fuzzy");
        check.put("key","");
        check.put("way","precision or choose");
        yamlDetailDTO.setCheck(check);


        FileWriter fileWriter = new FileWriter(yamlFilePath, false);
        YamlWriter yamlWriter = new YamlWriter(fileWriter);
        // 取消添加全限定类名
        yamlWriter.getConfig().writeConfig.setWriteRootTags(false);
        yamlWriter.write(yamlDetailDTO);
        // 分隔符
        fileWriter.write("---\n");
        yamlWriter.close();

    }

    public static void main(String [] args){
        try {
            CreateYamlDemo.demo("test.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
