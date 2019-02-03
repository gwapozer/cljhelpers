public class c_wrapper {

    static
    {
        System.loadLibrary("NumberList");
    }

    c_wrapper()
    {
        initCppSide();
    }

    public native void addNumber(int n);
    public native int size();
    public native int getNumber(int i);
    private native void initCppSide();
    private int numberListPtr_;
}
