package baidu.com.ndkdemo;

public class NativeMethod {
    static {
        System.loadLibrary("JniTest");
    }

    public native String getNativeStr();

    public native void callJavaCode();

    public void printStrFromNative(String str) {
        System.out.println("String From Native : " + str);
    }
}
