package baidu.com.ndkdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import baidu.com.ndkdemo.sniffer.SnifferMgr;

public class CommonService extends Service {
    private static final String TAG = "CommonService";
    private static final boolean DEBUG = true;

    private final ICommonService.Stub mBinder = new ICommonService.Stub() {
        @Override
        public String getNativeStr() throws RemoteException {
            NativeMethod method = new NativeMethod();
            return method.getNativeStr();
        }

        @Override
        public void startSniff(String protocol, int packetCounts, IChangeListener l) throws RemoteException {
            SnifferMgr.getsInstance().startSniff(protocol, packetCounts, l);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
