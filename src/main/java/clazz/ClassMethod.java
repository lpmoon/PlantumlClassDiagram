package clazz;

import java.util.ArrayList;
import java.util.List;

public class ClassMethod {
    private String name;
    private String returnType;
    private List<String> paramsType;

    public ClassMethod() {
        paramsType = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<String> getParamsType() {
        return paramsType;
    }

    public void setParamsType(List<String> paramsType) {
        this.paramsType = paramsType;
    }

    @Override
    public String toString() {
        return "ClassMethod{" +
                "name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", paramsType=" + paramsType +
                '}';
    }
}
