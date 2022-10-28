import test.framework.TestRunner;
import test.framework.utils.assertion.Equals;
import tests.SomeClassTest;
import tests.SomeTest1;
import tests.SomeTest2;
import tests.SomeTest3;

public class Main {
    public static void main(String[] args) {
//        TestRunner.runTests(SomeTest1.class);
//        TestRunner.runTests(SomeTest2.class);
//        TestRunner.runTests(SomeTest3.class);
        TestRunner.runTests(SomeClassTest.class);
    }
}
