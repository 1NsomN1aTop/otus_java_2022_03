import annotations.After;
import annotations.Before;
import annotations.Test;

import static java.lang.System.out;
public class MyTest {

    @Before
    public void doSomethingBefore(){
        out.println("@Before annotation");
    }

    @After
    public void doSomethingAfter(){
        out.println("@After annotation");
    }

    @Test(displayName = "Failed Test")
    public void failedTest(){
        throw new RuntimeException("this is the failed test with @Test annotation");
    }
    @Test(displayName = "Successful Test")
    public void test(){
        out.println("this is the test with @Test annotation");
    }

}
