import dep.TestClass;
import dep.TestInterface;
import dep.TestInterface2;

public abstract class AbstractTest extends TestClass implements TestInterface, TestInterface2 {
    private Integer value;
    private AbstractTest next;
    private TestEnum testEnum;
    private Test3 test3;
    private Test2 test2;
}
