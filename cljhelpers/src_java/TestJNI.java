public class TestJNI {
    static {
        System.out.println("Loading C library from Java!");
        System.load(System.getProperty("user.dir") + "/resources/c_libs/libTestJNI.so");
    }

    public static native String print(String name);
    public static native String testprint();
}
