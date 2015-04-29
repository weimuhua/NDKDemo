// ICommonService.aidl
package baidu.com.ndkdemo;

import baidu.com.ndkdemo.IChangeListener;

// Declare any non-default types here with import statements

interface ICommonService {
    String getNativeStr();

    void startSniff(String protocol, int packetCounts, IChangeListener l);
}
