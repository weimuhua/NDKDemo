package baidu.com.ndkdemo.sniffer;

import baidu.com.ndkdemo.IChangeListener;

public class SnifferMgr {

    private static final String TAG = "SnifferMgr";
    private static final boolean DEBUG = true;

    private static SnifferMgr sInstance;
    private Sniffer mSniffer;

    public static SnifferMgr getsInstance() {
        if (sInstance == null) {
            synchronized (SnifferMgr.class) {
                if (sInstance == null) {
                    sInstance = new SnifferMgr();
                }
            }
        }
        return sInstance;
    }

    public SnifferMgr() {
        mSniffer = new Sniffer();
    }

    public void startSniff(String protocol, int packetCounts, IChangeListener l) {
        //TODO
    }
}
