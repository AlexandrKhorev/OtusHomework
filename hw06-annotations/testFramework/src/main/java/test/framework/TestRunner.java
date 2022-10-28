package test.framework;

import test.framework.annotations.Before;
import test.framework.annotations.After;
import test.framework.annotations.Test;
import test.framework.utils.ColorText;
import test.framework.utils.StatusTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TestRunner {
    private static Map<String, List<Method>> getAnnotatedMethods(Class<?> clazz) {

        // Метод разбирает класс-тест по аннотациям

        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {

            method.setAccessible(true);

            if (method.isAnnotationPresent(Before.class)) {
                addOneMethodInCollection(method, beforeMethods, "BEFORE");
            } else if (method.isAnnotationPresent(After.class)) {
                addOneMethodInCollection(method, afterMethods, "AFTER");
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }
        return new HashMap<>() {{
            put("BEFORE", beforeMethods);
            put("TESTS", testMethods);
            put("AFTER", afterMethods);
        }};
    }

    private static void addOneMethodInCollection(Method method, List<Method> listMethods, String msg) {
        // Добавляет только один метод в коллекцию,
        // для повторяющихся выводит предупреждение
        if (listMethods.isEmpty() || listMethods.size() == 1) {
            listMethods.add(method);
        } else {
            ColorText.printWarning(String.format("There should be only one method with \"%s\" annotation", msg));
        }
    }

    public static void runTests(Class<?> clazz) {

        int all = 0;
        int passed = 0;
        int failed = 0;

        Map<String, List<Method>> methods = getAnnotatedMethods(clazz);

        Method beforeMethod = methods.get("BEFORE").size() == 1 ? methods.get("BEFORE").get(0) : null;
        Method afterMethod = methods.get("AFTER").size() == 1 ? methods.get("AFTER").get(0) : null;
        List<Method> testsMethod = methods.get("TESTS").isEmpty() ? null : methods.get("TESTS");

        if (testsMethod == null) {
            ColorText.printColorString("Tests not found", ColorText.YELLOW);
            return;
        }


        for (Method method : testsMethod) {
            // Если исключение возникает при создании сущности или в методах BEFORE,
            // то нет смысла пробовать запускать остальные тесты, поэтому выходим из метода.
            // Если исключение в тесте или AFTER, считаем тест проваленным, запускаем следующий
            StatusTest statusTest = runTest(clazz, beforeMethod, method, afterMethod);

            switch (statusTest) {
                case BEFORE_ERROR, CREATE_INSTANCE_ERROR -> {
                    return;
                }
                case TEST_FAILED, AFTER_ERROR -> failed++;
                case TEST_PASSED -> passed++;
            }
            all++;
        }

        // Вывод статистики
        ColorText.print(String.format("PASSED: %s, FAILED: %s, ALL: %s%n", passed, failed, all));
    }

    private static StatusTest runTest(Class<?> clazz, Method beforeMethod, Method method, Method afterMethod) {

        StatusTest testStatus;

        Object instance = createInstance(clazz);
        String nameMethod = method.getName();

        if (instance == null) {
            ColorText.printException("Failed to create test class instance");
            ColorText.print();
            return StatusTest.CREATE_INSTANCE_ERROR;
        }

        // Before method
        if (runBeforeTest(instance, beforeMethod, afterMethod)) {
            ColorText.printInfo(nameMethod + ": BEFORE");
        } else {
            ColorText.printException("Exception in method \"BEFORE\"");
            ColorText.print();
            return StatusTest.BEFORE_ERROR;
        }

        // Test method
        if (runMethod(instance, method)) {
            ColorText.printColorString(nameMethod + ": PASSED", ColorText.GREEN);
            testStatus = StatusTest.TEST_PASSED;
        } else {
            ColorText.printException(nameMethod + ": FAILED");
            testStatus = StatusTest.TEST_FAILED;
        }

        // After method
        if (runAfterTest(instance, afterMethod)) {
            ColorText.printInfo(nameMethod + ": AFTER");
        } else {
            ColorText.printException("Exception in method \"AFTER\"");
            ColorText.print();
            return StatusTest.AFTER_ERROR;
        }
        ColorText.print();
        return testStatus;
    }

    private static boolean runMethod(Object instance, Method method) {
        try {
            method.invoke(instance);
            return true;
        } catch (InvocationTargetException | IllegalAccessException ex) {
            return false;
        }
    }

    private static Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e
        ) {
            return null;
        }
    }


    private static boolean runBeforeTest(Object instance, Method beforeMethod, Method afterMethod) {
        /*
         * Если Before отсутствует, возвращаем true (метод отработал успешно).
         * Иначе, запускаем Before. Если выполнился упешно - возвращаем true.
         * Если произошло исключение - пробуем запустить after метод, но в любом случае возвращаем false
         */
        if (beforeMethod == null) {
            return true;
        }

        if (runMethod(instance, beforeMethod)) {
            return true;
        }

        runAfterTest(instance, afterMethod);
        return false;
    }

    private static boolean runAfterTest(Object instance, Method afterMethod) {

        if (afterMethod == null) {
            return true;
        }
        return runMethod(instance, afterMethod);
    }
}


