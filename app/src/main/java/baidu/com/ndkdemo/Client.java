package baidu.com.ndkdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private static final String TAG = "Client";
    private static final boolean DEBUG = true;

    public static final String BIND_ACTION = "com.baidu.action.NDK_DEMO";

    private static Client sInstance;
    private Context mAppContext;
    private String mPkgName;
    private ICommonService mService;
    private AtomicBoolean mConnecting = new AtomicBoolean(false);

    public static Client getInstance(Context cxt) {
        if (sInstance == null) {
            synchronized (Client.class) {
                if (sInstance == null) {
                    sInstance = new Client(cxt);
                }
            }
        }
        return sInstance;
    }

    public Client(Context cxt) {
        mAppContext = cxt.getApplicationContext();
        mPkgName = mAppContext.getPackageName();
    }

    private void connectServiceIfNeed() {
        if (mService != null || mConnecting.get()) {
            if (DEBUG) {
                Log.d(TAG, "service is running...");
            }
            return;
        }

        mConnecting.set(true);

        Intent intent = new Intent(BIND_ACTION);
        intent.setPackage(mPkgName);
        List<ResolveInfo> servicesList = mAppContext.getPackageManager().queryIntentServices(intent, 0);
        if (servicesList == null || servicesList.size() == 0) {
            if (DEBUG) Log.d(TAG, "no service available, cannot connect");
            mConnecting.set(false);
            return;
        }

        ServiceConnection connection = new ServiceConnection() {
            private boolean mConnectLost = false;

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (DEBUG) {
                    Log.d(TAG, "service connected: " + name + ", binder: " + service
                            + ", connLost: " + mConnectLost);
                }
                if (!mConnectLost) {
                    mService = ICommonService.Stub.asInterface(service);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if (DEBUG)
                    Log.d(TAG, "service disconnected: " + name + ", connLost: " + mConnectLost);
                if (mConnectLost) {
                    return;
                }

                mConnectLost = true;
                mService = null;
                mAppContext.unbindService(this);
                mConnecting.set(false);
            }
        };

        if (DEBUG) Log.d(TAG, "connect service...");
        if (!mAppContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            if (DEBUG) Log.d(TAG, "cannot connect");
            mConnecting.set(false);
        }
    }

    private void waitForConnected(long timeoutMs) {
        if (Looper.myLooper() == Looper.getMainLooper() && timeoutMs != 0) {
            throw new RuntimeException("Cannot be invoked in UI thread");
        }

        if (mService != null) return;

        synchronized (mConnecting) {
            connectServiceIfNeed();
            long timeElapsed = 0;
            while (true) {
                if (DEBUG)
                    Log.d(TAG, "checking mService: " + mService + ", s: " + mConnecting.get());
                if (mService != null || !mConnecting.get()) {
                    if (DEBUG) {
                        Log.d(TAG, "mService != null, return");
                    }
                    return;
                }

                if (timeoutMs >= 0 && timeElapsed >= timeoutMs) {
                    if (DEBUG) {
                        Log.d(TAG, "timeoutMs >= 0 && timeElapsed >= timeoutMs, return");
                    }
                    return;
                }
                connectServiceIfNeed();
                if (DEBUG) {
                    Log.d(TAG, "sleep 100ms...");
                }
                SystemClock.sleep(100);
                timeElapsed += 100;
            }
        }
    }

    private void waitForConnected() {
        waitForConnected(-1);
    }

    public String getNativeString(boolean waitConnected) throws RemoteException {
        if (waitConnected) {
            waitForConnected();
        } else {
            connectServiceIfNeed();
        }
        String str = "";
        if (mService != null) {
            str = mService.getNativeStr();
        }
        return str;
    }

    public void startSniff(String protocol, int packetCounts, IChangeListener l,
            boolean waitConnected) throws RemoteException {
        if (waitConnected) {
            waitForConnected();
        } else {
            connectServiceIfNeed();
        }
        if (mService != null) {
            mService.startSniff(protocol, packetCounts, l);
        }
    }
}
