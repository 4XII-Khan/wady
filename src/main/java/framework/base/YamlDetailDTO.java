package framework.base;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class YamlDetailDTO {
    public String testcase;
    public HashMap<String,String> parameterization;

    public String parameterCombination;
    public HashMap<String,String> parameter;
    public HashMap<String,String> expectResult;
    public HashMap<String,String> check;


    public void setTestcase(String testcase) {
        this.testcase = testcase;
    }

    public void setParameterization(HashMap<String, String> parameterization) {
        this.parameterization = parameterization;
    }

    public void setParameterCombination(String parameterCombination) {
        this.parameterCombination = parameterCombination;
    }

    public void setParameter(HashMap<String, String> parameter) {
        this.parameter = parameter;
    }

    public void setExpectResult(HashMap<String, String> expectResult) {
        this.expectResult = expectResult;
    }

    public void setCheck(HashMap<String, String> check) {
        this.check = check;
    }


    public String getTestcase() {
        return testcase;
    }

    public HashMap<String, String> getParameterization() {
        return parameterization;
    }

    public String getParameterCombination() {
        return parameterCombination;
    }

    public HashMap<String, String> getParameter() {
        return parameter;
    }

    public HashMap<String, String> getExpectResult() {
        return expectResult;
    }

    public HashMap<String, String> getCheck() {
        return check;
    }



}
