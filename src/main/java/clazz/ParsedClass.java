package clazz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsedClass {
    private String packagePath;
    private String name;
    private List<String> imports;
    private Map<String, String> members;
    private List<String> extendsClasses;
    private List<String> implementsClasses;
    private List<ClassMethod> methods;

    private ClassEnum type = ClassEnum.CLASS;

    public ParsedClass() {
        imports = new ArrayList<>();
        members = new HashMap<>();
        extendsClasses = new ArrayList<>();
        implementsClasses = new ArrayList<>();
        methods = new ArrayList<>();
    }

    public ClassEnum getType() {
        return type;
    }

    public void setType(ClassEnum type) {
        this.type = type;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public Map<String, String> getMembers() {
        return members;
    }

    public void setMembers(Map<String, String> members) {
        this.members = members;
    }

    public List<String> getExtendsClasses() {
        return extendsClasses;
    }

    public void setExtendsClasses(List<String> extendsClasses) {
        this.extendsClasses = extendsClasses;
    }

    public List<String> getImplementsClasses() {
        return implementsClasses;
    }

    public void setImplementsClasses(List<String> implementsClasses) {
        this.implementsClasses = implementsClasses;
    }

    public List<ClassMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<ClassMethod> methods) {
        this.methods = methods;
    }

    @Override
    public String toString() {
        return "ParsedClass{" +
                "packagePath='" + packagePath + '\'' +
                ", name='" + name + '\'' +
                ", imports=" + imports +
                ", members=" + members +
                ", extendsClasses=" + extendsClasses +
                ", implementsClasses=" + implementsClasses +
                ", methods=" + methods +
                ", type=" + type +
                '}';
    }
}
