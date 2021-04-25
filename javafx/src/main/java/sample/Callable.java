package sample;

public class Callable implements java.util.concurrent.Callable<Object> {
    private Object object;

    public Callable(Object anyObject) {
        object = anyObject;
    }

    @Override
    public Object call() throws Exception {
        return null;
    }

    public static abstract class callback1<T> {
        public abstract void run(T value);
    }

    public static abstract class callback2<T, J> {
        public abstract void run(T value, J value2);
    }

    public static abstract class callback3<T, J, Z> {
        public abstract void run(T value, J value2, Z value3);
    }


    public static abstract class callbackret1<R, T> {
        public abstract R run(T value);
    }

    public static abstract class callbackret2<R, T, J> {
        public abstract R run(T value, J value2);
    }

    public static abstract class callbackret3<R, T, J, Z> {
        public abstract R run(T value, J value2, Z value3);
    }
}
