package tests;

import test.framework.annotations.After;
import test.framework.annotations.Before;
import test.framework.annotations.Test;

public class SomeTest3 {
    /*
     * Исключение в After. Ожидаемый вывод:
     *
     * test2: BEFORE
     * test2: PASSED
     * Exception in method "AFTER"
     *
     * test1: BEFORE
     * test1: PASSED
     * Exception in method "AFTER"
     *
     * PASSED: 0, FAILED: 2, ALL: 2
     */
    @Before
    void before() {
    }

    @Test
    void test1() {
    }

    @Test
    void test2() {
    }

    @After
    void after() {
        throw new NullPointerException();
    }
}
