import com.lpmoon.plantuml.classdiagram.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testBlank() {
        Assert.assertTrue(StringUtil.isBlank(""));
        Assert.assertTrue(StringUtil.isBlank(" "));
        Assert.assertTrue(StringUtil.isBlank("  "));
        Assert.assertTrue(StringUtil.isBlank("      "));
        Assert.assertTrue(StringUtil.isBlank("\t"));
        Assert.assertTrue(StringUtil.isBlank("\r"));
        Assert.assertTrue(StringUtil.isBlank("\r\t"));
        Assert.assertTrue(StringUtil.isBlank(" \r\t"));
        Assert.assertFalse(StringUtil.isBlank(" \r\ta"));
        Assert.assertFalse(StringUtil.isBlank("a "));
    }
}
