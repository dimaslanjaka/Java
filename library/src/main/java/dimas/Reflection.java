package dimas;

public class Reflection {
    /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     *
     * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
     * @return method name
     */
    public static String getMethodName(final int depth) {
        final StackTraceElement[] ste = new Throwable().getStackTrace();

        //System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
        return ste[ste.length - depth].getMethodName();
    }

    private static String getMethodName() {
        return getMethodName(1);
    }

    private static void test() {
        System.out.println(getMethodName(2));
    }

    public static void main(String[] args) {
        test();
        System.out.println(getMethodName());
    }
}
