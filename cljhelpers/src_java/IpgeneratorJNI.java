public class IpgeneratorJNI
{
    static
    {
        System.out.println("Loading C library from Java!");
        //System.load(System.getProperty("user.dir") + "/resources/c_libs/libIpgeneratorJNI.so");
        System.load(System.getProperty("user.dir") + "/target/jni/libIpgeneratorJNI.so");
    }

    //public static native Object IpgeneratorJNI();
    //public static native String IPrandom();
    public static native String print();
}
