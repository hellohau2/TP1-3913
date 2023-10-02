import org.junit.Assert;
public class tassert1 {

    public void testMethod() {
        Assert.fail("Blabla");
    }

    public static void main(String[] args) {
        tassert1 test = new tassert1();
        test.testMethod();

        // Ne fonctionne pas pour deux assertions sur une meme ligne
        Assert.fail(); Assert.fail();
        Assert.assertFalse(true);
        Assert.fail();
    }
}
