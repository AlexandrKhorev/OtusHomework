package test.framework.utils.assertion;

public final class Assert {
    public static Equals assertThat(Object obj1){
        return new Equals(obj1);
    }
}

