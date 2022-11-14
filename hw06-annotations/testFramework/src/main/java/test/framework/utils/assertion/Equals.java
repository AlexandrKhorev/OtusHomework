package test.framework.utils.assertion;

public final class Equals {
    private Object object1;

    Equals(Object object1) {
        this.object1 = object1;
    }

    public void equalsTo(Object object2) {
        if (object1.equals(object2)) {
            return;
        }
        throw new AssertionError(String.format("%s don't equals %s", object1, object2));
    }
}
