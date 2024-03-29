package logging;

public class TestLogging implements TestLoggingInterface {
    @Log
    @Override
    public void calculation(int param1) {
        System.out.println("Result: " + param1);
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.println("Result: " + (param1 + param2));
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println("Result: " + (param1 + param2) + " " + param3);
    }
}
