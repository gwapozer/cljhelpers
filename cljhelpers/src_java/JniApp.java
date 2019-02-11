public class JniApp {

    // --- Native methods
    public native int intMethod(int n);
    public native boolean booleanMethod(boolean bool);
    public native String stringMethod(String text);
    public native int intArrayMethod(int[] intArray);
    public native String IpRandom(String text);

    // --- Main method to test our native library
    public static void main(String[] args) {

        System.load(System.getProperty("user.dir") + "/target/jni/libJniApp.so");

        JniApp sample = new JniApp();
        int square = sample.intMethod(5);
        boolean bool = sample.booleanMethod(true);
        String text = sample.stringMethod("java");
        int sum = sample.intArrayMethod(new int[] {1, 1, 2, 3, 5, 8, 13});
        String ipran = sample.IpRandom("Random Gen");

        System.out.println("intMethod: " + square);
        System.out.println("booleanMethod: " + bool);
        System.out.println("stringMethod: " + text);
        System.out.println("intArrayMethod: " + sum);
        System.out.println("IpRandom: " + ipran);
    }
}
