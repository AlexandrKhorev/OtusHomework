package logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

class Ioc {
    private Ioc() {
    }

    static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class},
                handler
        );
    }

    static class DemoInvocationHandler implements InvocationHandler {

        private final TestLoggingInterface myClass;
        private final Method[] myClassMethods;

        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
            this.myClassMethods = myClass.getClass().getDeclaredMethods();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Если у метода имеется аннотация Log, выводим дополнительную информацию в консоль
            if (isMethodAnnotatedLog(method)) {
                System.out.println("executed method: " + method.getName() + ", params: " + Arrays.toString(args));
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }

        private boolean isMethodAnnotatedLog(Method method) {
            // Приводим название метода интерфейса к виду, который был бы в реализации этого интерфейса.
            // Для того чтобы определить, есть ли аннотация над методом.
            String nameMethod = method.toString()
                    .replace("abstract ", "")
                    .replace(TestLoggingInterface.class.getName(), myClass.getClass().getName());

            for (Method myClassMethod : myClassMethods) {
                if (myClassMethod.toString().equals(nameMethod) && myClassMethod.isAnnotationPresent(Log.class)) {
                    return true;
                }
            }
            return false;
        }
    }
}
