package tests;

import test.framework.annotations.After;
import test.framework.annotations.Before;
import test.framework.annotations.Test;
import static test.framework.utils.assertion.Assert.assertThat;

public class SomeTest2 {

    /*
     * Две аннотации BEFORE. Выведется предупреждение, но тесты запустятся.
     * Три теста, два с исключением, один корректный.
     * Один метод без аннотации, он не должен дергаться фреймворком
     * Ожидаемый вывод:

     * there should be only one method with "BEFORE" annotation
     * test3: BEFORE
     * test3: PASSED
     * test3: AFTER
     *
     * test1: BEFORE
     * test1: FAILED
     * test1: AFTER
     *
     * test2: BEFORE
     * test2: FAILED
     * test2: AFTER
     *
     * PASSED: 1, FAILED: 2, ALL: 3
     */

    @Before
    void beforeTests() {
    }

    @Before
    void beforeTests2() {
    }

    @Test
    void test1() {
        assertThat(1).equalsTo(1);
    }

    @Test
    void test2() {
        throw new NullPointerException();
    }

    @Test
    void test3() {
        assertThat(1).equalsTo(3);
    }

    void noTest() {
        System.out.println("ERROR! It's no test");
    }

    @After
    void afterTests() {
    }
}
