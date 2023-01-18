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
}
