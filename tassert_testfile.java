//package notReal;

import org.junit.Assert;
public class tassert_testfile {

    public void testMethod() {
        Assert.fail("Blabla");
    }

    public static void main(String[] args) {
        tassert_testfile test = new tassert_testfile();
        test.testMethod();

        // Ne fonctionne pas pour deux assertions sur une meme ligne
        Assert.fail();// Assert.fail();
        Assert.assertFalse(true);
        Assert.fail();
        fail();
    }
}
