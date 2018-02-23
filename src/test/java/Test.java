import dep.TestClass;
import dep.TestInterface;
import dep.TestInterface2;

public class Test extends TestClass implements TestInterface, TestInterface2 {
    private Integer value;
    private Test next;
    private Test2 next2;

    public void hello(String s) {
        System.out.println("abc");
    }

    public class InnerClass {
        private int innerValue;
    }

    public static class InnerClass2 {
        private int innerValue;
    }
}
