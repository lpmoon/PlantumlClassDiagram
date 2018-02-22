import dep.TestClass;
import dep.TestInterface;
import dep.TestInterface2;

public abstract class AbstractTest extends TestClass implements TestInterface, TestInterface2 {
    private Integer value;
    private AbstractTest next;
}
