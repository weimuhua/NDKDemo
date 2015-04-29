package baidu.com.ndkdemo.sniffer;

public class Sniffer {
    static {
        System.loadLibrary("");//TODO
    }

    /**
     * @param protocol 表示捕获包的协议类型；
     * @param packetCounts 表示捕获包的数量，-1表示无限抓包。
     */
    public native void startSniff(String protocol, int packetCounts);

    /**
     * 底层抓包异常回掉接口
     */
    public void onExpception() {

    }
}
