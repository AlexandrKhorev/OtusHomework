package tests;

import test.framework.annotations.After;
import test.framework.annotations.Before;
import test.framework.annotations.Test;

import testClasses.SomeClass;

import static test.framework.utils.assertion.Assert.assertThat;


public class SomeClassTest {

    // Проверяем, что действительно каждый раз отрабатывает Before,
    // и создает новый объект со стандартными параметрами

    final int id = 1;
    final String name = "someName";
    SomeClass instance;

    @Before
    void before(){
        instance = new SomeClass(id, name);
    }

    @Test
    void test1(){

        assertThat(instance.getId()).equalsTo(id);
        assertThat(instance.getName()).equalsTo(name);

        String otherName = "otherName1";

        instance.setName(otherName);

        assertThat(instance.getName()).equalsTo(otherName);
    }

    @Test
    void test2(){
        assertThat(instance.getId()).equalsTo(id);
        assertThat(instance.getName()).equalsTo(name);

        String otherName = "otherName2";

        instance.setName(otherName);

        assertThat(instance.getName()).equalsTo(otherName);
    }

    void noTest(){
        throw new NullPointerException();
    }

    @After
    void after(){
        instance.setName("afterName");
    }

}
