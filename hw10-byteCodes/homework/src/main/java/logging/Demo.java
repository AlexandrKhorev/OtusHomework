package logging;

public class Demo {
    public void action(){

        TestLoggingInterface myClass = Ioc.createMyClass();

        myClass.calculation(1);
        System.out.println();

        myClass.calculation(2, 3);
        System.out.println();

        myClass.calculation(4, 5, "nine");
        System.out.println();
    }

    public static void main(String[] args) {
        new Demo().action();
    }

    private void testAction() {
        long start = System.currentTimeMillis();

        TestLoggingInterface myClass = Ioc.createMyClass();
        for (int i = 0; i < 1_000_000; i++) {
            myClass.calculation(1);
            myClass.calculation(2, 3);
            myClass.calculation(4, 5, "nine");
        }

        System.out.println(System.currentTimeMillis() - start);

        // До оптимизации:  ~2860
        // После:           ~1415
        // (без вывода в консоль)
    }
}
