package framework.utils;

import com.esotericsoftware.yamlbeans.YamlWriter;
import framework.base.YamlDetailDTO;
import org.ho.yaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class WriterYaml {
    public static  void setWriter(String yamlFilePath) throws IOException {

        YamlDetailDTO yamlDetailDTO = new YamlDetailDTO();
        yamlDetailDTO.setTestcase("");


        HashMap<String,String> parameterization = new HashMap<>();
        parameterization.put("parameter1.name","Jack,Tim");
        yamlDetailDTO.setParameterization(parameterization);

        yamlDetailDTO.setParameterCombination("permutation or sequential");
        HashMap<String,String> parameter = new HashMap<>();
        parameter.put("parameter1",null);
        parameter.put("parameter2",null);

        yamlDetailDTO.setParameter(parameter);

        HashMap<String,String> expectResult = new HashMap<>();
        expectResult.put("expect",null);
        yamlDetailDTO.setExpectResult(expectResult);

        HashMap<String,String> check = new HashMap<>();
        check.put("precision","precision or fuzzy");
        check.put("key",null);
        check.put("way","precision or choose");
        yamlDetailDTO.setCheck(check);



        FileWriter fileWriter = new FileWriter(yamlFilePath, false);
        YamlWriter yamlWriter = new YamlWriter(fileWriter);
        yamlWriter.getConfig().writeConfig.setWriteRootTags(false); // 取消添加全限定类名
        yamlWriter.write(yamlDetailDTO);
        fileWriter.write("---\n"); // 分隔符
        yamlWriter.close();

    }

    public static void main(String [] args){
        try {
            WriterYaml.setWriter("aaa.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
