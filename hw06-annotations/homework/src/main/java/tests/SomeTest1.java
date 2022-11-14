package tests;

import test.framework.annotations.After;
import test.framework.annotations.Before;
import test.framework.annotations.Test;

public class SomeTest1 {
    /*
    * Исключение в Before. Ожидаемый вывод:
    *
    * before
    * after
    * Exception in method "BEFORE"
    */
    @Before
    void before() {
        System.out.println("before");
        throw new NullPointerException();
    }

    @Test
    void test1() {
    }

    @Test
    void test2() {
    }

    @After
    void after() {
        System.out.println("after");
    }
}
